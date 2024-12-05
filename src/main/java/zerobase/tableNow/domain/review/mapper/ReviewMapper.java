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
    public ReviewEntity toReviewEntity(ReviewDto reviewDto ,
                                       UsersEntity optionalUsers){
        String hashPassword = passwordEncoder.encode(reviewDto.getPassword());

        return ReviewEntity.builder()
                .id(reviewDto.getId())
                .user(optionalUsers)
                .store(reviewDto.getStore())
                .contents(reviewDto.getContents())
                .role(reviewDto.getRole())
                .secretReview(reviewDto.getSecretReview())
                .password(hashPassword)
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
