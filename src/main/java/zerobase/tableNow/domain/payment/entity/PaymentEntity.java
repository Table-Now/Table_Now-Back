package zerobase.tableNow.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.User;
import zerobase.tableNow.domain.baseEntity.BaseEntity;
import zerobase.tableNow.domain.constant.PaymentStatus;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.order.repository.OrderRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "payment")
public class PaymentEntity extends BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String impUid;          // 포트원 결제 고유 ID
        private PaymentStatus status;  //결제 상태
        private BigDecimal totalAmount;  // 결제 금액
}
