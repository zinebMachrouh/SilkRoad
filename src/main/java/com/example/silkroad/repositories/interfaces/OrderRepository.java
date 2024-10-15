package com.example.silkroad.repositories.interfaces;

import com.example.silkroad.models.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface OrderRepository {
    Order addOrder(Order order) throws SQLException;
    Order updateOrder(Order order) throws SQLException;
    void deleteOrder(UUID id) throws SQLException;
    List<Order> getAllOrders() throws SQLException;
    Order getOrderById(UUID id) throws SQLException;
}
