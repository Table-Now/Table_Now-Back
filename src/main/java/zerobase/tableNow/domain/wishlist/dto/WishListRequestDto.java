package zerobase.tableNow.domain.wishlist.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishListRequestDto {
    private String user;
    private String store;
}
