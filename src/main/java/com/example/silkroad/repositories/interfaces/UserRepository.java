package com.example.silkroad.repositories.interfaces;

import com.example.silkroad.dto.UserDTO;
import com.example.silkroad.models.User;
import com.example.silkroad.models.enums.UserRole;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    public User getUser(String email);
    public boolean deleteUser(UUID id) throws SQLException;
    public List<User> getAllUsers(int offset, int limit) throws SQLException;
    public int getUsersCount() throws SQLException;
    public List<User> searchUsers(String search) throws SQLException;
}
