package zerobase.tableNow.domain.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.domain.wishlist.dto.WishListRequestDto;
import zerobase.tableNow.domain.wishlist.dto.WishListStoreListDto;
import zerobase.tableNow.domain.wishlist.entity.WishListEntity;
import zerobase.tableNow.domain.wishlist.repository.WishListRepository;
import zerobase.tableNow.domain.wishlist.service.WishListService;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishListServiceImpl implements WishListService {
    private final UserRepository usersRepository;
    private final StoreRepository storeRepository;
    private final WishListRepository wishListRepository;

    @Override
    public void toggleWishList(WishListRequestDto dto) {

        UsersEntity user = usersRepository.findByUser(dto.getUser())
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        StoreEntity store = storeRepository.findByStore(dto.getStore())
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        Optional<WishListEntity> existingWish = wishListRepository.findByUserAndStore(user, store);

        if (existingWish.isPresent()) {
            wishListRepository.delete(existingWish.get());  // 삭제
        } else {
            // 좋아요 추가
            WishListEntity newWish = WishListEntity.builder()
                    .user(user)
                    .store(store)
                    .build();
            wishListRepository.save(newWish);  // 추가
        }
    }

    @Override
    public boolean isWishListed(String user, String store) {

        UsersEntity users = usersRepository.findByUser(user)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        StoreEntity stores = storeRepository.findByStore(store)
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        return wishListRepository.findByUserAndStore(users, stores).isPresent();
    }

    @Override
    public List<WishListStoreListDto> wishList(String user) {
        // 현재 로그인한 사용자 정보 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 해당 유저의 정보를 찾음 (userId 기준)
        UsersEntity users = usersRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // 유저의 찜 목록을 리스트로 조회 (userId를 기준으로 조회)
        List<WishListEntity> wishListEntities = wishListRepository.findByUser(users);

        List<WishListStoreListDto> wishListDtos = wishListEntities.stream()
                .map(wishListEntity -> new WishListStoreListDto(
                        wishListEntity.getUser().getId(),
                        wishListEntity.getStore().getId(),
                        wishListEntity.getStore().getStore(),
                        wishListEntity.getStore().getStoreImg()
                ))
                .collect(Collectors.toList());

        return wishListDtos;
    }


}
