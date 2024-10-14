package com.example.silkroad.dto;

import com.example.silkroad.models.User;
import com.example.silkroad.models.enums.UserRole;

import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private UserRole role;

    public UserDTO() {
    }

    public UserDTO(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserDTO(UUID id, String name, String email, String password, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role ;
    }

    public User dtoToModel() {
        return new User(this.id, this.name, this.email, this.password, this.role);
    }
    public UserDTO modelToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
