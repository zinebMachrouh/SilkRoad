package com.example.silkroad.services;

import com.example.silkroad.dto.ProductDTO;
import com.example.silkroad.models.Product;
import com.example.silkroad.repositories.interfaces.ProductRepository;
import com.example.silkroad.services.interfaces.OrderService;
import com.example.silkroad.services.interfaces.ProductService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private OrderService orderService;

    public ProductServiceImpl(ProductRepository productRepository, OrderService orderService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @Override
    public Product addProduct(ProductDTO productDTO) throws SQLException{
        Product product = new Product(productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getStock(), productDTO.getImage());
        return productRepository.addProduct(product);
    }

    @Override
    public Product updateProduct(ProductDTO productDTO) throws SQLException{
       if(productRepository.getProductById(productDTO.getId()) == null){
              return null;
       }else{
                Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getStock(), productDTO.getImage());
                return productRepository.updateProduct(product);
       }
    }

    @Override
    public void deleteProduct(UUID id) throws SQLException{
        Product product = productRepository.getProductById(id);
        if (product != null) {
            productRepository.deleteProduct(id);
        }
    }

    @Override
    public List<ProductDTO> getAllProducts() throws SQLException{
        List<Product> allProducts = productRepository.getAllProducts();

        return allProducts.stream().map(ProductDTO::modelToDTO).collect(Collectors.toList());
    }

    @Override
    public Product getProductById(UUID id) throws SQLException{
        return productRepository.getProductById(id);

    }


}
