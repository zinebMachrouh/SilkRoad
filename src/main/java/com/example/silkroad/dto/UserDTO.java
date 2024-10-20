package com.example.silkroad.dto;

import com.example.silkroad.models.User;
import com.example.silkroad.models.enums.PaymentMethod;
import com.example.silkroad.models.enums.UserRole;

import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private String salt;

    public UserDTO() {
    }

    public UserDTO(String name, String email, String password, String salt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public UserDTO(UUID id, String name, String email, String password, String salt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public UserDTO(UUID id, String name, String email, UserRole role, String shippingAddress, PaymentMethod paymentMethod) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
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

    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }



    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'';
    }

    public User dtoToModel() {
        return new User(this.id, this.name, this.email, this.password, this.salt);
    }
    public UserDTO modelToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getSalt(), user.getPassword());
    }
}
