package zerobase.tableNow.domain.review.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.review.dto.ReviewDto;
import zerobase.tableNow.domain.review.dto.UpdateDto;
import zerobase.tableNow.domain.review.entity.ReviewEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Component
@RequiredArgsConstructor
public class ReviewMapper {
    private final PasswordEncoder passwordEncoder;

    //리뷰 등록 Dto -> Entity
    public ReviewEntity toReviewEntity(ReviewDto reviewDto, UsersEntity optionalUsers) {
        String hashPassword = null;

        // 비밀 리뷰일 때만 비밀번호를 처리
        if (Boolean.TRUE.equals(reviewDto.getSecretReview())) {
            // 비밀번호가 없으면 예외를 던지거나 처리하도록 할 수 있습니다.
            if (reviewDto.getPassword() == null) {
                throw new IllegalArgumentException("Password cannot be null for secret reviews");
            }
            // 비밀번호 해싱
            hashPassword = passwordEncoder.encode(reviewDto.getPassword());
        }

        return ReviewEntity.builder()
                .id(reviewDto.getId())
                .user(optionalUsers)
                .store(reviewDto.getStore())
                .contents(reviewDto.getContents())
                .role(reviewDto.getRole())
                .secretReview(reviewDto.getSecretReview())
                .password(hashPassword)  // 비밀번호가 null이면 비밀번호 필드를 null로 저장
                .build();
    }

    //리뷰 등록 Entity -> Dto
    public ReviewDto toReviewDto(ReviewEntity reviewEntity){
        return ReviewDto.builder()
                .id(reviewEntity.getId())
                .user(reviewEntity.getUser().getUser())
                .store(reviewEntity.getStore())
                .contents(reviewEntity.getContents())
                .role(reviewEntity.getRole())
                .secretReview(reviewEntity.getSecretReview())
                .password(reviewEntity.getPassword())
                .build();
    }

    //리뷰 수정
    public UpdateDto toUpdateDto(ReviewEntity reviewEntity){
        return UpdateDto.builder()
                .id(reviewEntity.getId())
                .user(reviewEntity.getUser().getUser())
                .store(reviewEntity.getStore())
                .contents(reviewEntity.getContents())
                .build();
    }
}
