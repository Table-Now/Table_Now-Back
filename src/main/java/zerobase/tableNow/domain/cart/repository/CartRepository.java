package zerobase.tableNow.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.cart.entity.CartEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {
}
