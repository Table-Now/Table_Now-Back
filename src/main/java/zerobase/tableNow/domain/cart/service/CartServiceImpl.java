package zerobase.tableNow.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    //장바구니 등록
    @Override
    public CartDto register(CartDto cartDto) {
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
            throw new TableException(ErrorCode.FORBIDDEN_ACCESS,"대기번호가 부여되지 않은 상태에서는 주문이 불가능합니다.");
        }

        // 메뉴 및 매장 정보 조회
        MenuEntity menuEntity = menuRepository.findById(cartDto.getMenuId())
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));
        StoreEntity storeEntity = storeRepository.findById(cartDto.getStoreId())
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_PURCHASED));

        // TakeoutEntity 생성
        CartEntity cartEntity = CartEntity.builder()
                .userId(userEntity)
                .menuId(menuEntity)
                .storeId(storeEntity)
                .count(cartDto.getCount())
                .build();
        // 데이터베이스에 포장 메뉴 저장
        CartEntity savedEntity = cartRepository.save(cartEntity);


        // 저장된 엔티티를 DTO로 변환하여 반환
        return CartDto.builder()
                .id(savedEntity.getId())
                .userId(savedEntity.getUserId().getId())
                .menuId(savedEntity.getMenuId().getId())
                .storeId(savedEntity.getStoreId().getId())
                .count(savedEntity.getCount())
                .build();

    }
}
