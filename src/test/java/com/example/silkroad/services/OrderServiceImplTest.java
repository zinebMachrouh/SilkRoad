package com.example.silkroad.services;

import com.example.silkroad.dto.ClientDTO;
import com.example.silkroad.dto.OrderDTO;
import com.example.silkroad.dto.ProductDTO;
import com.example.silkroad.models.Order;
import com.example.silkroad.models.Product;
import com.example.silkroad.repositories.interfaces.OrderRepository;
import com.example.silkroad.repositories.interfaces.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.example.silkroad.models.enums.OrderStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrder() throws SQLException {
        // Create a sample OrderDTO
        UUID clientId = UUID.randomUUID();
        ClientDTO clientDTO = new ClientDTO(clientId, "John Doe", "john@example.com", "password", "salt", "123 Street", null, 0);
        ProductDTO productDTO = new ProductDTO(UUID.randomUUID(), "Product 1", "Description", 19.99, 10, "image_url");
        OrderDTO orderDTO = new OrderDTO(null, LocalDate.now(), "PENDING", clientDTO);
        orderDTO.setProducts(Arrays.asList(productDTO));

        // Mocking product repository to return the product
        Product product = productDTO.dtoToModel();
        when(productRepository.getProductById(productDTO.getId())).thenReturn(product);
        when(orderRepository.addOrder(any(Order.class))).thenReturn(orderDTO.dtoToModel());

        // Call addOrder
        Order result = orderService.addOrder(orderDTO);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(1, result.getProducts().size());
        verify(orderRepository, times(1)).addOrder(any(Order.class));
    }

    @Test
    void testUpdateOrder() throws SQLException {
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        ClientDTO clientDTO = new ClientDTO(clientId, "Jane Doe", "jane@example.com", "password", "salt", "456 Avenue", null, 0);
        ProductDTO productDTO = new ProductDTO(UUID.randomUUID(), "Product 2", "Description", 29.99, 5, "image_url");
        OrderDTO orderDTO = new OrderDTO(orderId, LocalDate.now(), "PENDING", clientDTO);
        orderDTO.setProducts(Arrays.asList(productDTO));

        Order existingOrder = orderDTO.dtoToModel();
        when(orderRepository.getOrderById(orderId)).thenReturn(existingOrder);
        when(orderRepository.updateOrder(any(Order.class))).thenReturn(existingOrder);

        Order result = orderService.updateOrder(orderDTO);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository, times(1)).updateOrder(any(Order.class));
    }

    @Test
    void testDeleteOrder() throws SQLException {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, LocalDate.now(), PENDING, null);

        when(orderRepository.getOrderById(orderId)).thenReturn(order);

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteOrder(orderId);
    }

    @Test
    void testGetAllOrders() throws SQLException {
        Order order1 = new Order(UUID.randomUUID(), LocalDate.now(), PENDING, null);
        Order order2 = new Order(UUID.randomUUID(), LocalDate.now(), SHIPPED, null);
        List<Order> orderList = Arrays.asList(order1, order2);

        when(orderRepository.getAllOrders()).thenReturn(orderList);

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById() throws SQLException {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, LocalDate.now(), PENDING, null);

        when(orderRepository.getOrderById(orderId)).thenReturn(order);

        OrderDTO result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository, times(1)).getOrderById(orderId);
    }

    @Test
    void testGetOrdersByClientId() throws SQLException {
        UUID clientId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), LocalDate.now(), PENDING, null);
        List<Order> orders = Arrays.asList(order);

        when(orderRepository.getOrdersByClientId(clientId)).thenReturn(orders);

        List<OrderDTO> result = orderService.getOrdersByClientId(clientId);

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).getOrdersByClientId(clientId);
    }
}
