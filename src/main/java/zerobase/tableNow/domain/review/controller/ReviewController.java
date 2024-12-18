package zerobase.tableNow.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.review.dto.PasswordRequestDto;
import zerobase.tableNow.domain.review.dto.ReviewDto;
import zerobase.tableNow.domain.review.dto.UpdateDto;
import zerobase.tableNow.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/reviews/")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("register")
    public ResponseEntity<ReviewDto> register(@RequestBody ReviewDto reviewDto){
        return ResponseEntity.ok(reviewService.register(reviewDto));
    }

    // 리뷰 목록 조회
    @GetMapping("review/{store}")
    public ResponseEntity<List<ReviewDto>> list(@PathVariable(name = "store") String store){
        return ResponseEntity.ok().body(reviewService.listByStore(store));

    }

    // 리뷰 수정
    @PutMapping("{reviewId}")
    public ResponseEntity<UpdateDto> update(
            @Valid @PathVariable(name = "reviewId") Long reviewId,
            @RequestBody  UpdateDto dto
    ) {
        return ResponseEntity.ok().body(reviewService.update(reviewId, dto));
    }
    // 리뷰 삭제
    @DeleteMapping("{reviewId}")
    public ResponseEntity<?> delete(@PathVariable(name = "reviewId")Long reviewId){
        // 현재 로그인한 사용자 정보 가져오기
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        // 리뷰 삭제 서비스 호출
        reviewService.delete(user, reviewId);
        return ResponseEntity.noContent().build();
    }

    //리뷰 비밀글 암호입력 확인 요청
    @PostMapping("passwordrequest")
    public  ResponseEntity<Boolean> passwordRequest(
            @RequestBody PasswordRequestDto passwordRequestDto){
        return ResponseEntity.ok().body(reviewService.passwordRequest(passwordRequestDto));
    }

}
