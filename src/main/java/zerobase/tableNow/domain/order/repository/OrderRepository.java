package zerobase.tableNow.domain.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    boolean existsByUserIdAndStatus(UsersEntity userEntity, Status status);
}
