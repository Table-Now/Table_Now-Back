package zerobase.tableNow.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.order.repository.OrderRepository;
import zerobase.tableNow.domain.order.service.OrderService;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
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
        UsersEntity users = userRepository.findByUser(orderDto.getUserId())
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        StoreEntity store = storeRepository.findById(orderDto.getStoreId())
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        String merchantUid = UUID.randomUUID().toString(); // 랜덤 UUID 생성

        // 주문 엔티티 생성
        OrderEntity orderEntity = OrderEntity.builder()
                .user(users)
                .store(store)
                .orderName(orderDto.getOrderName())
                .merchantUid(merchantUid)
                .totalAmount(orderDto.getTotalAmount())
                .paymentStatus(false) // 결제 미완료
                .build();

        // 데이터베이스에 저장
        orderEntity = orderRepository.save(orderEntity);

        // 저장된 엔티티를 기반으로 DTO 반환
        return OrderDto.builder()
                .id(orderEntity.getId())
                .userId(orderEntity.getUser().getUser())
                .storeId(orderEntity.getStore().getId())
                .orderName(orderEntity.getOrderName())
                .merchantUid(orderEntity.getMerchantUid())
                .totalAmount(orderEntity.getTotalAmount())
                .paymentStatus(orderEntity.getPaymentStatus())
                .build();

    }
}