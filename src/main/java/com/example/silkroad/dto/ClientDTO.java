package com.example.silkroad.dto;

import com.example.silkroad.models.Client;
import com.example.silkroad.models.enums.PaymentMethod;
import com.example.silkroad.models.enums.UserRole;

import java.util.UUID;

public class ClientDTO extends UserDTO {
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private int points;

    public ClientDTO() {
    }

    public ClientDTO(String name, String email, String password, String salt, String shippingAddress, PaymentMethod paymentMethod, int points) {
        super(name, email, password,salt, UserRole.CLIENT);
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.points = points;
    }

    public ClientDTO(UUID id, String name, String email, String password, String salt, String shippingAddress, PaymentMethod paymentMethod, int points) {
        super(id, name, email, password, salt, UserRole.CLIENT);
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.points = points;
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

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return super.toString() +
                ",shippingAddress=" + shippingAddress +
                ",paymentMethod=" + paymentMethod +
                ",points=" + points +
                "} ";
    }

    public Client dtoToModel() {
        return new Client(getId(), getName(), getEmail(), getPassword(), getSalt(), shippingAddress, paymentMethod, points);
    }

    public static ClientDTO modelToDTO(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getEmail(), client.getPassword(), client.getSalt(), client.getShippingAddress(), client.getPaymentMethod(), client.getPoints());
    }
}
