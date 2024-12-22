package zerobase.tableNow.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zerobase.tableNow.domain.payment.dto.PaymentDto;
import zerobase.tableNow.domain.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    //주문 등록
//    @PostMapping("addorder")
//    public ResponseEntity<String> addOrder(@RequestParam(name = "cartId") Long cartId,
//                                           @RequestBody PaymentDto paymentDto){
//        paymentService.addOrder(paymentDto,cartId);
//        return ResponseEntity.ok("success");
//    }
//

}
