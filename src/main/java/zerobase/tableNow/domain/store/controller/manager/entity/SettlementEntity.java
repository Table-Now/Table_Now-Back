package zerobase.tableNow.domain.store.controller.manager.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "settlement")
public class SettlementEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    private String menu;
    private Long menuCount;
    private BigDecimal totalPrice;
    private String takeoutName;
    private String takeoutPhone;
    private BigDecimal totalAmount;
}
