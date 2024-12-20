package zerobase.tableNow.domain.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.cart.dto.CartDto;
import zerobase.tableNow.domain.cart.service.CartService;

import java.util.List;

/**
 * 장바구니 관련 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(name = "/cart/")
public class CartController {
    private final CartService cartService;

    //장바구니 등록
    @PostMapping("addCart/{storeId}")
    public ResponseEntity<CartDto> addCart(@Valid  @PathVariable(name = "storeId")Long storeId,
                                           @RequestBody CartDto cartDto){
        return ResponseEntity.ok().body(cartService.addCart(storeId,cartDto));
    }

    //내 장바구니 리스트
    @GetMapping("use/{userId}")
    public ResponseEntity<List<CartDto>> cartList (@PathVariable(name = "userId")
                                                      Long userId){
        return ResponseEntity.ok().body(cartService.cartList(userId));
    }

}
