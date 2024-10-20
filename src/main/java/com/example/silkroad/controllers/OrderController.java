package com.example.silkroad.controllers;

import com.example.silkroad.dto.ClientDTO;
import com.example.silkroad.dto.OrderDTO;
import com.example.silkroad.dto.ProductDTO;
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
import java.util.Arrays;
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

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect to the product page if accessed with a GET request
        response.sendRedirect(request.getContextPath() + "/product");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Client client = (Client) request.getSession().getAttribute("loggedUser");

        if (client == null || client.getId() == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Retrieve the product IDs from the request
        String productsJson = request.getParameter("products");
        List<UUID> productIdList = new ArrayList<>();

        if (productsJson != null && !productsJson.isEmpty()) {
            // Convert the JSON string to a list of IDs
            ObjectMapper objectMapper = new ObjectMapper();
            productIdList = objectMapper.readValue(productsJson, new TypeReference<List<UUID>>() {
            });
            System.out.println("Product IDs from cart: " + productIdList);
        } else {
            System.out.println("The cart is empty or not found.");
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
            response.sendRedirect(request.getContextPath() + "/product");
        } catch (SQLException e) {
            e.printStackTrace();

            // Handle error by showing an error message on the same page
            request.setAttribute("errorMessage", "Failed to create order. Please try again.");
            request.getRequestDispatcher("/WEB-INF/templates/order_error.html").forward(request, response);
        }
    }
}

