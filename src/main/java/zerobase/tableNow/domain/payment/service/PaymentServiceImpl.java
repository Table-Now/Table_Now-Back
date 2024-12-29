package zerobase.tableNow.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.constant.PaymentStatus;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.order.entity.OrderEntity;
import zerobase.tableNow.domain.order.repository.OrderRepository;
import zerobase.tableNow.domain.payment.dto.PaymentRequestDto;
import zerobase.tableNow.domain.payment.entity.PaymentEntity;
import zerobase.tableNow.domain.payment.repository.PaymentRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final IamportClient iamportClient;

    //결제 상세 정보를 조회
    @Override
    @Transactional
    public PaymentRequestDto getPaymentDetails(Long paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new TableException(ErrorCode.INTERNAL_SERVER_ERROR, "결제 정보가 없습니다"));

        return PaymentRequestDto.builder()
                .impUid(payment.getImpUid())
                .totalAmount(payment.getTotalAmount())
                .status(payment.getStatus())
                .build();
    }

    //결제를 처리하는 메서드.
    @Override
    public String processPayment(PaymentRequestDto paymentRequestDto) {
        // 현재 로그인한 사용자 정보 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 조회
        UsersEntity user = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // 주문 정보 조회
        OrderEntity order = orderRepository.findByUuid(paymentRequestDto.getImpUid())
                .orElseThrow(() -> new TableException(ErrorCode.ORDER_NOT_FOUND));

        // Portone API를 통해 결제 검증
        Payment verifiedPayment = verifyPaymentWithPortone(paymentRequestDto.getImpUid());

        // 결제 정보 생성 및 저장
        PaymentEntity payment = PaymentEntity.builder()
                .impUid(verifiedPayment.getImpUid())
                .totalAmount(verifiedPayment.getAmount())
                .status(PaymentStatus.OK)
                .build();
        paymentRepository.save(payment);

        // 결제 성공 메시지 반환
        return "결제 성공 : " + verifiedPayment.getStatus();
    }

    // Portone API를 호출하여 결제 상태를 검증하는 메서드
    private Payment verifyPaymentWithPortone(String impUid) {

        try {
            // Portone API에 impUid를 이용해 결제 정보를 조회
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);

            if (response.getResponse() == null) {
                throw new TableException(ErrorCode.INTERNAL_SERVER_ERROR, "결제 검증 실패");
            }

            // 결제 상태를 반환
            return response.getResponse();
        } catch (Exception e) {
            throw new TableException(ErrorCode.INTERNAL_SERVER_ERROR, "Portone 결제 검증 중 오류 발생");
        }
    }
}
