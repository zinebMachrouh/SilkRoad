package com.example.silkroad.services;

import com.example.silkroad.dto.ProductDTO;
import com.example.silkroad.models.Product;
import com.example.silkroad.repositories.interfaces.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() throws SQLException {
        ProductDTO productDTO = new ProductDTO(null, "Product Name", "Product Description", 29.99, 100, "image_url");
        Product product = productDTO.dtoToModel();  // Convert DTO to model

        when(productRepository.addProduct(any(Product.class))).thenReturn(product);

        Product result = productService.addProduct(productDTO);

        assertNotNull(result);
        assertEquals("Product Name", result.getName());
        verify(productRepository, times(1)).addProduct(any(Product.class));
    }

    @Test
    void testUpdateProduct() throws SQLException {
        UUID productId = UUID.randomUUID();
        ProductDTO productDTO = new ProductDTO(productId, "Updated Product", "Updated Description", 19.99, 50, "new_image_url");
        Product existingProduct = productDTO.dtoToModel(); // Convert DTO to model

        when(productRepository.getProductById(productId)).thenReturn(existingProduct);
        when(productRepository.updateProduct(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.updateProduct(productDTO);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        verify(productRepository, times(1)).updateProduct(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() throws SQLException {
        UUID productId = UUID.randomUUID();
        ProductDTO productDTO = new ProductDTO(productId, "Non-existent Product", "Non-existent Description", 19.99, 50, "non_existent_image_url");

        when(productRepository.getProductById(productId)).thenReturn(null);

        Product result = productService.updateProduct(productDTO);

        assertNull(result);
        verify(productRepository, times(1)).getProductById(productId);
    }

    @Test
    void testDeleteProduct() throws SQLException {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Product to Delete", "Description", 29.99, 100, "image_url");

        when(productRepository.getProductById(productId)).thenReturn(product);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteProduct(productId);
    }

    @Test
    void testDeleteProductNotFound() throws SQLException {
        UUID productId = UUID.randomUUID();

        when(productRepository.getProductById(productId)).thenReturn(null);

        productService.deleteProduct(productId);

        verify(productRepository, times(0)).deleteProduct(productId);
    }

    @Test
    void testGetAllProducts() throws SQLException {
        Product product1 = new Product(UUID.randomUUID(), "Product 1", "Description 1", 29.99, 100, "image_url_1");
        Product product2 = new Product(UUID.randomUUID(), "Product 2", "Description 2", 19.99, 50, "image_url_2");
        List<Product> productList = Arrays.asList(product1, product2);

        when(productRepository.getAllProducts()).thenReturn(productList);

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        verify(productRepository, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() throws SQLException {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Product Name", "Description", 29.99, 100, "image_url");

        when(productRepository.getProductById(productId)).thenReturn(product);

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals("Product Name", result.getName());
        verify(productRepository, times(1)).getProductById(productId);
    }
}
