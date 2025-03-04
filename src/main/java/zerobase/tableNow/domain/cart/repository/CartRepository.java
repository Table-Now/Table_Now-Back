package zerobase.tableNow.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.cart.entity.CartEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long>{
    List<CartEntity> findByUserId_User(String userId);
    Optional<CartEntity> findByUser_UserAndMenu_Id(String userId, Long menuId);
    List<CartEntity> findByUserAndStoreNot(UsersEntity user, StoreEntity store);

}
