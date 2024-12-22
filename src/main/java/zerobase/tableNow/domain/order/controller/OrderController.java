package zerobase.tableNow.domain.order.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.service.OrderService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문서에 나타낼 정보
     */
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto) {
        OrderDto savedOrder = orderService.createOrder(orderDto);
        return ResponseEntity.ok(savedOrder);
    }
}
