package zerobase.tableNow.domain.payment.service;

import zerobase.tableNow.domain.payment.dto.PaymentRequestDto;

public interface PaymentService {
    PaymentRequestDto getPaymentDetails(Long paymentId);

    String processPayment(PaymentRequestDto paymentRequestDto);
}
