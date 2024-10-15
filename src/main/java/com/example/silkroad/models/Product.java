package com.example.silkroad.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Changed from int to UUID

    @Length(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Length(max = 1000, message = "Description must be less than 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

//    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
//    @Digits(integer = 10, fraction = 2, message = "Price must be a valid decimal number")
    @Column(name = "price", nullable = false)
    private double price;

//    @Min(value = 0, message = "Stock cannot be negative")
    @Column(name = "stock")
    private int stock;

//    @Length(max = 255, message = "Image URL must be less than 255 characters")
//    @Column(name = "image", length = 255)
    private String image;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders = new ArrayList<>();

    // Default constructor
    public Product() {
    }

    // Constructor with id
    public Product(UUID id, String name, String description, double price, int stock, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    // Constructor without id
    public Product(String name, String description, double price, int stock, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    // Getters and Setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
