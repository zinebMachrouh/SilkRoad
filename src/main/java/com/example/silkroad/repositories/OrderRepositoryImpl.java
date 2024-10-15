package com.example.silkroad.repositories;

import com.example.silkroad.models.Order;
import com.example.silkroad.repositories.interfaces.OrderRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class OrderRepositoryImpl implements OrderRepository {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(OrderRepositoryImpl.class);

    public OrderRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Order addOrder(Order order) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            logger.info("Order added successfully: {}", order);
            return order;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Failed to add order: {}", order, e);
            throw new SQLException("Failed to add order", e);
        }
    }

    @Override
    public Order updateOrder(Order order) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Order updatedOrder = (Order) session.merge(order);
            transaction.commit();
            logger.info("Order updated successfully: {}", updatedOrder);
            return updatedOrder;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Failed to update order: {}", order, e);
            throw new SQLException("Failed to update order", e);
        }
    }

    @Override
    public void deleteOrder(UUID id) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Order order = session.get(Order.class, id);
            if (order != null) {
                session.delete(order);
                logger.info("Order deleted successfully: {}", order);
            } else {
                logger.warn("Order not found for deletion: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Failed to delete order with id: {}", id, e);
            throw new SQLException("Failed to delete order", e);
        }
    }

    @Override
    public List<Order> getAllOrders() throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            List<Order> orders = session.createQuery("from Order", Order.class).list();
            logger.info("Retrieved {} orders", orders.size());
            return orders;
        } catch (Exception e) {
            logger.error("Failed to get all orders", e);
            throw new SQLException("Failed to get all orders", e);
        }
    }

    @Override
    public Order getOrderById(UUID id) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Order order = session.get(Order.class, id);
            if (order != null) {
                logger.info("Retrieved order: {}", order);
            } else {
                logger.warn("Order not found with id: {}", id);
            }
            return order;
        } catch (Exception e) {
            logger.error("Failed to get order by id: {}", id, e);
            throw new SQLException("Failed to get order by id", e);
        }
    }
}
