//package zerobase.tableNow.domain.order.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import zerobase.tableNow.domain.baseEntity.BaseEntity;
//import zerobase.tableNow.domain.constant.Status;
//import zerobase.tableNow.domain.store.controller.menu.entity.MenuEntity;
//import zerobase.tableNow.domain.store.entity.StoreEntity;
//import zerobase.tableNow.domain.user.entity.UsersEntity;
//
//@Entity
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class OrderEntity extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user")
//    private UsersEntity userId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "menu")
//    private MenuEntity menuId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store")
//    private StoreEntity storeId;
//
//    private int Tocount; //개수
//
//    private Status status; //매진여부
//
//}
