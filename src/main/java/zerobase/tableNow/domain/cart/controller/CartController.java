package zerobase.tableNow.domain.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zerobase.tableNow.domain.cart.dto.CartDto;
import zerobase.tableNow.domain.cart.service.CartService;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    //포장메뉴등록
    @PostMapping("register")
    public ResponseEntity<CartDto> register(@RequestBody CartDto cartDto){
        return ResponseEntity.ok().body(cartService.register(cartDto));
    }
}
