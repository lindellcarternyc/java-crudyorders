package com.lambdaschool.orders.controllers;

import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.services.CustomerService;
import com.lambdaschool.orders.views.OrderCounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomersController {
    @Autowired
    private CustomerService customerService;

    // http://localhost:2019/customers/orders
    @GetMapping(value = "/orders", produces = "application/json")
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = customerService.findAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // http://localhost:2019/customers/customer/{id}
    @GetMapping(value = "/customer/{id}", produces = "application/json")
    public ResponseEntity<?> getCustomerWithId(@PathVariable Long id) {
        Customer customer = customerService.findCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    // http://localhost:2019/customers/namelike/{likename}
    @GetMapping(value = "/customer/namelike/{likename}", produces = "application/json")
    public ResponseEntity<?> getCustomerWithNameLike(@PathVariable String likename) {
        List<Customer> customers = customerService.findByCustomerNameLike(likename);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // http://localhost:2019/customers/orders/count
    @GetMapping(value = "/orders/count", produces = "application/json")
    public ResponseEntity<?> getOrderCounts() {
        List<OrderCounts> orderCounts = customerService.findOrderCounts();
        return new ResponseEntity<>(orderCounts, HttpStatus.OK);
    }
}
