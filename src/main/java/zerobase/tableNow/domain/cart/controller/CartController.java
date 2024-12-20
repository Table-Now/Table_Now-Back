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

    //장바구니 삭제
    @DeleteMapping("use/{cartId}")
    public ResponseEntity<String> cartDelete(@PathVariable(name = "cartId") Long cartId){
        cartService.cartDelete(cartId);
        return ResponseEntity.ok("success");
    }

    //장바구니 수정
    @PatchMapping("cart/{userId}")
    public ResponseEntity<?> updateCart(@Valid @PathVariable(name = "userId") Long userId,
                                             @RequestBody CartDto cartDto){
        cartService.updateCart(userId,cartDto);
        return ResponseEntity.ok("장바구니가 성공적으로 수정되었습니다.");
    }

}
