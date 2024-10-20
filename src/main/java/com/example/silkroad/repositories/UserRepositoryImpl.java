package com.example.silkroad.repositories;

import com.example.silkroad.dto.UserDTO;
import com.example.silkroad.models.User;
import com.example.silkroad.repositories.interfaces.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {
    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User getUser(String email) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteUser(UUID id) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new SQLException("Failed to delete user", e);
        }
    }

    @Override
    public List<User> getAllUsers(int offset, int limit) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery(
                            "SELECT u FROM User u " +
                                    "LEFT JOIN Client c ON u.id = c.id " +
                                    "WHERE NOT EXISTS (SELECT 1 FROM Admin a WHERE a.id = u.id AND a.isSuperAdmin = true)",
                            User.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Failed to get all users", e);
        }
    }

}
