package zerobase.tableNow.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.review.entity.ReviewEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // 리뷰 목록 조회
    List<ReviewEntity> findAllByStoreOrderByCreateAtDesc(String store);

    // 리뷰 수정 (리뷰가 있는지)
    Optional<ReviewEntity> findByUser(UsersEntity user);
}