package com.example.silkroad.models;

import com.example.silkroad.models.enums.PaymentMethod;
import com.example.silkroad.models.enums.UserRole;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Table(name = "clients")
@DiscriminatorValue("CLIENT")
public class Client extends User{
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "points", nullable = false)
    private int points;

    public Client() {
    }

    public Client(String name, String email, String password,String salt, String shippingAddress, PaymentMethod paymentMethod, int points) {
        super(name, email, password, salt);
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.points = points;
    }

    public Client(UUID id, String name, String email, String password,String salt, String shippingAddress, PaymentMethod paymentMethod, int points) {
        super(id, name, email, password,salt);
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
}
