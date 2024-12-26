package zerobase.tableNow.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.constant.PayMethod;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.orderDetail.entity.OrderDetailEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.math.BigDecimal;
import java.util.List;

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
    private Long id;

    private String user;

    @Column(name = "total_amount")
    private Long totalAmount; // 총가격

    @Column(name = "pay_method")
    private String payMethod; // 결제 방식

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetailEntity> orderDetails;
//    private String orderName; // 주문자 이름

//    @Column(length = 100, name = "merchant_uid")
//    private String merchantUid; // 주문번호

//    @Column(name = "payment_status")
//    private Boolean paymentStatus = false; // 결제 상태

//    @Enumerated(EnumType.STRING)
//    @Column(name = "pay_method")
//    private PayMethod payMethod; // 결제 방식
}
