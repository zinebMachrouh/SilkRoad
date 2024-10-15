package com.example.silkroad.services.interfaces;

import com.example.silkroad.dto.AdminDTO;
import com.example.silkroad.models.Admin;

import java.sql.SQLException;
import java.util.List;

public interface AdminService {
    public Admin addAdmin(AdminDTO admin) throws SQLException;
    public Admin getAdminById(String id) throws SQLException;
    public Admin getAdminByEmail(String email) throws SQLException;
    public Admin updateAdmin(AdminDTO admin) throws SQLException;
    public boolean deleteAdmin(String id) throws SQLException;
    public List<Admin> getAllAdmins() throws SQLException;
}
