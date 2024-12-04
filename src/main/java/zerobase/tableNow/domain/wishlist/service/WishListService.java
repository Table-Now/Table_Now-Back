package zerobase.tableNow.domain.wishlist.service;

import zerobase.tableNow.domain.wishlist.dto.WishListRequestDto;

public interface WishListService {

    void toggleWishList(WishListRequestDto dto);

    boolean isWishListed(String user, String store);
}
