package zerobase.tableNow.domain.order.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.cart.entity.CartEntity;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Component
public class OrderMapper {
    public OrderEntity toOrderEntity(OrderDto orderDto,
                                     UsersEntity users,
                                     StoreEntity store,
                                     CartEntity cart){
        return OrderEntity.builder()
                .id(orderDto.getId())
                .userId(users)
                .storeId(store)
                .totalCount(cart.getTotalCount())
                .totalAmount(cart.getTotalAmount())
                .status(Status.PENDING)
                .build();
    }
    public OrderDto toOrderDto(OrderEntity order){
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId().getUser())
                .storeId(order.getStoreId().getStore())
                .totalCount(order.getTotalCount())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .build();
    }
}
