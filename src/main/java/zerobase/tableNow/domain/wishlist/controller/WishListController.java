package zerobase.tableNow.domain.wishlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.wishlist.dto.WishListRequestDto;
import zerobase.tableNow.domain.wishlist.service.WishListService;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@Slf4j
public class WishListController {

    private final WishListService wishListService;

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleWishList(@RequestBody WishListRequestDto dto) {
        // 서비스 메서드 호출
        wishListService.toggleWishList(dto);

        // 성공 메시지 반환
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkWishList(
            @RequestParam(name = "user") String user,
            @RequestParam(name = "store") String store
    ) {
        boolean isWished = wishListService.isWishListed(user, store);
        return ResponseEntity.ok(isWished);
    }

}
