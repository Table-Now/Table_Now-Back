package zerobase.tableNow.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "사용자 ID는 필수 입력.")
    private String user; //상점 담당매니저

    private String phone;
    @NotBlank(message = "상점 이름 필수 입력.")
    @Size(max = 30,message = "최대 30자 입력 가능")
    private String store;// 상점 이름
    @NotBlank(message = "상점 장소 필수 입력.")
    private String storeLocation; //상점 장소
    private String storeImg; // 상점 이미지

    @NotBlank(message = "상점 필수 입력.")
    @Size(max = 255,message = "최대 255자 입력 가능")
    private String storeContents; // 상점 소개요약
    private Integer rating; // 별점
    @NotBlank(message = "Open 필수 입력.")
    private String storeOpen;
    @NotBlank(message = "Close 필수 입력.")
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
