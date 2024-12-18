package zerobase.tableNow.domain.review.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.review.dto.PasswordRequestDto;
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
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

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
    private final PasswordEncoder passwordEncoder;

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
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // store 이름으로 StoreEntity 조회
        StoreEntity storeEntity = storeRepository.findByStore(reviewDto.getStore())
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        // 해당 상점을 이용한 유저인지 확인
        boolean hasValidReservation = reservationRepository.existsByUserAndStore(
                user,
                storeEntity);

        if (!hasValidReservation) {
            throw new TableException(ErrorCode.PRODUCT_NOT_PURCHASED);
        }
        // 비밀리뷰일 경우 비밀번호 유효성 검사
        if (Boolean.TRUE.equals(reviewDto.getSecretReview())) { // secretReview가 true일 경우만 검사
            if (reviewDto.getPassword() == null || reviewDto.getPassword().length() != 4) {
                throw new TableException(ErrorCode.INVALID_PASSWORD);
            }
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
    @Transactional
    public UpdateDto update(Long reviewId,UpdateDto dto) {
        // 사용자 확인
        UsersEntity users = userRepository.findByUser(dto.getUser())
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        ReviewEntity existingReview = reviewRepository.findByUser(users)
                .orElseThrow(() -> new TableException(ErrorCode.REVIEW_NOT_FOUND));

        // 리뷰 작성자와 현재 사용자가 일치하는지 확인
        if (!existingReview.getUser().getUser().equals(dto.getUser())) {
            throw new TableException(ErrorCode.ACCESS_DENIED);
        }

        // 더티 체킹으로 엔티티 변경
        existingReview.setStore(dto.getStore());
        existingReview.setContents(dto.getContents());

        // 변경된 엔티티를 DTO로 변환하여 반환
        return reviewMapper.toUpdateDto(existingReview);
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
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // 2. 특정 리뷰 ID로 리뷰 조회
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new TableException(ErrorCode.REVIEW_NOT_FOUND));

        // 3. 권한 확인
        boolean isReviewAuthor = review.getUser().getUser().equals(user);
        boolean isManager = currentUser.getRole() == Role.MANAGER;
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        // 4. 리뷰 작성자 확인 및 권한 검증
        if (!isReviewAuthor && !isManager && !isAdmin) {
            throw new TableException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 5. 리뷰 삭제
        reviewRepository.delete(review);
    }

    //리뷰 암호확인 요청
    @Override
    public boolean passwordRequest(PasswordRequestDto passwordRequestDto) {
        //로그인한 유저 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        UsersEntity users = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        //해당 유저가 아닐시
        if (!users.getUser().equals(passwordRequestDto.getUser())){
            throw new TableException(ErrorCode.USER_NOT_FOUND);
        }

        // 리뷰 정보 가져오기
        ReviewEntity reviewEntity = reviewRepository.findById(passwordRequestDto.getId())
                .orElseThrow(()-> new TableException(ErrorCode.REVIEW_NOT_FOUND));

        //패스워드 일치 여부
        if (!passwordEncoder.matches(passwordRequestDto.getPassword(), reviewEntity.getPassword())){
            throw new TableException(ErrorCode.INVALID_PASSWORD);
        }
        return true;
    }

}

