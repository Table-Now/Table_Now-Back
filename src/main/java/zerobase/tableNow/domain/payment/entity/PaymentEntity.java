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
        private String merchantUid;      // 주문 번호
        @Enumerated(EnumType.STRING)
        private PaymentStatus status = PaymentStatus.READY; // 결제 상태 초기값 설정
        private BigDecimal totalAmount;  // 결제 금액
        private String payMethod;        // 결제 방식
        private String cardName;         // 카드사 이름
        private String cardNumber;       // 마스킹된 카드번호
        private Integer cardQuota;       // 할부 개월 수
        private String buyerName;        // 구매자 이름
        private String buyerEmail;       // 구매자 이메일
        private String buyerTel;         // 구매자 연락처
}
