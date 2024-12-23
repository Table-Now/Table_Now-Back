package zerobase.tableNow.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.payment.dto.PaymentRequestDto;
import zerobase.tableNow.domain.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api")
public class PaymentController {
    private final PaymentService paymentService;
    /**
     * 결제 상세 정보를 조회하는 메서드입니다.
     *
     * @param paymentId 결제 ID (결제 상세 정보 조회에 필요한 식별자)
     * @return 결제 ID에 해당하는 결제 정보를 반환합니다.
     */
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<PaymentRequestDto> paymentPage(@PathVariable(name = "paymentId",required = false)Long paymentId){
        PaymentRequestDto payment = paymentService.getPaymentDetails(paymentId);
        return ResponseEntity.ok(payment);
    }

    /**
     * 결제를 처리하는 메서드.
     * @param paymentRequestDto 결제 요청 정보 (결제 정보를 포함하는 DTO)
     * @return 결제 처리 결과를 반환.
     */
    @GetMapping("/payment")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequestDto paymentRequestDto){
        String paymentResult = paymentService.processPayment(paymentRequestDto);
        return ResponseEntity.ok(paymentResult);
    }

}
