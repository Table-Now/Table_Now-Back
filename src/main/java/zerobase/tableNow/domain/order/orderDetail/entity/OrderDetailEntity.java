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
@Table(name = "order_detail_table")
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "menu_count")
    private Long menuCount;
}
