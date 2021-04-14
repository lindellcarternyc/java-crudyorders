package com.lambdaschool.orders.repositories;

import com.lambdaschool.orders.models.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrdersRepository extends CrudRepository<Order, Long> {
    @Query(value = "SELECT* " +
            "FROM orders " +
            "WHERE advanceamount > 0 " +
            "ORDER BY advanceamount DESC",
        nativeQuery = true)
    List<Order> findOrdersByAdvanceamountAbove0();
}
