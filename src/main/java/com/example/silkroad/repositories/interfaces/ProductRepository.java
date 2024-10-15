package com.example.silkroad.repositories.interfaces;

import com.example.silkroad.models.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ProductRepository {
    public Product addProduct(Product product) throws SQLException;
    public Product updateProduct(Product product) throws SQLException;
    void deleteProduct(UUID id) throws SQLException;
    List<Product> getAllProducts() throws SQLException;
    Product getProductById(UUID id) throws SQLException;
}
