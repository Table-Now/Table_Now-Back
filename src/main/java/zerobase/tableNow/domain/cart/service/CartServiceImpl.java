package zerobase.tableNow.domain.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import zerobase.tableNow.domain.cart.mapper.CartMapper;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.store.controller.menu.entity.MenuEntity;
import zerobase.tableNow.domain.store.controller.menu.repository.MenuRepository;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.cart.dto.CartDto;
import zerobase.tableNow.domain.cart.entity.CartEntity;
import zerobase.tableNow.domain.cart.repository.CartRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;
    private final CartMapper cartMapper;

    //장바구니 등록
    @Override
    @Transactional
    public CartDto addCart(Long storeId, CartDto cartDto) {
        // 현재 로그인한 사용자 ID 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 정보 조회
        UsersEntity userEntity = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // 해당 사용자의 예약 정보 조회
        ReservationEntity reservationEntity = reservationRepository.findByPhone(userEntity.getPhone())
                .orElseThrow(() -> new TableException(ErrorCode.RESERVATION_NOT_FOUND));

        // 대기번호가 부여된 상태인지 확인
        if (reservationEntity.getWaitingNumber() == null) {
            throw new TableException(ErrorCode.FORBIDDEN_ACCESS, "대기번호가 부여되지 않은 상태에서는 주문이 불가능합니다.");
        }

        // 매장 정보 조회
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        // 메뉴 정보 조회 및 검증
        MenuEntity menuEntity = menuRepository.findById(cartDto.getMenuId())
                .orElseThrow(() -> new TableException(ErrorCode.MENU_NOT_FOUND));

        if (!menuEntity.getStore().getId().equals(storeId)) {
            throw new TableException(ErrorCode.FORBIDDEN_ACCESS, "해당 매장의 메뉴가 아닙니다.");
        }

        // 금액 총합산 로직
        int totalAmount = menuEntity.getPrice() * cartDto.getTotalCount();

        // 장바구니 엔티티 생성
        CartEntity cartEntity = CartEntity.builder()
                .user(userEntity)
                .menu(menuEntity)
                .store(storeEntity)
                .totalCount(cartDto.getTotalCount())
                .totalAmount(totalAmount)
                .build();

        // 데이터베이스에 저장
        CartEntity savedEntity = cartRepository.save(cartEntity);

        // DTO 반환
        CartDto cartDtoResponse = cartMapper.toCartDto(savedEntity);
        cartDtoResponse.setTotalAmount(totalAmount);  // 총합을 DTO에 추가

        return cartDtoResponse;
    }

    //장바구니 목록
    @Override
    public List<CartDto> cartList(String userId) {
        List<CartEntity> cartEntities = cartRepository.findByUserId_User(userId);

        return cartEntities.stream()
                .map(cartMapper::toCartDto)
                .collect(Collectors.toList());
    }

    //장바구니 삭제
    @Override
    @Transactional
    public void cartDelete(Long cartId) {
        // 현재 로그인한 사용자 ID 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        UsersEntity userEntity = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // 장바구니 ID가 존재하는지 확인
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new TableException(ErrorCode.CART_NOT_FOUND));

        // 로그인한 사용자와 장바구니의 소유자가 일치하는지 확인
        if (!cart.getUser().equals(userEntity)) {
            throw new TableException(ErrorCode.FORBIDDEN_ACCESS);
        }
        cartRepository.delete(cart);
    }

    //장바구니 수정
    @Override
    @Transactional
    public void updateCart(String userId,CartDto cartDto) {
        CartEntity cart = cartRepository.findByUser_UserAndMenu_Id(userId, cartDto.getMenuId())
                .orElseThrow(() -> new TableException(ErrorCode.CART_NOT_FOUND));

        MenuEntity menu = menuRepository.findById(cartDto.getMenuId())
                .orElseThrow(() -> new TableException(ErrorCode.MENU_NOT_FOUND));

        cart.setTotalCount(cartDto.getTotalCount());
        cart.setTotalAmount(menu.getPrice() * cartDto.getTotalCount());
    }

}
