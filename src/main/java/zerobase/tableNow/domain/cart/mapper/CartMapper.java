package zerobase.tableNow.domain.cart.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.cart.dto.CartDto;
import zerobase.tableNow.domain.cart.entity.CartEntity;

@Component
public class CartMapper {
    public CartDto toCartDto(CartEntity cartEntity) {
        return CartDto.builder()
                .id(cartEntity.getId())
                .userId(cartEntity.getUserId().getId())
                .menuId(cartEntity.getMenuId().getId())
                .storeId(cartEntity.getStoreId().getId())
                .totalCount(cartEntity.getTotalCount())
                .totalAmount(cartEntity.getTotalAmount())
                .build();
    }
}
