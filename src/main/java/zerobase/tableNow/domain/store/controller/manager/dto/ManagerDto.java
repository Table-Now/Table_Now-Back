package zerobase.tableNow.domain.store.controller.manager.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerDto {
    private String user;
    private String phone;

    private String store;
    private String storeLocation;
    private String storeImg;
    private String storeContents;

    private Integer rating = 0; // 별점
    private String storeOpen;
    private String storeClose;
    private String storeWeekOff;
}
