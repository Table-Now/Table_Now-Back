package zerobase.tableNow.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoreDto implements Serializable {
    private Long id;
    private String user; //상점 담당매니저
    private String store;// 상점 이름
    private String storeLocation; //상점 장소
    private String storeImg; // 상점 이미지
    private String storeContents; // 상점 소개요약
    private Integer rating; // 별점
    private String storeOpen;
    private String storeClose;
    private String storeWeekOff; //상점 휴무날

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @CreatedDate
    private LocalDateTime createAt; //상점등록일

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate
    private LocalDateTime updateAt; //상점정보 업데이트일

    private double latitude;  // 위도 추가
    private double longitude; // 경도 추가
    private Double distance; // 거리 정보를 저장할 필드 추가

}
