package com.example.silkroad.controllers;

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
import com.example.silkroad.services.UserServiceImpl;
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

    @Override
    public void init() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        UserRepository userRepository = new UserRepositoryImpl(sessionFactory);
        AdminRepository adminRepository = new AdminRepositoryImpl(sessionFactory);
        ClientRepository clientRepository = new ClientRepositoryImpl(sessionFactory);

        this.userService = new UserServiceImpl(userRepository, clientRepository, adminRepository);

        byte[] salt = PasswordUtil.generateSalt();
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        Client client = new Client("client","client@gmail.com", PasswordUtil.hashPassword("password", salt), encodedSalt,"jiji", PaymentMethod.CASH, 40);

        try {
            clientRepository.addClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ThymeLeafUtil thymeleafUtil = new ThymeLeafUtil(getServletContext());
        templateEngine = ThymeLeafUtil.templateEngine;

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null || action.equals("/")) {
            action = "login";
        }

        switch (action) {
            case "login":
                showLoginPage(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                showLoginPage(request, response);
                break;
        }
    }

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("login", ctx, response.getWriter());
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

            logger.info("User role: " + role);
            switch (role) {

                case "ADMIN":
                    if (user instanceof Admin) {
                        Admin admin = (Admin) user;

                        if (admin.isSuperAdmin()) {
                            response.sendRedirect("super-admin-dashboard");
                        } else {
                            response.sendRedirect("admin-dashboard");
                        }
                    }
                    break;
                case "CLIENT":
                    response.sendRedirect("client");
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

}