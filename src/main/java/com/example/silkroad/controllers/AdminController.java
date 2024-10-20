package com.example.silkroad.controllers;

import com.example.silkroad.dto.AdminDTO;
import com.example.silkroad.dto.ClientDTO;
import com.example.silkroad.dto.UserDTO;
import com.example.silkroad.models.Admin;

import com.example.silkroad.models.Client;
import com.example.silkroad.models.User;
import com.example.silkroad.models.enums.PaymentMethod;
import com.example.silkroad.models.enums.UserRole;
import com.example.silkroad.repositories.AdminRepositoryImpl;
import com.example.silkroad.repositories.ClientRepositoryImpl;
import com.example.silkroad.repositories.UserRepositoryImpl;
import com.example.silkroad.repositories.interfaces.AdminRepository;
import com.example.silkroad.repositories.interfaces.ClientRepository;
import com.example.silkroad.repositories.interfaces.UserRepository;
import com.example.silkroad.services.AdminServiceImpl;
import com.example.silkroad.services.ClientServiceImpl;
import com.example.silkroad.services.UserServiceImpl;
import com.example.silkroad.services.interfaces.AdminService;
import com.example.silkroad.services.interfaces.ClientService;
import com.example.silkroad.services.interfaces.UserService;
import com.example.silkroad.utils.HibernateUtil;
import com.example.silkroad.utils.NameUtils;
import com.example.silkroad.utils.PasswordUtil;
import com.example.silkroad.utils.ThymeLeafUtil;
import org.hibernate.SessionFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdminController extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService;
    private AdminService adminService;
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        UserRepository userRepository = new UserRepositoryImpl(sessionFactory);
        AdminRepository adminRepository = new AdminRepositoryImpl(sessionFactory);
        ClientRepository clientRepository = new ClientRepositoryImpl(sessionFactory);

        this.userService = new UserServiceImpl(userRepository, clientRepository, adminRepository);
        this.adminService = new AdminServiceImpl(adminRepository);
        this.clientService = new ClientServiceImpl(clientRepository);

        ThymeLeafUtil thymeleafUtil = new ThymeLeafUtil(getServletContext());
        templateEngine = ThymeLeafUtil.templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());

        if (pathInfo == null || pathInfo.equals("/")) {
            showDashboard(ctx, req, resp);
            return;
        }

        switch (pathInfo) {
            case "/dashboard":
                showDashboard(ctx, req, resp);
                break;
            case "/deleteUser":
                try {
                    deleteUser(req);
                    resp.sendRedirect("SilkRoad/admin/dashboard");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                showDashboard(ctx, req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();

        switch (action) {
            case "/addUser":
                addUser(req);
                resp.sendRedirect("admin/dashboard");
                break;
            case "/editUser":
                editUser(req, new WebContext(req, resp, getServletContext(), req.getLocale()), resp);
                resp.sendRedirect("admin/dashboard");
                break;

            case "/search":
                searchUsers(req, resp);
                break;
            default:
                showDashboard(new WebContext(req, resp, getServletContext(), req.getLocale()), req, resp);
                break;

        }

        showDashboard(new WebContext(req, resp, getServletContext(), req.getLocale()), req,resp);
    }

    private void showDashboard(WebContext ctx, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //ctx.setVariable("users", userService.getAllUsers());
        int offset = 0;
        int limit = 12;

        String offsetParam = req.getParameter("offset");
        if (offsetParam != null) {
            try {
                offset = Integer.parseInt(offsetParam);
            } catch (NumberFormatException e) {
                offset = 0;
            }
        }

        List<User> users = Collections.emptyList();
        List<UserDTO> userDTOs = Collections.emptyList();
        int totalUsers = 0;
        try {
            totalUsers = userService.getUsersCount();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            users = userService.getAllUsers(offset, limit);
            userDTOs = users.stream().map(user -> {
                if (user instanceof Admin) {
                    return new UserDTO(user.getId(), user.getName(), user.getEmail(),UserRole.valueOf("ADMIN"), null, null);
                } else if (user instanceof Client) {
                    Client client = (Client) user;
                    return new UserDTO(user.getId(),user.getName(), user.getEmail(), UserRole.valueOf("CLIENT"), client.getShippingAddress(), client.getPaymentMethod());
                }
                return null;
            }).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Admin user = (Admin) req.getSession().getAttribute("loggedUser");
        if (user.isSuperAdmin()){
            ctx.setVariable("name",user.getName());
            ctx.setVariable("users", userDTOs);
            ctx.setVariable("limit", limit);
            ctx.setVariable("offset", offset);
            ctx.setVariable("totalUsers", totalUsers);
            ctx.setVariable("initials", NameUtils.getInitials(user.getName()));
            templateEngine.process("superAdmin/dashboard", ctx, resp.getWriter());
        }else{
            templateEngine.process("admin/dashboard", ctx, resp.getWriter());
        }
    }

    private void addUser(HttpServletRequest req) {
        System.out.println("Adding user");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        String shippingAddress = req.getParameter("shipping_address");
        String paymentMethod = req.getParameter("payment_method");

        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Role: " + role);
        System.out.println("Shipping Address: " + shippingAddress);
        System.out.println("Payment Method: " + paymentMethod);

        byte[] salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        if (role.equals("ADMIN")) {
            try {
                adminService.addAdmin(
                        new AdminDTO(
                                name,
                                email,
                                hashedPassword,
                                Base64.getEncoder().encodeToString(salt),
                                false
                        )
                );
            }catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            try {
                clientService.addClient(
                        new ClientDTO(
                                name,
                                email,
                                hashedPassword,
                                Base64.getEncoder().encodeToString(salt),
                                shippingAddress,
                                PaymentMethod.valueOf(paymentMethod),
                                0
                        )
                );
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }



    private void editUser(HttpServletRequest req, WebContext ctx, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String role = req.getParameter("role");
        String shippingAddress = req.getParameter("shippingAddress");
        String paymentMethod = req.getParameter("paymentMethod");

        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Role: " + role);
        System.out.println("Shipping Address: " + shippingAddress);
        System.out.println("Payment Method: " + paymentMethod);

        AdminDTO adminDTO = null;
        ClientDTO clientDTO = null;
        if(role.equals("ADMIN")){
            try {
                Admin admin = adminService.getAdminById(id);
                adminDTO = AdminDTO.modelToDTO(admin);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            AdminDTO admin = new AdminDTO(UUID.fromString(id),name, email, adminDTO.getPassword(), adminDTO.getSalt(), adminDTO.isSuperAdmin());
            try {
                adminService.updateAdmin(admin);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                Client client = clientService.getClientById(id);
                clientDTO = ClientDTO.modelToDTO(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ClientDTO client = new ClientDTO(UUID.fromString(id),name, email, clientDTO.getPassword(), clientDTO.getSalt(), shippingAddress, PaymentMethod.valueOf(paymentMethod), clientDTO.getPoints());
            try {
                clientService.updateClient(client);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void deleteUser(HttpServletRequest req) throws SQLException {
        String userId = req.getParameter("userId");
        userService.deleteUser(UUID.fromString(userId));
    }

    private void searchUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String search = req.getParameter("search");
        List<User> users = null;
        try {
            users = userService.searchUsers(search);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<UserDTO> userDTOs = users.stream().map(user -> {
            if (user instanceof Admin) {
                return new UserDTO(user.getId(), user.getName(), user.getEmail(), UserRole.ADMIN, null, null);
            } else if (user instanceof Client) {
                Client client = (Client) user;
                return new UserDTO(user.getId(), user.getName(), user.getEmail(), UserRole.CLIENT, client.getShippingAddress(), client.getPaymentMethod());
            }
            return null;
        }).collect(Collectors.toList());

        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("users", userDTOs);
        templateEngine.process("superAdmin/dashboard", ctx, resp.getWriter());
    }
}
