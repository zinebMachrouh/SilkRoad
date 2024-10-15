package com.example.silkroad.repositories;

import com.example.silkroad.models.Admin;
import com.example.silkroad.repositories.interfaces.AdminRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class AdminRepositoryImpl implements AdminRepository {

    private final SessionFactory sessionFactory;

    public AdminRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Admin addAdmin(Admin admin) throws SQLException {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
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
    public Admin getAdminById(String id) throws SQLException{
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Admin admin = session.get(Admin.class, id);
            transaction.commit();
            return admin;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new SQLException("Failed to get admin", e);
        }
    }

    @Override
    public Admin getAdminByEmail(String email) throws SQLException{
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Admin admin = session.createQuery("from Admin where email = :email", Admin.class)
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
    public Admin updateAdmin(Admin admin) throws SQLException{
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
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
    public boolean deleteAdmin(String id) throws SQLException{
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Admin admin = session.get(Admin.class, id);
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
    public List<Admin> getAllAdmins() throws SQLException{
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("from Admin", Admin.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Failed to get all admins", e);
        }
    }
}
