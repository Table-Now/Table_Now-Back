package zerobase.tableNow.domain.review.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.review.dto.ReviewDto;
import zerobase.tableNow.domain.review.dto.UpdateDto;
import zerobase.tableNow.domain.review.entity.ReviewEntity;
import zerobase.tableNow.domain.review.mapper.ReviewMapper;
import zerobase.tableNow.domain.review.repository.ReviewRepository;
import zerobase.tableNow.domain.review.service.ReviewService;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final ReviewMapper reviewMapper;

    /**
     * 리뷰 등록
     *
     * @param store     상점 이름
     * @param reviewDto 리뷰 DTO
     * @return 등록된 리뷰 내용
     */
    @Override
    public ReviewDto register(ReviewDto reviewDto) {
        // 사용자 확인
        UsersEntity user = userRepository.findByUser(reviewDto.getUser())
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        // store 이름으로 StoreEntity 조회
        StoreEntity storeEntity = storeRepository.findByStore(reviewDto.getStore())
                .orElseThrow(() -> new RuntimeException("해당 상점을 찾을 수 없습니다."));

        // 해당 상점을 이용한 유저인지 확인
        boolean hasValidReservation = reservationRepository.existsByUserAndStoreAndReservationStatus(
                user,
                storeEntity,
                Status.REQ
        );
        if (!hasValidReservation) {
            throw new RuntimeException("상점을 이용 후 리뷰를 작성할 수 있습니다.");
        }

        // ReviewEntity 생성 및 저장
        ReviewEntity reviewEntity = reviewMapper.toReviewEntity(reviewDto, user);
        ReviewEntity savedEntity = reviewRepository.save(reviewEntity);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return reviewMapper.toReviewDto(savedEntity);
    }

    /**
     * 리뷰 목록
     * @param store
     * @return 리뷰 목록
     */
    @Override
    public List<ReviewDto> listByStore(String store) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByStoreOrderByCreateAtDesc(store);
        return reviewEntities.stream()
                .map(reviewMapper::toReviewDto)
                .collect(Collectors.toList());
    }

    /**
     * 리뷰 수정
     *
     * @param user      사용자 ID
     * @param reviewDto 수정할 리뷰 DTO
     * @return 수정된 리뷰 내용
     */
    @Override
    public UpdateDto update(UpdateDto dto) {
        // 사용자 확인
        UsersEntity users = userRepository.findByUser(dto.getUser())
                .orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));

        ReviewEntity existingReview = reviewRepository.findByUser(users)
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 없습니다."));

        // 리뷰 작성자와 현재 사용자가 일치하는지 확인
        if (!existingReview.getUser().getUser().equals(dto.getUser())) {
            throw new RuntimeException("리뷰 작성자만 수정할 수 있습니다.");
        }

        // 기존 리뷰 엔티티 업데이트
        existingReview.setStore(dto.getStore());
        existingReview.setContents(dto.getContents());

        // 변경된 엔티티 저장
        ReviewEntity updatedReview = reviewRepository.save(existingReview);

        // 업데이트된 엔티티를 DTO로 변환하여 반환
        return reviewMapper.toUpdateDto(updatedReview);
    }

    /**
     * 리뷰 삭제
     *
     * @param user 사용자 ID
     */
    @Override
    public void delete(String user, Long id) {
        // 1. 현재 로그인한 사용자 정보 조회
        UsersEntity currentUser = userRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("아이디 " + user + "를 가진 사용자를 찾을 수 없습니다."));

        // 2. 특정 리뷰 ID로 리뷰 조회
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 리뷰를 찾을 수 없습니다."));

        // 3. 권한 확인
        boolean isReviewAuthor = review.getUser().getUser().equals(user);
        boolean isManager = currentUser.getRole() == Role.MANAGER;
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        // 4. 리뷰 작성자 확인 및 권한 검증
        if (!isReviewAuthor && !isManager && !isAdmin) {
            throw new AccessDeniedException("이 리뷰를 삭제할 권한이 없습니다.");
        }

        // 5. 리뷰 삭제
        reviewRepository.delete(review);
    }

}

