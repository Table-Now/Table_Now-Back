package zerobase.tableNow.domain.wishlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.wishlist.dto.WishListRequestDto;
import zerobase.tableNow.domain.wishlist.dto.WishListStoreListDto;
import zerobase.tableNow.domain.wishlist.service.WishListService;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@Slf4j
public class WishListController {

    private final WishListService wishListService;

    //좋아요토글
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleWishList(@RequestBody WishListRequestDto dto) {
        // 서비스 메서드 호출
        wishListService.toggleWishList(dto);

        // 성공 메시지 반환
        return ResponseEntity.ok("Success");
    }

    //좋아요상황
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkWishList(
            @RequestParam(name = "user") String user,
            @RequestParam(name = "store") String store
    ) {
        boolean isWished = wishListService.isWishListed(user, store);
        return ResponseEntity.ok(isWished);
    }

    //찜 목록
    @GetMapping("/")
    public ResponseEntity<List<WishListStoreListDto>> wishList(@RequestParam(name = "user") String user){
        return ResponseEntity.ok().body(wishListService.wishList(user));
    }
}
