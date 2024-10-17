package com.example.silkroad.repositories;

import com.example.silkroad.models.Product;
import com.example.silkroad.repositories.interfaces.ProductRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ProductRepositoryImpl implements ProductRepository {

    private final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(ProductRepositoryImpl.class);

    public ProductRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Product addProduct(Product product) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.save(product); // Save the product

            transaction.commit(); // Commit the transaction
            logger.info("Product added successfully: {}", product); // Log success

            return product; // Return the saved product
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Rollback only if the transaction is active
            }
            logger.error("Failed to add product: {}", product, e); // Log failure
            throw new SQLException("Failed to add product", e); // Throw a SQLException with details
        }
    }


    @Override
    public Product updateProduct(Product product) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Product updatedProduct = (Product) session.merge(product);
            transaction.commit();
            logger.info("Product updated successfully: {}", updatedProduct); // Log product update
            return updatedProduct;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update product: {}", product, e); // Log the exception
            throw new SQLException("Failed to update product", e);
        }
    }

    @Override
    public void deleteProduct(UUID id) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null) {
                session.delete(product);
                logger.info("Product deleted successfully: {}", product); // Log product deletion
            } else {
                logger.warn("Product not found for deletion: {}", id); // Log warning if product not found
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to delete product with id: {}", id, e); // Log the exception
        }
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            List<Product> products = session.createQuery("from Product", Product.class).list();
            logger.info("Retrieved {} products", products.size()); // Log the number of products retrieved
            return products;
        } catch (Exception e) {
            logger.error("Failed to get all products", e); // Log the exception
            throw new SQLException("Failed to get all products", e);
        }
    }

    @Override
    public Product getProductById(UUID id) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Product product = session.get(Product.class, id);
            if (product != null) {
                logger.info("Retrieved product: {}", product); // Log product retrieval
            } else {
                logger.warn("Product not found with id: {}", id); // Log warning if product not found
            }
            return product;
        } catch (Exception e) {
            logger.error("Failed to get product by id: {}", id, e); // Log the exception
            throw new SQLException("Failed to get product by id", e);
        }
    }
}
