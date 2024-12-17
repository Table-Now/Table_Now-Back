package zerobase.tableNow.domain.store.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoreRequestDto {
    private String user; //상점 담당매니저
    private String phone;
    private String store;// 상점 이름
    private String storeLocation; //상점 장소
    private String storeImg; // 상점 이미지
    private String storeContents; // 상점 소개요약
    private Integer rating; // 별점
    private String storeOpen;
    private String storeClose;
    private String storeWeekOff; //상점 휴무날
}
