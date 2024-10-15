package com.example.silkroad.dto;

import com.example.silkroad.models.Order;
import com.example.silkroad.models.enums.OrderStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrderDTO {
    private UUID id;
    private LocalDate orderDate;
    private String status;
    private ClientDTO client;
    private List<ProductDTO> products = new ArrayList<>();

    public OrderDTO() {
    }

    public OrderDTO(UUID id, LocalDate orderDate, String status, ClientDTO client) {
        this.id = id;
        this.orderDate = orderDate;
        this.status = status;
        this.client = client;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", client=" + client +
                ", products=" + products +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id == orderDTO.id && Objects.equals(orderDate, orderDTO.orderDate) && Objects.equals(status, orderDTO.status) && Objects.equals(client, orderDTO.client) && Objects.equals(products, orderDTO.products);
    }

    public Order dtoToModel() {
        Order order = new Order();
        order.setId(this.id);
        order.setOrderDate(this.orderDate);
        order.setStatus(OrderStatus.valueOf(this.status));
        order.setClient(this.client.dtoToModel());
        this.products.forEach(productDTO -> order.getProducts().add(productDTO.dtoToModel()));
        return order;
    }

    public static OrderDTO modelToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO(order.getId(), order.getOrderDate(), order.getStatus().name(), ClientDTO.modelToDTO(order.getClient()));
        order.getProducts().forEach(product -> orderDTO.getProducts().add(ProductDTO.modelToDTO(product)));
        return orderDTO;
    }
}
