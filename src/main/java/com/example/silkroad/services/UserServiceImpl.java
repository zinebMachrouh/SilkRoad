package com.example.silkroad.services;

import com.example.silkroad.dto.UserDTO;
import com.example.silkroad.models.Admin;
import com.example.silkroad.models.Client;
import com.example.silkroad.models.User;
import com.example.silkroad.models.enums.UserRole;
import com.example.silkroad.repositories.interfaces.AdminRepository;
import com.example.silkroad.repositories.interfaces.ClientRepository;
import com.example.silkroad.repositories.interfaces.UserRepository;
import com.example.silkroad.services.interfaces.UserService;
import com.example.silkroad.utils.PasswordUtil;
import javax.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;

    public UserServiceImpl(UserRepository userRepository, ClientRepository clientRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public User login(String email, String password,HttpSession session) throws SQLException {
        System.out.println("Login service");
        validateInput(email, password);

        System.out.println("Login service after validation");

        User user = userRepository.getUser(email);
        System.out.println("Login service after user" + user);
        if (user == null) {
            throw new SQLException("User not found");
        }

        if (isPasswordValid(user, password)) {
            return fetchUserByRole(user, email, session);
        } else {
            throw new SQLException("Invalid password");
        }
    }

    private void validateInput(String email, String password) throws SQLException {
        if (email.isEmpty()) {
            throw new SQLException("Email cannot be empty");
        }else if (password.isEmpty()) {
            throw new SQLException("Password cannot be empty");
        }else if (!email.contains("@")) {
            throw new SQLException("Invalid email");
        }else if (password.length() < 8) {
            throw new SQLException("Password must be at least 8 characters long");
        }
    }

    private boolean isPasswordValid(User user, String password) {
        byte[] salt = Base64.getDecoder().decode(user.getSalt());
        String hashedPassword = PasswordUtil.hashPassword(password, salt);
        return hashedPassword.equals(user.getPassword());
    }

    private User fetchUserByRole(User user, String email, HttpSession session) throws SQLException {
        switch (user.getRole()) {
            case "ADMIN":
                Admin admin = adminRepository.getAdminByEmail(email);
                session.setAttribute("loggedUser", admin);
                return admin;
            case "CLIENT":
                Client client = clientRepository.getClientByEmail(email);
                session.setAttribute("loggedUser", client);
                return client;

            default:
                throw new SQLException("Unknown role: " + user.getRole());
        }
    }

     @Override
    public boolean deleteUser(UUID id) throws SQLException {
        return userRepository.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers(int offset, int limit) throws SQLException {
        int page = offset / limit + 1;
        offset = (page - 1) * limit;
        return userRepository.getAllUsers(offset, limit);
    }
}
