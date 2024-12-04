package zerobase.tableNow.domain.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.wishlist.entity.WishListEntity;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
    Optional<WishListEntity> findByUserAndStore(UsersEntity user, StoreEntity store);
}
