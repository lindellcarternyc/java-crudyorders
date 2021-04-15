package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.views.OrderCounts;

import java.util.List;

public interface CustomerService {
    Customer save(Customer customer);
    List<Customer> findAllCustomers();
    Customer findCustomerById(long id);
    List<Customer> findByCustomerNameLike(String nameLike);
    List<OrderCounts> findOrderCounts();
    void delete(long id);
    Customer update(Customer customer, long id);
}
