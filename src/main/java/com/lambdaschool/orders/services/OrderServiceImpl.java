package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.models.Payment;
import com.lambdaschool.orders.repositories.CustomersRepository;
import com.lambdaschool.orders.repositories.OrdersRepository;
import com.lambdaschool.orders.repositories.PaymentRepository;
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

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    @Override
    public Order save(Order order) {
        Order newOrder = new Order();

        if (order.getOrdnum() != 0) {
            findOrderById(order.getOrdnum());
            newOrder.setOrdnum(order.getOrdnum());
        }

        newOrder.setAdvanceamount(newOrder.getAdvanceamount());
        newOrder.setOrdamount(newOrder.getOrdamount());
        newOrder.setOrderdescription(order.getOrderdescription());

        Customer orderCustomer = customersRepository.findById(order.getCustomer().getCustcode())
                .orElseThrow(() -> new EntityNotFoundException("Cannot create order ... Customer not found"));
        orderCustomer.getOrders().add(newOrder);
        newOrder.setCustomer(orderCustomer);

        newOrder.getPayments().clear();
        for (Payment p : order.getPayments()) {
            Payment orderPayment = paymentRepository.findById(p.getPaymentid())
                    .orElseThrow(() -> new EntityNotFoundException("Cannot create order ... Payment not found"));
            newOrder.getPayments().add(orderPayment);
        }

        return orderRepository.save(newOrder);
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
