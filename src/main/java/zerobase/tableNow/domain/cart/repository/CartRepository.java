package zerobase.tableNow.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.cart.entity.CartEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {
    Optional<CartEntity> findByUserIdIdAndMenuIdId(Long userId, Long menuId);

    CartEntity findByIdAndUserId(Long cartId, UsersEntity userEntity);
}
