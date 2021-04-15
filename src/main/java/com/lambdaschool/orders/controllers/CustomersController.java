package com.lambdaschool.orders.controllers;

import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.services.CustomerService;
import com.lambdaschool.orders.views.OrderCounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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

    // DELETE http://localhost:2019/customers/customer/{custcode}
    @DeleteMapping(value = "/customer/{custcode}")
    public ResponseEntity<?> deleteCustomer(@PathVariable long custcode) {
        customerService.delete(custcode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // POST http://localhost:2019/customers/customer
    @PostMapping(value = "/customer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer newCustomer) {
        newCustomer.setCustcode(0);
        Customer savedNewCustomer = customerService.save(newCustomer);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newCustomerURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{custcode}")
                .buildAndExpand(savedNewCustomer.getCustcode())
                .toUri();
        responseHeaders.setLocation(newCustomerURI);

        return new ResponseEntity<>(savedNewCustomer, responseHeaders, HttpStatus.CREATED);
    }

    // PUT http://localhost:2019/customers/customer/{custcode}
    @PutMapping(value = "/customer/{custcode}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> replaceCustomer(@Valid @RequestBody Customer customer, @PathVariable long custcode) {
        customer.setCustcode(custcode);
        Customer updateCustomer = customerService.save(customer);
        return new ResponseEntity<>(updateCustomer, HttpStatus.OK);
    }

    // PATCH http://localhost:2019/customers/customer/{custcode}
    @PatchMapping(value = "/customer/{custcode}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @PathVariable long custcode) {
        Customer updateCustomer = customerService.update(customer, custcode);
        return new ResponseEntity<>(updateCustomer, HttpStatus.OK);
    }
}
