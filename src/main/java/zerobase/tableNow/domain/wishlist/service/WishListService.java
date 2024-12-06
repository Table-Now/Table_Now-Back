package zerobase.tableNow.domain.wishlist.service;

import zerobase.tableNow.domain.wishlist.dto.WishListRequestDto;

import java.util.List;
import java.util.Map;

public interface WishListService {

    void toggleWishList(WishListRequestDto dto);

    boolean isWishListed(String user, String store);

    List<WishListRequestDto>  wishList(String user);
}