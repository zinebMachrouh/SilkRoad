package com.example.silkroad.services.interfaces;

import com.example.silkroad.dto.UserDTO;
import com.example.silkroad.models.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    public User login(String email, String password, HttpSession session) throws SQLException;
    public boolean deleteUser(UUID id) throws SQLException;
    List<User> getAllUsers(int page, int limit) throws SQLException;
}
