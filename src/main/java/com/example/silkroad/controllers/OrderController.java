package com.example.silkroad.controllers;

import com.example.silkroad.dto.ClientDTO;
import com.example.silkroad.dto.OrderDTO;
import com.example.silkroad.dto.ProductDTO;
import com.example.silkroad.models.Admin;
import com.example.silkroad.models.Client;
import com.example.silkroad.models.enums.OrderStatus;
import com.example.silkroad.repositories.OrderRepositoryImpl;
import com.example.silkroad.repositories.ProductRepositoryImpl;
import com.example.silkroad.services.OrderServiceImpl;
import com.example.silkroad.services.ProductServiceImpl;
import com.example.silkroad.services.interfaces.OrderService;
import com.example.silkroad.services.interfaces.ProductService;
import com.example.silkroad.utils.HibernateUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/order")
public class OrderController extends HttpServlet {
    private OrderService orderService;
    private ProductService productService;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        orderService = new OrderServiceImpl(new OrderRepositoryImpl(sessionFactory), new ProductRepositoryImpl(sessionFactory));

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object loggedUser = request.getSession().getAttribute("loggedUser");

        // Check if the user is logged in
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Check the user's role
        if (loggedUser instanceof Client) {
            // The logged-in user is a Client
            Client client = (Client) loggedUser;

            // Retrieve the orders for the logged-in client
            List<OrderDTO> orders;
            try {
                orders = orderService.getOrdersByClientId(client.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            request.setAttribute("orders", orders);

            // Render the order page for Client
            WebContext context = new WebContext(request, response, getServletContext());
            context.setVariable("orders", orders);
            templateEngine.process("client/orders", context, response.getWriter());
        } else if (loggedUser instanceof Admin) {
            // The logged-in user is an Admin
            List<OrderDTO> orders;
            try {
                orders = orderService.getAllOrders();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            request.setAttribute("orders", orders);

            // Render the order page for Admin
            WebContext context = new WebContext(request, response, getServletContext());
            context.setVariable("orders", orders);
            templateEngine.process("admin/orders", context, response.getWriter());
        } else {
            // Redirect to login for any other role
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Perform different actions based on the request parameter "action"
        if ("add".equalsIgnoreCase(action)) {
            handleAddOrder(request, response);
        } else if ("edit".equalsIgnoreCase(action)) {
            handleEditOrder(request, response);
        } else if ("delete".equalsIgnoreCase(action)) {
            handleDeleteOrder(request, response);
        } else {
            // Handle invalid action
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    private void handleAddOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Object loggedUser = request.getSession().getAttribute("loggedUser");

        // Check if the logged-in user is a Client
        if (!(loggedUser instanceof Client)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Client client = (Client) loggedUser;

        if (client.getId() == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Retrieve the product IDs from the request
        String productsJson = request.getParameter("products");
        List<UUID> productIdList = new ArrayList<>();

        if (productsJson != null && !productsJson.isEmpty()) {
            // Convert the JSON string to a list of IDs
            ObjectMapper objectMapper = new ObjectMapper();
            productIdList = objectMapper.readValue(productsJson, new TypeReference<List<UUID>>() {});
        }

        // Create a new order
        OrderDTO order = new OrderDTO();
        order.setClient(new ClientDTO(client.getId(), null, null, null, null, null, null, 0));
        order.setOrderDate(LocalDate.now());
        order.setStatus(String.valueOf(OrderStatus.PENDING));

        // Create a list of ProductDTOs based on the product IDs
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (UUID productId : productIdList) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(productId);
            productDTOList.add(productDTO);
        }

        // Set the products in the order
        order.setProducts(productDTOList);

        try {
            // Add the order using the OrderService
            orderService.addOrder(order);
            // Redirect to the product page after the order is successfully created
            response.sendRedirect(request.getContextPath() + "/order");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error by showing an error message on the same page
            request.setAttribute("errorMessage", "Failed to create order. Please try again.");
            request.getRequestDispatcher("/WEB-INF/templates/order_error.html").forward(request, response);
        }
    }

    private void handleEditOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Logic to edit an order based on order ID and updated status
        String orderIdParam = request.getParameter("orderId");
        String statusParam = request.getParameter("status");

        if (orderIdParam == null || statusParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID and status are required.");
            return;
        }

        UUID orderId = UUID.fromString(orderIdParam);
        try {
            // Only allow status updates when the order is still pending
            OrderDTO order = orderService.getOrderById(orderId);
            if (order.getStatus().equalsIgnoreCase(OrderStatus.PENDING.name())) {
                order.setStatus(statusParam);
                orderService.updateOrder(order);
                response.sendRedirect(request.getContextPath() + "/order");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only pending orders can be edited.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to edit the order.");
        }
    }

    private void handleDeleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Logic to delete an order based on order ID
        String orderIdParam = request.getParameter("orderId");

        if (orderIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID is required.");
            return;
        }

        UUID orderId = UUID.fromString(orderIdParam);
        try {
            // Only allow deletion when the order is still pending
            OrderDTO order = orderService.getOrderById(orderId);
            if (order.getStatus().equalsIgnoreCase(OrderStatus.PENDING.name())) {
                orderService.deleteOrder(orderId);
                response.sendRedirect(request.getContextPath() + "/order");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only pending orders can be deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete the order.");
        }
    }
}
