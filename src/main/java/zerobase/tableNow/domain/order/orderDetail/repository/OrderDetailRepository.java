package zerobase.tableNow.domain.order.orderDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.tableNow.domain.order.orderDetail.entity.OrderDetailEntity;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
}
