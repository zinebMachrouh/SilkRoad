package com.example.silkroad.controllers;

import com.example.silkroad.dto.ProductDTO;
import com.example.silkroad.models.Product;
import com.example.silkroad.repositories.ProductRepositoryImpl;
import com.example.silkroad.services.ProductServiceImpl;
import com.example.silkroad.services.interfaces.ProductService;
import com.example.silkroad.utils.HibernateUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


@WebServlet("/product")
public class ProductController extends HttpServlet {

    private ProductService productService;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        productService = new ProductServiceImpl(new ProductRepositoryImpl(HibernateUtil.getSessionFactory() ),null);
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setPrefix("/WEB-INF/templates/");


        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductDTO> products = null;
        try {
            products = productService.getAllProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        WebContext context = new WebContext(request, response, getServletContext());
        context.setVariable("products", products);

        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("/products/products.html", context, response.getWriter());



    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                // Add a new product
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                String image = request.getParameter("image");

                Product newProduct = new Product(name, description, price, stock, image);
                productService.addProduct(ProductDTO.modelToDTO(newProduct));

            } else if ("update".equals(action)) {
                UUID id = UUID.fromString(request.getParameter("id"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                String image = request.getParameter("image");
                ProductDTO product = new ProductDTO(id, name, description, price, stock, image);
                productService.updateProduct(product);
            } else if ("delete".equals(action)) {
                UUID productId = UUID.fromString(request.getParameter("id"));
                productService.deleteProduct(productId);
            }

            // Redirect after all actions
            response.sendRedirect(request.getContextPath() + "/product");

        } catch (NumberFormatException e) {
            // Handle the case where parsing fails
            request.setAttribute("error", "Invalid input for price or stock");
            // Forward the request to the products page to show the error
            request.getRequestDispatcher("/WEB-INF/templates/products/products.html").forward(request, response);
        } catch (Exception e) {
            // Handle any other exceptions
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while processing the request");
            request.getRequestDispatcher("/WEB-INF/templates/products/products.html").forward(request, response);
        }
    }

}