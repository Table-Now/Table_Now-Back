package zerobase.tableNow.domain.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zerobase.tableNow.domain.order.dto.OrderDto;

@RestController
public class OrderController {
    @PostMapping("addorder")
    public ResponseEntity<?> addOrder(@RequestBody OrderDto orderDto){

        return null;
    }

}
