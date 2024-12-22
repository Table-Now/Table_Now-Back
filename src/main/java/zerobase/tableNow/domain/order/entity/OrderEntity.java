package zerobase.tableNow.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.constant.PayMethod;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.math.BigDecimal;
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "order_table")
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeId;

    @Column(name = "order_name")
    private String orderName; // 주문자 이름

    @Column(length = 100, name = "merchant_uid")
    private String merchantUid; // 주문번호

    @Column(name = "total_amount")
    private BigDecimal totalAmount; // 총가격

    @Column(name = "detail_address")
    private String detailAddress; // 상세주소

    @Column(name = "post_code", length = 100)
    private String postCode; // 우편번호

    @Column(name = "payment_status")
    private Boolean paymentStatus = false; // 결제 상태

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_method")
    private PayMethod payMethod; // 결제 방식

    @Enumerated(EnumType.STRING)
    private Status status;
}
