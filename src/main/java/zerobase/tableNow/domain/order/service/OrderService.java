package zerobase.tableNow.domain.order.service;

import zerobase.tableNow.domain.order.dto.OrderCheckDto;
import zerobase.tableNow.domain.order.dto.OrderDto;


public interface OrderService {
    OrderDto createOrder(OrderDto menuIds);

    OrderCheckDto getOrderCheck(String user);
}
