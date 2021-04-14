package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Transactional
@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrdersRepository orderRepository;

    @Transactional
    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order findOrderById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id '" + id + "' NOT FOUND"));
    }

    @Override
    public List<Order> findOrdersWithAdvanceAmounts() {
        return orderRepository.findOrdersByAdvanceamountAbove0();
    }

    @Override
    public void delete(long id) {
        if (orderRepository.findById(id).isPresent()) {
            orderRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Order with id '" + id + "' NOT FOUND");
        }
    }
}
