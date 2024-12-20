package zerobase.tableNow.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.cart.entity.CartEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {

    List<CartEntity> findByUserId(UsersEntity userEntity);
}
