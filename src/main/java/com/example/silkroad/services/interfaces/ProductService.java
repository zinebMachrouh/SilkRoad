package com.example.silkroad.services.interfaces;

import com.example.silkroad.dto.ProductDTO;
import com.example.silkroad.models.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    public Product addProduct(ProductDTO product) throws SQLException;
    public Product updateProduct(ProductDTO product) throws SQLException;
    void deleteProduct(int id) throws SQLException;
    List<ProductDTO> getAllProducts() throws SQLException;
    Product getProductById(int id) throws SQLException;
}
