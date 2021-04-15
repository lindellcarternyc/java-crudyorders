package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Agent;
import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.models.Payment;
import com.lambdaschool.orders.repositories.AgentsRepository;
import com.lambdaschool.orders.repositories.CustomersRepository;
import com.lambdaschool.orders.repositories.PaymentRepository;
import com.lambdaschool.orders.views.OrderCounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomersRepository customerRepository;

    @Autowired
    private AgentsRepository agentsRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    @Override
    public Customer save(Customer customer) {
        Customer newCustomer = new Customer();

        if (customer.getCustcode() != 0) {
            findCustomerById(customer.getCustcode());
            newCustomer.setCustcode(customer.getCustcode());
        }

        newCustomer.setCustcity(customer.getCustcity());
        newCustomer.setCustcountry(customer.getCustcountry());
        newCustomer.setCustname(customer.getCustname());
        newCustomer.setGrade(customer.getGrade());
        newCustomer.setOpeningamt(customer.getOpeningamt());
        newCustomer.setOutstandingamt(customer.getOutstandingamt());
        newCustomer.setPaymentamt(customer.getPaymentamt());
        newCustomer.setPhone(customer.getPhone());
        newCustomer.setReceiveamt(customer.getReceiveamt());
        newCustomer.setWorkingarea(customer.getWorkingarea());

        // Agent
        Agent customerAgent = agentsRepository.findById(customer.getAgent().getAgentcode())
                .orElseThrow(() -> new EntityNotFoundException("No agent with ID: " + customer.getAgent().getAgentcode() + "FOUND"));
        customerAgent.getCustomers().add(newCustomer);
        newCustomer.setAgent(customerAgent);

        // Orders
        newCustomer.getOrders().clear();
        for (Order o : customer.getOrders()) {
            Order newOrder = new Order();
            newOrder.setAdvanceamount(o.getAdvanceamount());
            newOrder.setOrdamount(o.getOrdamount());
            newOrder.setOrderdescription(o.getOrderdescription());

            newOrder.getPayments().clear();
            for (Payment p : o.getPayments()) {
                Payment newOrderPayment = paymentRepository.findById(p.getPaymentid())
                        .orElseThrow(() -> new EntityNotFoundException("Cannot create Customer. Cannot Create order ... Payment ... NOT FOUND"));
                newOrder.getPayments().add(newOrderPayment);
            }
            newOrder.setCustomer(newCustomer);
            newCustomer.getOrders().add(newOrder);
        }

        return customerRepository.save(newCustomer);
    }

    @Override
    public List<Customer> findAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().iterator().forEachRemaining(customers::add);
        return customers;
    }

    @Override
    public Customer findCustomerById(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id '" + id + "' NOT FOUND"));
    }

    @Override
    public List<Customer> findByCustomerNameLike(String namelike) {
        return customerRepository.findByCustnameContainingIgnoringCase(namelike);
    }

    @Override
    public List<OrderCounts> findOrderCounts() {
        return customerRepository.findOrderCounts();
    }

    @Override
    public void delete(long id) {
        if (customerRepository.findById(id).isPresent()) {
            customerRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Customer with id '" + id + "' NOT FOUND");
        }
    }

    @Override
    public Customer update(Customer customer, long id) {
        Customer updateCustomer = findCustomerById(id);

        if (customer.getCustcity() != null) {
            updateCustomer.setCustcity(customer.getCustcity());
        }
        if (customer.getCustcountry() != null) {
            updateCustomer.setCustcountry(customer.getCustcountry());
        }
        if (customer.getCustname() != null) {
            updateCustomer.setCustname(customer.getCustname());
        }
        if (customer.getGrade() != null) {
            updateCustomer.setGrade(customer.getGrade());
        }
        if (customer.getPhone() != null) {
            updateCustomer.setPhone(customer.getPhone());
        }
        if (customer.getWorkingarea() != null) {
            updateCustomer.setWorkingarea(customer.getWorkingarea());
        }

        if (customer.hasValueForOutstandingamt) {
            updateCustomer.setOutstandingamt(customer.getOutstandingamt());
        }
        if (customer.hasValueForOpeningamt) {
            updateCustomer.setOpeningamt(customer.getOpeningamt());
        }
        if (customer.hasValueForPaymentamt) {
            updateCustomer.setPaymentamt(customer.getPaymentamt());
        }
        if (customer.hasValueForReceiveamt) {
            updateCustomer.setReceiveamt(customer.getReceiveamt());
        }

        if (customer.getAgent() != null) {
            Agent customerAgent = agentsRepository.findById(customer.getAgent().getAgentcode())
                    .orElseThrow(() -> new EntityNotFoundException("No agent with ID: " + customer.getAgent().getAgentcode() + "FOUND"));
            customerAgent.getCustomers().add(updateCustomer);
            updateCustomer.setAgent(customerAgent);
        }

        if (customer.getOrders().size() != 0) {
            updateCustomer.getOrders().clear();
            for (Order o : customer.getOrders()) {
                Order newOrder = new Order();
                newOrder.setAdvanceamount(o.getAdvanceamount());
                newOrder.setOrdamount(o.getOrdamount());
                newOrder.setOrderdescription(o.getOrderdescription());

                newOrder.getPayments().clear();
                for (Payment p : o.getPayments()) {
                    Payment newOrderPayment = paymentRepository.findById(p.getPaymentid())
                            .orElseThrow(() -> new EntityNotFoundException("Cannot create Customer. Cannot Create order ... Payment ... NOT FOUND"));
                    newOrder.getPayments().add(newOrderPayment);
                }
                newOrder.setCustomer(updateCustomer);
                updateCustomer.getOrders().add(newOrder);
            }
        }
        return updateCustomer;
    }
}
