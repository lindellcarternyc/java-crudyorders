package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Order;

import java.util.List;

public interface OrderService {
    Order save(Order order);
    Order findOrderById(long id);
    List<Order> findOrdersWithAdvanceAmounts();
}
