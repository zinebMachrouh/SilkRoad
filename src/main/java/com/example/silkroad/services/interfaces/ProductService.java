package com.example.silkroad.services.interfaces;

import com.example.silkroad.dto.ProductDTO;
import com.example.silkroad.models.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    public Product addProduct(ProductDTO product) throws SQLException;
    public Product updateProduct(ProductDTO product) throws SQLException;
    void deleteProduct(UUID id) throws SQLException;
    List<ProductDTO> getAllProducts() throws SQLException;
    Product getProductById(UUID id) throws SQLException;
}
