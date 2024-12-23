package zerobase.tableNow.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.User;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.entity.OrderEntity;
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
public class PaymentEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private UsersEntity user;              // 결제 사용자

        private String impUid;          // 포트원 결제 고유 ID
        private BigDecimal amount;  // 결제 금액

        @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
        private List<OrderEntity> order;  // 결제 상품 목록

        private LocalDateTime paidAt;    // 결제 시간
}
