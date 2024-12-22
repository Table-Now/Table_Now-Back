package zerobase.tableNow.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //주문 등록
    @PostMapping("addorder")
    public ResponseEntity<String> addOrder(@RequestBody OrderDto orderDto){
        orderService.addOrder(orderDto);
        return ResponseEntity.ok("success");
    }

}
