package zerobase.tableNow.domain.order.service;

import zerobase.tableNow.domain.order.dto.OrderDto;

public interface OrderService {
    void addOrder(OrderDto orderDto);
}
