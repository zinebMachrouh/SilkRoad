package com.example.silkroad.services.interfaces;

import com.example.silkroad.dto.OrderDTO;
import com.example.silkroad.models.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    public Order addOrder(OrderDTO order) throws SQLException;
    public Order updateOrder(OrderDTO order) throws SQLException;
    void deleteOrder(UUID id) throws SQLException;
    List<OrderDTO> getAllOrders() throws SQLException;
    OrderDTO getOrderById(UUID id) throws SQLException;

    List<OrderDTO> getOrdersByClientId(UUID clientId) throws SQLException;
}
