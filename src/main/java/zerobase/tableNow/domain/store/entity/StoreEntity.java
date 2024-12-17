package zerobase.tableNow.domain.store.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "store")
public class StoreEntity extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private UsersEntity user;
    private String phone;

    private String store;
    private String storeLocation;
    @Column(name = "storeImg")
    private String storeImg;
    private String storeContents;

    @Column(nullable = true)
    @Builder.Default
    private Integer rating = 0; // 별점
    private String storeOpen;
    private String storeClose;
    private String storeWeekOff;

    private double latitude;  // 위도 추가
    private double longitude; // 경도 추가

    @Transient
    private Double distance; // 거리순

    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<ReservationEntity> reservations = new ArrayList<>();

}
