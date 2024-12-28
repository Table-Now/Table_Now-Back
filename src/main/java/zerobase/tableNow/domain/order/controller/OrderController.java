package zerobase.tableNow.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.order.dto.OrderCheckDto;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.service.OrderService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문서에 나타낼 정보를 등록
     */
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderDto orderDto) {
        orderService.createOrder(orderDto);
        return ResponseEntity.ok("성공적으로 결제등록 되었습니다.");
    }

    /**
     * 주문 확인
     * @param user 사용자 아이디
     * @return String
     */
    @GetMapping("/check/{user}")
    public ResponseEntity<OrderCheckDto> getOrderCheck(
            @PathVariable(name = "user") String user
    ) {
        OrderCheckDto orderDto = orderService.getOrderCheck(user);
        return ResponseEntity.ok(orderDto);
    }

}
