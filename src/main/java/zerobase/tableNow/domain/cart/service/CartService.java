package zerobase.tableNow.domain.cart.service;

import zerobase.tableNow.domain.cart.dto.CartDto;

import java.util.List;

public interface CartService {
    CartDto addCart(Long storeId, CartDto cartDto);

    List<CartDto> cartList(String userId);

    void cartDelete(Long cartId);

    void updateCart(Long userId,CartDto cartDto);
}
