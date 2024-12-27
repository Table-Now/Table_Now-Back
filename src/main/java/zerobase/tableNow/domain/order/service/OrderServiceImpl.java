package zerobase.tableNow.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.tableNow.domain.order.dto.OrderCheckDto;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.order.orderDetail.dto.OrderDetailDto;
import zerobase.tableNow.domain.order.orderDetail.entity.OrderDetailEntity;
import zerobase.tableNow.domain.order.repository.OrderRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    /**
     * 주문 정보 임시저장
     * @param orderDto
     * @return
     */
    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // OrderEntity 생성 시 orderDetails 초기화
        OrderEntity orderEntity = OrderEntity.builder()
                .user(userId)
                .takeoutName(orderDto.getTakeoutName())
                .takeoutPhone(orderDto.getTakeoutPhone())
                .totalAmount(orderDto.getTotalAmount())
                .payMethod(orderDto.getPayMethod())
                .orderDetails(new ArrayList<>())  // 명시적 초기화
                .build();

        // 주문 상세 정보 생성 및 연결
        orderDto.getOrderDetails().forEach(detailDto -> {
            OrderDetailEntity detail = OrderDetailEntity.builder()
                    .orders(orderEntity)
                    .menuId(detailDto.getMenuId())
                    .menu(detailDto.getMenu())
                    .menuCount(detailDto.getMenuCount())
                    .totalPrice(detailDto.getTotalPrice())
                    .build();
            orderEntity.addOrderDetail(detail);
        });

        // 주문 저장
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        // DTO 변환 및 반환
        return OrderDto.builder()
                .totalAmount(savedOrder.getTotalAmount())
                .payMethod(savedOrder.getPayMethod())
                .user(savedOrder.getUser())
                .orderDetails(savedOrder.getOrderDetails().stream()
                        .map(detail -> OrderDetailDto.builder()
                                .menuId(detail.getMenuId())
                                .menuCount(detail.getMenuCount())
                                .totalPrice(detail.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public OrderCheckDto getOrderCheck(String user) {
        // 현재 인증된 사용자 확인
        String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 정보가 일치하지 않으면 예외 발생
        if (!authenticatedUser.equals(user)) {
            throw new RuntimeException("Unauthorized access: You can only view your own payment information.");
        }

        // OrderEntity 조회
        OrderEntity orderEntity = orderRepository.findByUser(user)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // OrderEntity -> OrderCheckDto 변환
        return OrderCheckDto.builder()
                .takeoutName(orderEntity.getTakeoutName())
                .takeoutPhone(orderEntity.getTakeoutPhone())
                .totalAmount(orderEntity.getTotalAmount())
                .payMethod(orderEntity.getPayMethod())
                .orderDetails(orderEntity.getOrderDetails().stream()
                        .map(detail -> OrderDetailDto.builder()
                                .menuId(detail.getMenuId())
                                .menu(detail.getMenu())
                                .menuCount(detail.getMenuCount())
                                .totalPrice(detail.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}