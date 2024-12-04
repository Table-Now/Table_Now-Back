package zerobase.tableNow.domain.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.domain.wishlist.dto.WishListRequestDto;
import zerobase.tableNow.domain.wishlist.entity.WishListEntity;
import zerobase.tableNow.domain.wishlist.repository.WishListRepository;
import zerobase.tableNow.domain.wishlist.service.WishListService;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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


}
