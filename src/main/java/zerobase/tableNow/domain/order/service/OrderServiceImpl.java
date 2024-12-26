package zerobase.tableNow.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.order.orderDetail.OrderDetailDto;
import zerobase.tableNow.domain.order.orderDetail.entity.OrderDetailEntity;
import zerobase.tableNow.domain.order.orderDetail.repository.OrderDetailRepository;
import zerobase.tableNow.domain.order.repository.OrderRepository;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    /**
     * 주문 정보 임시저장
     * @param orderDto
     * @return
     */
    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (orderDto.getOrderDetails() == null || orderDto.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Order details cannot be null or empty");
        }

        // SecurityContext에서 가져온 userId를 사용
//        UsersEntity user = userRepository.findByUser(userId)
//                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

//        StoreEntity store = storeRepository.findById(orderDto.getStoreId())
//                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

//        String merchantUid = UUID.randomUUID().toString(); // 랜덤 UUID 생성

        // 주문 엔티티 생성
        final OrderEntity orderEntity = OrderEntity.builder()
                .user(userId)
                .totalAmount(orderDto.getTotalAmount())
                .payMethod(orderDto.getPayMethod())
//                .store(store)
//                .orderName(orderDto.getOrderName())
//                .merchantUid(merchantUid)
//                .paymentStatus(false) // 결제 미완료
                .build();

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        List<OrderDetailEntity> details = orderDto.getOrderDetails().stream()
                .map(detail -> OrderDetailEntity.builder()
                        .order(orderEntity)
                        .menuId(detail.getMenuId())
                        .totalPrice(detail.getTotalPrice())
                        .menuCount(detail.getMenuCount())
                        .build())
                .toList();

        savedOrder.setOrderDetails(details); // 양방향 관계 설정

        // 주문 상세 저장
        orderDetailRepository.saveAll(details);

        // 저장된 엔티티를 기반으로 DTO 반환
        return OrderDto.builder()
//                .id(orderEntity.getId())
                .totalAmount(orderEntity.getTotalAmount())
                .payMethod(orderEntity.getPayMethod())
                .orderDetails(orderEntity.getOrderDetails().stream()
                        .map(detail -> OrderDetailDto.builder()
                                .menuId(detail.getMenuId())
                                .totalPrice(detail.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
//                .storeId(orderEntity.getStore().getId())
//                .orderName(orderEntity.getOrderName())
//                .merchantUid(orderEntity.getMerchantUid())
//                .paymentStatus(orderEntity.getPaymentStatus())
                .build();

    }
}