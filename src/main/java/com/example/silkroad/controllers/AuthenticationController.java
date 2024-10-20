package com.example.silkroad.controllers;

import com.example.silkroad.dto.ClientDTO;
import com.example.silkroad.models.Admin;
import com.example.silkroad.models.Client;
import com.example.silkroad.models.User;
import com.example.silkroad.models.enums.PaymentMethod;
import com.example.silkroad.repositories.AdminRepositoryImpl;
import com.example.silkroad.repositories.ClientRepositoryImpl;
import com.example.silkroad.repositories.UserRepositoryImpl;
import com.example.silkroad.repositories.interfaces.AdminRepository;
import com.example.silkroad.repositories.interfaces.ClientRepository;
import com.example.silkroad.repositories.interfaces.UserRepository;
import com.example.silkroad.services.ClientServiceImpl;
import com.example.silkroad.services.UserServiceImpl;
import com.example.silkroad.services.interfaces.ClientService;
import com.example.silkroad.services.interfaces.UserService;
import com.example.silkroad.utils.HibernateUtil;
import com.example.silkroad.utils.PasswordUtil;
import com.example.silkroad.utils.ThymeLeafUtil;
import org.hibernate.SessionFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Logger;

public class AuthenticationController extends HttpServlet {
    private Logger logger = Logger.getLogger(AuthenticationController.class.getName());
    private UserService userService;
    private TemplateEngine templateEngine;
    private ClientService clientService;

    @Override
    public void init() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        UserRepository userRepository = new UserRepositoryImpl(sessionFactory);
        AdminRepository adminRepository = new AdminRepositoryImpl(sessionFactory);
        ClientRepository clientRepository = new ClientRepositoryImpl(sessionFactory);

        this.userService = new UserServiceImpl(userRepository, clientRepository, adminRepository);
        this.clientService = new ClientServiceImpl(clientRepository);

        /*byte[] salt = PasswordUtil.generateSalt();

        String hashedPassword = PasswordUtil.hashPassword("super_admin", salt);

        Admin admin = new Admin( "super admin", "super@gmail.com", hashedPassword, Base64.getEncoder().encodeToString(salt), true);
        try {
            adminRepository.addAdmin(admin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
        ThymeLeafUtil thymeleafUtil = new ThymeLeafUtil(getServletContext());
        templateEngine = ThymeLeafUtil.templateEngine;

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.equals("/")) {
            action = "/login";
        }

        switch (action) {
            case "login":
                showLoginPage(request, response);
                break;
            case "signup":
                showSignUpPage(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page Not Found");
                break;
        }
    }

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("login", ctx, response.getWriter());
    }

    private void showSignUpPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("signup", ctx, response.getWriter());
    }


    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        session.invalidate();
        showLoginPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "signup":
                handleSignup(request, response);
                break;
            default:
                response.sendRedirect("auth?action=login");
                break;
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Login request received");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();

        if (email == null || password == null) {
            logger.warning("Email or password is missing");
        } else {
            logger.info("Email: " + email);
            logger.info("Password: " + password);
        }
        try {
            User user = userService.login(email, password, session);
            logger.info("User logged in successfully: " + user.getEmail());

            String role = user.getRole();
            session.setAttribute("role", role);
            switch (role) {

                case "ADMIN":
                    response.sendRedirect("admin/dashboard");
                    break;
                case "CLIENT":
                    response.sendRedirect("client/dashboard");
                    break;
                default:
                    response.sendRedirect("auth?action=login");
                    break;
            }

            WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            response.setContentType("text/html;charset=UTF-8");
            templateEngine.process("dashboard", ctx, response.getWriter());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Invalid email or password");
            showLoginPage(request, response);
        }
    }

    private void handleSignup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Signup request received");
        HttpSession session = request.getSession();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String shippingAddress = request.getParameter("shipping_address");
        String paymentMethod = request.getParameter("payment_method");

        if (name == null || email == null || password == null) {
            logger.warning("Name, email, password is missing");
        } else {
            logger.info("Name: " + name);
            logger.info("Email: " + email);
            logger.info("Password: " + password);
        }

        try {
            byte[] salt = PasswordUtil.generateSalt();
            assert password != null;
            String hashedPassword = PasswordUtil.hashPassword(password, salt);
           ClientDTO client = new ClientDTO(name, email, hashedPassword,Base64.getEncoder().encodeToString(salt), shippingAddress, PaymentMethod.valueOf(paymentMethod),0);
            clientService.addClient(client);

            String role = "CLIENT";
            session.setAttribute("role", role);

            response.sendRedirect("client/dashboard");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Invalid email or password");
            showLoginPage(request, response);
        }
    }
}