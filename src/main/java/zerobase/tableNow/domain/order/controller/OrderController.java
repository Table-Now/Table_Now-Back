package zerobase.tableNow.domain.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.service.OrderService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문서에 나타낼 정보
     */
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody @Valid OrderDto orderDto
    ) throws JsonProcessingException {
        log.info("Raw request body: {}", new ObjectMapper().writeValueAsString(orderDto));

        OrderDto savedOrder = orderService.createOrder(orderDto);
        return ResponseEntity.ok(savedOrder);
    }
}
