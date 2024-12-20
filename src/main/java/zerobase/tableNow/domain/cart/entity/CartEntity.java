package zerobase.tableNow.domain.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.store.controller.menu.entity.MenuEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class CartEntity extends BaseEntity{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        private UsersEntity userId;

        @ManyToOne(fetch = FetchType.LAZY)
        private MenuEntity menuId;

        @ManyToOne(fetch = FetchType.LAZY)
        private StoreEntity store;

        private int totalCount; //개수

        private int totalAmount; // 총액

}
