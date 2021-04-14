package com.lambdaschool.orders.controllers;

import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrderService orderService;

    // http://localhost:2019/orders/order/{id}
    @GetMapping(value = "/order/{id}", produces = "application/json")
    public ResponseEntity<?> getOrderById(@PathVariable long id) {
        Order order = orderService.findOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // http://localhost:2019/orders/advanceamount
    @GetMapping(value = "/advanceamount", produces = "application/json")
    public ResponseEntity<?> getOrdersWithAdvanceamount() {
        List<Order> orders = orderService.findOrdersWithAdvanceAmounts();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // DELETE http://localhost:2019/orders/order/{ordernum}
    @DeleteMapping(value = "/order/{ordnum}")
    public ResponseEntity<?> deleteOrder(@PathVariable long ordnum) {
        orderService.delete(ordnum);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
