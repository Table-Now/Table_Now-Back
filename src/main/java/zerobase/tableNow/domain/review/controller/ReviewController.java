package zerobase.tableNow.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.review.dto.PasswordRequestDto;
import zerobase.tableNow.domain.review.dto.ReviewDto;
import zerobase.tableNow.domain.review.dto.UpdateDto;
import zerobase.tableNow.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review/")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("register")
    public ResponseEntity<ReviewDto> register(@RequestBody ReviewDto reviewDto){
        return ResponseEntity.ok(reviewService.register(reviewDto));
    }

    // 리뷰 목록 조회
    @GetMapping("list")
    public ResponseEntity<List<ReviewDto>> list(@RequestParam(name = "store") String store){
        return ResponseEntity.ok().body(reviewService.listByStore(store));

    }

    // 리뷰 수정
    @PutMapping("update")
    public ResponseEntity<UpdateDto> update(@RequestBody UpdateDto dto){
        return ResponseEntity.ok().body(reviewService.update(dto));
    }

    // 리뷰 삭제
    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@RequestParam(name = "user") String user,
                                    @RequestParam(name = "id")Long id){
        reviewService.delete(user,id);
        return ResponseEntity.noContent().build();
    }
    //리뷰 비밀글 암호입력 확인 요청
    @PostMapping("passwordrequest")
    public  ResponseEntity<Boolean> passwordRequest(
            @RequestBody PasswordRequestDto passwordRequestDto){
        return ResponseEntity.ok().body(reviewService.passwordRequest(passwordRequestDto));
    }
}
