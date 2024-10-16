package com.example.silkroad.repositories.interfaces;

import com.example.silkroad.models.Admin;

import java.sql.SQLException;
import java.util.List;

public interface AdminRepository {
    public Admin addAdmin(Admin admin) throws SQLException;
    public Admin getAdminById(String id) throws SQLException;
    public Admin getAdminByEmail(String email) throws SQLException;
    public Admin updateAdmin(Admin admin) throws SQLException;
    public boolean deleteAdmin(String id) throws SQLException;
    public List<Admin> getAllAdmins() throws SQLException;
}
