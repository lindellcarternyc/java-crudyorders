package com.lambdaschool.orders;

import com.github.javafaker.Faker;
import com.lambdaschool.orders.models.Agent;
import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.models.Payment;
import com.lambdaschool.orders.repositories.AgentsRepository;
import com.lambdaschool.orders.repositories.CustomersRepository;
import com.lambdaschool.orders.repositories.OrdersRepository;
import com.lambdaschool.orders.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Transactional
public class FakeSeedData {
    // Repositories
    private AgentsRepository agentsRepository;

    private CustomersRepository customersRepository;

    private OrdersRepository ordersRepository;

    private PaymentRepository paymentRepository;

    private static final Random random = new Random();

    private static final Faker dataFaker = new Faker();

    private List<Payment> generatePayments() {
        List<String> paymentTypes = new ArrayList<>();
        paymentTypes.add("Mock Payment 1");
        paymentTypes.add("Mock Payment 2");
        paymentTypes.add("Mock Payment 3");
        paymentTypes.add("Mock Payment 4");

        List<Payment> mockPayments = new ArrayList<>();
        paymentTypes.forEach(paymentType -> {
           Payment mockPayment = new Payment(paymentType);
           mockPayments.add(paymentRepository.save(mockPayment));
        });

        return mockPayments;
    }

    private List<Agent> generateMockAgents() {
        List<Agent> mockAgents = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String agentname = dataFaker.name()
                    .fullName();
            String country = dataFaker.address()
                    .country();
            double commission = dataFaker.number()
                    .randomDouble(2,
                            0,
                            1);
            String phone = dataFaker.phoneNumber().phoneNumber();
            String workingarea = dataFaker.address()
                    .country();

            Agent mockAgent = new Agent(agentname,
                    country,
                    commission,
                    phone,
                    workingarea);

            mockAgents.add(agentsRepository.save(mockAgent));
        }

        return mockAgents;
    }

    private List<Customer> generateMockCustomers(List<Agent> mockAgents) {
        List<Customer> mockCustomers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            String custname = dataFaker.name().fullName();
            String workingarea = dataFaker.address().city();
            String custcity = dataFaker.address().city();
            String custcountry = dataFaker.address().country();
            String phone = dataFaker.phoneNumber().phoneNumber();
            double openingamt = dataFaker.number().randomDouble(2, 0, 25000);
            double outstandingamt = dataFaker.number().randomDouble(2, 0, 20000);
            double paymentamt = dataFaker.number().randomDouble(2, 0, 20000);
            double receiveamt = dataFaker.number().randomDouble(2, 0, 20000);
            String grade = dataFaker.lorem().characters(5);

            int agentIdx = random.nextInt(mockAgents.size());
            Agent agent = mockAgents.get(agentIdx);

            Customer customer = new Customer(
                            custname,
                            workingarea,
                            custcity,
                            custcity, phone, openingamt, outstandingamt, paymentamt, receiveamt, grade, agent);

            mockCustomers.add(customer);
        }
        return  mockCustomers;
    }

    private void generateMockOrders(List<Customer> customers, List<Payment> payments) {
        for (int i = 0; i < 100; i++) {
            double orderamout = dataFaker.number().randomDouble(2, 500, 1000);
            double advanceamount = dataFaker.number().randomDouble(2, 0, 500);
            Customer customer = customers.get(random.nextInt(customers.size()));
            Payment payment = payments.get(random.nextInt(payments.size()));
            String orderdescription = dataFaker.lorem().paragraph(1);

            Order tempOrder = new Order(
                    orderamout,
                    advanceamount,
                    customer,
                    orderdescription
            );
            tempOrder.getPayments().add(payment);
            customer.getOrders().add(tempOrder);
        }
    }

    public FakeSeedData(AgentsRepository agentsRepository, CustomersRepository customersRepository, OrdersRepository ordersRepository, PaymentRepository paymentRepository) {
        this.agentsRepository = agentsRepository;
        this.customersRepository = customersRepository;
        this.ordersRepository = ordersRepository;
        this.paymentRepository = paymentRepository;
    }

    public void initData() {
        System.out.println("Fake Seed Data ... Generate fake data");

        // Payments
        List<Payment> payments = generatePayments();

        // Agents
        List<Agent> agents = generateMockAgents();

        List<Customer> customers = generateMockCustomers(agents);

//        List<Order> orders =
        generateMockOrders(customers, payments);
        customers.forEach(c -> customersRepository.save(c));
    }
}
