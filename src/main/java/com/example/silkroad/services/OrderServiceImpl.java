package com.example.silkroad.services;

import com.example.silkroad.dto.OrderDTO;
import com.example.silkroad.dto.ProductDTO;
import com.example.silkroad.models.Order;
import com.example.silkroad.models.Product;
import com.example.silkroad.repositories.interfaces.OrderRepository;
import com.example.silkroad.repositories.interfaces.ProductRepository;
import com.example.silkroad.services.interfaces.OrderService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order addOrder(OrderDTO orderDTO) throws SQLException {
        Order order = orderDTO.dtoToModel();
        if (productRepository == null) {
            System.out.println("ProductRepository is not initialized.");
            throw new IllegalStateException("ProductRepository is not initialized.");
        }

        if (orderDTO.getProducts() == null || orderDTO.getProducts().isEmpty()) {
            System.out.println("No products provided in the order.");
            throw new IllegalArgumentException("The order must contain at least one product.");
        }

        // Attempt to retrieve each product and add it to the order
        for (ProductDTO productDTO : orderDTO.getProducts()) {
            Product product = productRepository.getProductById(productDTO.getId());

            if (product != null) {
                order.getProducts().add(product);
                System.out.println("Product added to the order: " + product.getName() + " (ID: " + product.getId() + ")");
            } else {
                System.out.println("Product not found: ID " + productDTO.getId());
            }
        }

        // Check if any products were successfully added
        if (order.getProducts().isEmpty()) {
            System.out.println("No valid products were added to the order.");
            throw new IllegalArgumentException("The order does not contain any valid products.");
        }

        // Save the order to the database
        Order savedOrder = orderRepository.addOrder(order);
        System.out.println("Order successfully created with ID: " + savedOrder.getId());

        return savedOrder;
    }



    @Override
    public Order updateOrder(OrderDTO orderDTO) throws SQLException {
        if (orderRepository.getOrderById(orderDTO.getId()) == null) {
            return null;
        }
        Order order = orderDTO.dtoToModel();
        order.getProducts().clear();
        for (ProductDTO productDTO : orderDTO.getProducts()) {
            Product product = productRepository.getProductById(productDTO.getId());
            if (product != null) {
                order.getProducts().add(product);
            }
        }
        return orderRepository.updateOrder(order);
    }

    @Override
    public void deleteOrder(UUID id) throws SQLException {
        Order order = orderRepository.getOrderById(id);
        if (order != null) {
            orderRepository.deleteOrder(id);
        }
    }

    @Override
    public List<OrderDTO> getAllOrders() throws SQLException {
        List<Order> allOrders = orderRepository.getAllOrders();
        return allOrders.stream().map(OrderDTO::modelToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(UUID id) throws SQLException {
        Order order = orderRepository.getOrderById(id);
        return order != null ? OrderDTO.modelToDTO(order) : null;
    }
}
