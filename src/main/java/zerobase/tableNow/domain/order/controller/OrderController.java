package zerobase.tableNow.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zerobase.tableNow.domain.order.dto.OrderDto;
import zerobase.tableNow.domain.order.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //주문 등록
    @PostMapping("addorder")
    public ResponseEntity<String> addOrder(@RequestParam(name = "cartId") Long cartId,
                                           @RequestBody OrderDto orderDto){
        orderService.addOrder(orderDto,cartId);
        return ResponseEntity.ok("success");
    }

}
