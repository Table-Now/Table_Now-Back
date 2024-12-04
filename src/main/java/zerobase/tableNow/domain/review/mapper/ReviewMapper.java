package zerobase.tableNow.domain.review.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.review.dto.ReviewDto;
import zerobase.tableNow.domain.review.dto.UpdateDto;
import zerobase.tableNow.domain.review.entity.ReviewEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Component
public class ReviewMapper {

    //리뷰 등록 Dto -> Entity
    public ReviewEntity toReviewEntity(ReviewDto reviewDto ,
                                       UsersEntity optionalUsers){
        return ReviewEntity.builder()
                .id(reviewDto.getId())
                .user(optionalUsers)
                .store(reviewDto.getStore())
                .contents(reviewDto.getContents())
                .role(reviewDto.getRole())
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
