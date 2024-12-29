package zerobase.tableNow.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.tableNow.domain.cart.entity.CartEntity;
import zerobase.tableNow.domain.cart.repository.CartRepository;
import zerobase.tableNow.domain.constant.PaymentStatus;
import zerobase.tableNow.domain.order.dto.OrderCheckDto;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.order.orderDetail.dto.OrderDetailDto;
import zerobase.tableNow.domain.order.orderDetail.entity.OrderDetailEntity;
import zerobase.tableNow.domain.order.repository.OrderRepository;
import zerobase.tableNow.domain.payment.entity.PaymentEntity;
import zerobase.tableNow.domain.payment.repository.PaymentRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 주문 정보 임시저장
     * @param orderDto
     * @return
     */
    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        String portId = UUID.randomUUID().toString();

        // OrderEntity 생성 시 orderDetails 초기화
        OrderEntity orderEntity = OrderEntity.builder()
                .user(userId)
                .takeoutName(orderDto.getTakeoutName())
                .takeoutPhone(orderDto.getTakeoutPhone())
                .totalAmount(orderDto.getTotalAmount())
                .payMethod(orderDto.getPayMethod())
                .impUid(portId)
                .orderDetails(new ArrayList<>())  // 명시적 초기화
                .build();

        // 주문 상세 정보 생성 및 연결
        orderDto.getOrderDetails().forEach(detailDto -> {
            OrderDetailEntity detail = OrderDetailEntity.builder()
                    .orders(orderEntity)
                    .store(detailDto.getStore())
                    .menuId(detailDto.getMenuId())
                    .menu(detailDto.getMenu())
                    .menuCount(detailDto.getMenuCount())
                    .totalPrice(detailDto.getTotalPrice())
                    .build();
            orderEntity.addOrderDetail(detail);
        });

        // 주문 저장
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        // 결제 완료된 카트 항목 삭제 로직 추가
        List<CartEntity> userCarts = cartRepository.findByUserId_User(userId);
        List<Long> completedMenuIds = orderDto.getOrderDetails().stream()
                .map(OrderDetailDto::getMenuId)
                .toList();

        List<CartEntity> cartsToRemove = userCarts.stream()
                .filter(cart -> completedMenuIds.contains(cart.getMenu().getId()))
                .collect(Collectors.toList());

        cartRepository.deleteAll(cartsToRemove);


        // DTO 변환 및 반환
        return OrderDto.builder()
                .totalAmount(savedOrder.getTotalAmount())
                .payMethod(savedOrder.getPayMethod())
                .user(savedOrder.getUser())
                .orderDetails(savedOrder.getOrderDetails().stream()
                        .map(detail -> OrderDetailDto.builder()
                                .menuId(detail.getMenuId())
                                .store(detail.getStore())
                                .menuCount(detail.getMenuCount())
                                .totalPrice(detail.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * 결제 내역
     * @param user user
     * @return 결제 정보
     */
    @Override
    public List<OrderCheckDto> getOrderCheck(String user) {
        // 현재 인증된 사용자 확인
        String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 정보가 일치하지 않으면 예외 발생
        if (!authenticatedUser.equals(user)) {
            throw new RuntimeException("Unauthorized access: You can only view your own payment information.");
        }

        // 사용자에 해당하는 모든 OrderEntity 조회
        List<OrderEntity> orderEntities = orderRepository.findAllByUser(user);

        // OrderEntity -> OrderCheckDto 변환
        return orderEntities.stream()
                .map(orderEntity -> {
                    // OrderEntity에서 impUid 가져오기
                    String orderImpUid = orderEntity.getImpUid();

                    // PaymentEntity에서 해당 impUid를 가진 PaymentEntity 찾기
                    PaymentEntity paymentEntity = paymentRepository.findByMerchantUid(orderEntity.getImpUid());  // merchantUid로 찾음

                    // PaymentEntity가 있으면 impUid를 가져옴
                    String impUid = null;
                    PaymentStatus paymentStatus = null;
                    if (paymentEntity != null) {
                        impUid = paymentEntity.getImpUid();
                        paymentStatus = paymentEntity.getStatus();
                    }


                    // OrderCheckDto 빌드
                    return OrderCheckDto.builder()
                            .takeoutName(orderEntity.getTakeoutName())
                            .takeoutPhone(orderEntity.getTakeoutPhone())
                            .totalAmount(orderEntity.getTotalAmount())
                            .payMethod(orderEntity.getPayMethod())
                            .impUid(impUid) // 결제의 impUid 추가
                            .status(paymentStatus)
                            .orderDetails(orderEntity.getOrderDetails().stream()
                                    .map(detail -> OrderDetailDto.builder()
                                            .menuId(detail.getMenuId())
                                            .store(detail.getStore())
                                            .menu(detail.getMenu())
                                            .menuCount(detail.getMenuCount())
                                            .totalPrice(detail.getTotalPrice())
                                            .build())
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }


}