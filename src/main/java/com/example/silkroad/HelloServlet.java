package com.example.silkroad;

import com.example.silkroad.dto.ProductDTO;
import com.example.silkroad.models.Product;
import com.example.silkroad.repositories.ProductRepositoryImpl;
import com.example.silkroad.services.ProductServiceImpl;
import com.example.silkroad.services.interfaces.ProductService;
import com.example.silkroad.utils.HibernateUtil;
import org.hibernate.SessionFactory;

import java.io.*;
import java.sql.SQLException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "HelloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    private ProductService productService;

    public void init() {
        message = "Hello World!";
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        productService = new ProductServiceImpl(new ProductRepositoryImpl(new HibernateUtil().getSessionFactory()), null);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        ProductDTO product = new ProductDTO();
        try {
            productService.addProduct(product);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while adding the product.");
            return;
        }

        // If successful, send a response
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}