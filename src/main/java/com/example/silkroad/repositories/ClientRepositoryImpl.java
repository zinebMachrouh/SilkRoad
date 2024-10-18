package com.example.silkroad.repositories;

import com.example.silkroad.models.Client;
import com.example.silkroad.repositories.interfaces.ClientRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ClientRepositoryImpl implements ClientRepository {
    private final SessionFactory sessionFactory;

    public ClientRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Client addClient(Client admin) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(admin);
            transaction.commit();
            return admin;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new SQLException("Failed to add admin", e);
        }
    }

    @Override
    public Client getClientById(String id) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Client admin = session.get(Client.class, id);
            transaction.commit();
            return admin;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new SQLException("Failed to get admin", e);
        }
    }

    @Override
    public Client getClientByEmail(String email) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Client admin = session.createQuery("from Client where email = :email", Client.class)
                    .setParameter("email", email)
                    .uniqueResult();
            transaction.commit();
            return admin;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new SQLException("Failed to get admin", e);
        }
    }

    @Override
    public Client updateClient(Client admin) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(admin);
            transaction.commit();
            return admin;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new SQLException("Failed to update admin", e);
        }
    }

    @Override
    public boolean deleteClient(String id) throws SQLException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Client admin = session.get(Client.class, id);
            if (admin != null) {
                session.delete(admin);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new SQLException("Failed to delete admin", e);
        }
    }

    @Override
    public List<Client> getAllClients() throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Client", Client.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Failed to get all admins", e);
        }
    }
}
