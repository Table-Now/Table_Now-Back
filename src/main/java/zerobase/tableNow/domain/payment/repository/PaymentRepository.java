package zerobase.tableNow.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.payment.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {
}
