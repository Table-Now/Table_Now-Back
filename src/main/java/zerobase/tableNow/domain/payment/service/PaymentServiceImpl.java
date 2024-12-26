package zerobase.tableNow.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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
    @Override
    public PaymentRequestDto getPaymentDetails(Long paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new TableException(ErrorCode.INTERNAL_SERVER_ERROR,"결제 정보가 없습니다"));

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .impUid(payment.getImpUid())
                .amount(payment.getAmount())
                .order(payment.getOrder())
                .paidAt(payment.getPaidAt())
                .build();

        return paymentRequestDto;
    }

    @Override
    public String processPayment(PaymentRequestDto paymentRequestDto) {
        // 현재 로그인한 사용자 정보 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 조회
        UsersEntity users = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        PaymentEntity payment = PaymentEntity.builder()
                .user(users)
                .impUid(paymentRequestDto.getImpUid())
                .amount(paymentRequestDto.getAmount())
                .paidAt(LocalDateTime.now())
                .build();

        // 3. 결제와 관련된 주문 정보를 조회하여 결제 엔티티에 설정
        List<OrderEntity> orders = orderRepository.findByIdIn(List.of(paymentRequestDto.getImpUid()));
        payment.setOrder(orders);  // 결제 엔티티에 주문 정보 설정

        // 4. 결제 엔티티를 데이터베이스에 저장
        paymentRepository.save(payment);  // 결제 정보 DB 저장

        // 5. 결제 검증을 위해 Portone API에 요청
        Payment verificationResult = verifyPaymentWithPortone(paymentRequestDto.getImpUid());

        // 결제 상태가 성공적인 경우 주문 상태 업데이트
//        if ("paid".equals(verificationResult.getStatus())) {
//            // 주문 상태를 '결제 완료'로 업데이트
//            for (OrderEntity order : orders) {
//                order.setPaymentStatus(true);  // 결제 완료 상태로 변경
//                orderRepository.save(order);   // 상태 업데이트
//            }
//        }


        // 결제 처리 성공 메시지 반환
        return "결제 성공 : " + verificationResult.getStatus();
    }

    // Portone API를 호출하여 결제 상태를 검증하는 메서드
    private Payment verifyPaymentWithPortone(String impUid) {

        try {
            // Portone API에 impUid를 이용해 결제 정보를 조회
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);

            // 응답이 없다면 예외 처리
            if (response.getResponse() == null) {
                throw new RuntimeException("Payment verification failed");  // 결제 검증 실패
            }

            // 결제 상태를 반환
            return response.getResponse();
        } catch (IamportResponseException e) {
            // API 호출 또는 클라이언트 예외 발생 시 예외 처리
            throw new RuntimeException("Error verifying payment with Portone", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
