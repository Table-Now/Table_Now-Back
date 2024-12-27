package zerobase.tableNow.domain.order.orderDetail.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.order.entity.OrderEntity;

import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders_detail")
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity orders;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "menu")
    private String menu;

    @Column(name = "menu_count")
    private Long menuCount;

    @Column(name = "total_price")
    private BigDecimal totalPrice;
}
