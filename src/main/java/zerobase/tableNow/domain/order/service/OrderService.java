package zerobase.tableNow.domain.order.service;

import zerobase.tableNow.domain.order.dto.OrderCheckDto;
import zerobase.tableNow.domain.order.dto.OrderDto;

import java.util.List;


public interface OrderService {
    OrderDto createOrder(OrderDto menuIds);


    List<OrderCheckDto> getOrderCheck(String user);
}
