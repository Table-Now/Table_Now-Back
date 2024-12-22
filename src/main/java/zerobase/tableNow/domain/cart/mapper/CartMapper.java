package zerobase.tableNow.domain.cart.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.cart.dto.CartDto;
import zerobase.tableNow.domain.cart.entity.CartEntity;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuDto;

import java.util.Collections;

@Component
public class CartMapper {
    public CartDto toCartDto(CartEntity cartEntity) {
        return CartDto.builder()
                .id(cartEntity.getId())
                .userId(cartEntity.getUserId().getUser()) // 사용자 ID
                .menuId(cartEntity.getId())  // 메뉴 ID 리스트
                .storeId(cartEntity.getStoreId().getId())  // 매장 ID
                .totalCount(cartEntity.getTotalCount())    // 총 개수
                .totalAmount(cartEntity.getTotalAmount())  // 총 금액
                .build();
    }
}
