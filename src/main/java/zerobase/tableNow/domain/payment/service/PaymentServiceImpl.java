package zerobase.tableNow.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.tableNow.domain.constant.PaymentStatus;
import zerobase.tableNow.domain.payment.dto.PaymentCancelRequestDto;
import zerobase.tableNow.domain.payment.dto.PaymentCancelResponseDto;
import zerobase.tableNow.domain.payment.dto.PaymentRequestDto;
import zerobase.tableNow.domain.payment.entity.PaymentEntity;
import zerobase.tableNow.domain.payment.repository.PaymentRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    /**
     * 포트원 결제성공 조회
     * @param impUid (포트원 고유식별자)
     * @return 결제사용자 정보
     */
    @Override
    @Transactional
    public PaymentRequestDto verifyAndGetPayment(String impUid) {
        // 포트원에서 결제 정보 조회
        Payment portonePayment = verifyPaymentWithPortone(impUid);

        // 결제 상태 확인
        if (!"paid".equals(portonePayment.getStatus())) {
            throw new TableException(ErrorCode.USER_NOT_FOUND, "결제가 완료되지 않았습니다.");
        }

        // 결제 정보 저장 또는 업데이트
        PaymentEntity payment = paymentRepository.findByImpUid(impUid)
                .orElse(new PaymentEntity());

        updatePaymentEntity(payment, portonePayment);
        PaymentEntity savedPayment = paymentRepository.save(payment);

        return PaymentRequestDto.fromEntity(savedPayment);
    }

    private Payment verifyPaymentWithPortone(String impUid) {
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);

            if (response.getResponse() == null) {
                throw new TableException(ErrorCode.USER_NOT_FOUND, "결제 검증 실패");
            }

            return response.getResponse();
        } catch (IamportResponseException | IOException e) {
            throw new TableException(ErrorCode.USER_NOT_FOUND, "포트원 결제 검증 중 오류 발생: " + e.getMessage());
        }
    }
    private void updatePaymentEntity(PaymentEntity entity, Payment portonePayment) {
        entity.setImpUid(portonePayment.getImpUid());
        entity.setMerchantUid(portonePayment.getMerchantUid());
        entity.setStatus(PaymentStatus.OK);
        entity.setTotalAmount(new BigDecimal(portonePayment.getAmount().toString()));
        entity.setPayMethod(portonePayment.getPayMethod());
        entity.setCardName(portonePayment.getCardName());
        entity.setCardNumber(portonePayment.getCardNumber());
        entity.setCardQuota(portonePayment.getCardQuota());
        entity.setBuyerName(portonePayment.getBuyerName());
        entity.setBuyerEmail(portonePayment.getBuyerEmail());
        entity.setBuyerTel(portonePayment.getBuyerTel());
    }

    /**
     * 포트원 결제취소
     * @param cancelRequestDto 취소 요청 DTO (포트원 고유 식별자)
     * @return 취소 결과 DTO
     */
    @Override
    @Transactional
    public PaymentCancelResponseDto cancelPayment(PaymentCancelRequestDto cancelRequestDto) {
        try {
            // 결제 정보 조회
            PaymentEntity payment = paymentRepository.findByImpUid(cancelRequestDto.getImpUid())
                    .orElseThrow(() -> new TableException(ErrorCode.PAYMENT_NOT_FOUND));

            // 이미 취소된 결제인지 확인
            if (payment.getStatus() == PaymentStatus.CANCEL) {
                throw new TableException(ErrorCode.INVALID_REQUEST);
            }

            // 결제 취소 요청
            CancelData cancelData = new CancelData(cancelRequestDto.getImpUid(), true);
            IamportResponse<Payment> response = iamportClient.cancelPaymentByImpUid(cancelData);

            if (response.getResponse() == null) {
                throw new TableException(ErrorCode.PAYMENT_CANCELLATION_FAILED);
            }

            // DB 결제 상태 업데이트
            payment.setStatus(PaymentStatus.CANCEL);
            paymentRepository.save(payment);

            return PaymentCancelResponseDto.builder()
                    .impUid(payment.getImpUid())
                    .status(payment.getStatus())
                    .message("결제가 성공적으로 취소되었습니다.")
                    .build();

        } catch (IamportResponseException | IOException e) {
            throw new TableException(ErrorCode.PAYMENT_CANCELLATION_FAILED,
                    "포트원 결제 취소 중 오류 발생: " + e.getMessage());
        }
    }

}