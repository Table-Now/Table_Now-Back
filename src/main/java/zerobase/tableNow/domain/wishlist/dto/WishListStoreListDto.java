package zerobase.tableNow.domain.wishlist.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishListStoreListDto {
    private Long userId;
    private Long storeId;
    private String store;
    private String storeImg;
}
