package me.jimmy.orderservice.service;

import me.jimmy.orderservice.dto.OrderDto;
import me.jimmy.orderservice.entity.OrderEntity;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrderByUserId(String userId);
}
