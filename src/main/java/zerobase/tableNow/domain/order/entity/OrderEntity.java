package zerobase.tableNow.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.order.orderDetail.entity.OrderDetailEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders_table")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user")
    private String user;

    @Column(name = "takeout_name")
    private String takeoutName;

    @Column(name = "takeout_phone")
    private String takeoutPhone;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "pay_method")
    private String payMethod;

    private String impUid;

    @Builder.Default  // 이 부분 추가
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();

    public void addOrderDetail(OrderDetailEntity detail) {
        if (orderDetails == null) {
            orderDetails = new ArrayList<>();
        }
        orderDetails.add(detail);
        detail.setOrders(this);
    }

}
