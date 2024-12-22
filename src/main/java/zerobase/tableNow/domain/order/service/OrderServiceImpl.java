package zerobase.tableNow.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.tableNow.domain.cart.entity.CartEntity;
import zerobase.tableNow.domain.cart.repository.CartRepository;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.order.mapper.OrderMapper;
import zerobase.tableNow.domain.order.repository.OrderRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    //주문 등록
    @Override
    @Transactional
    public void addOrder(OrderDto orderDto,Long cartId) {
        // 현재 로그인한 사용자 ID 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 정보 조회
        UsersEntity userEntity = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // 장바구니 항목 조회
        CartEntity cartEntity = cartRepository.findByIdAndUserId(cartId, userEntity);

        // 중복 주문 방지: 사용자의 미결 주문 존재 여부 확인
        boolean hasPendingOrder = orderRepository.existsByUserIdAndStatus(userEntity, Status.PENDING);
        if (hasPendingOrder) {
            throw new TableException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 주문 엔티티 생성
        OrderEntity order = orderMapper.toOrderEntity(orderDto, userEntity, cartEntity.getStoreId(), cartEntity);

        // 주문 저장
        orderRepository.save(order);

        // 장바구니 비우기
        cartRepository.delete(cartEntity);

    }
}
