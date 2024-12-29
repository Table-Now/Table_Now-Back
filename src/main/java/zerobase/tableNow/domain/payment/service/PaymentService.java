package zerobase.tableNow.domain.payment.service;

import zerobase.tableNow.domain.payment.dto.PaymentCancelRequestDto;
import zerobase.tableNow.domain.payment.dto.PaymentCancelResponseDto;
import zerobase.tableNow.domain.payment.dto.PaymentRequestDto;

public interface PaymentService {
    PaymentRequestDto verifyAndGetPayment(String impUid);


    PaymentCancelResponseDto cancelPayment(PaymentCancelRequestDto cancelRequestDto);
}
