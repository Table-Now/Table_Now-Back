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
        @JoinColumn(name = "user")
        private UsersEntity userId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "menu_id")
        private MenuEntity menuId;


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "store_id")
        private StoreEntity store;

        private int count; //개수
}
