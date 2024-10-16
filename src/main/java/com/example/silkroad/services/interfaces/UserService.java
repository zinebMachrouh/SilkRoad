package com.example.silkroad.services.interfaces;

import com.example.silkroad.models.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public interface UserService {
    public User login(String email, String password, HttpSession session) throws SQLException;
}
