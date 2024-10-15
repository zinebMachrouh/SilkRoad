package com.example.silkroad.repositories.interfaces;

import com.example.silkroad.models.User;
import com.example.silkroad.models.enums.UserRole;

public interface UserRepository {
    public User getUser(String email, String password);
}
