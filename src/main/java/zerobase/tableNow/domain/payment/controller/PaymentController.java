package zerobase.tableNow.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.payment.dto.PaymentCancelRequestDto;
import zerobase.tableNow.domain.payment.dto.PaymentCancelResponseDto;
import zerobase.tableNow.domain.payment.dto.PaymentRequestDto;
import zerobase.tableNow.domain.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api")
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * 포트원 결제 검증 및 결제 정보 조회
     *
     * @param impUid 포트원 결제 고유 ID
     * @return 검증된 결제 정보
     */
    @GetMapping("/payment/verify/{impUid}")
    public ResponseEntity<PaymentRequestDto> verifyPayment(
            @PathVariable(name = "impUid") String impUid) {
        PaymentRequestDto payment = paymentService.verifyAndGetPayment(impUid);
        return ResponseEntity.ok(payment);
    }

    /**
     * 결제 취소 요청 API
     *
     * @param cancelRequestDto 결제 취소 요청 정보 (impUid)
     * @return 결제 취소 결과
     */
    @PostMapping("/payment/cancel")
    public ResponseEntity<PaymentCancelResponseDto> cancelPayment(
            @RequestBody PaymentCancelRequestDto cancelRequestDto) {
        PaymentCancelResponseDto result = paymentService.cancelPayment(cancelRequestDto);
        return ResponseEntity.ok(result);
    }
}
