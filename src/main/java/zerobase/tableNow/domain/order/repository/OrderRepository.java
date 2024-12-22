package zerobase.tableNow.domain.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.order.entity.OrderEntity;
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
}
