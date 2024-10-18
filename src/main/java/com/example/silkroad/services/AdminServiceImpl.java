package com.example.silkroad.services;

import com.example.silkroad.dto.AdminDTO;
import com.example.silkroad.models.Admin;
import com.example.silkroad.repositories.interfaces.AdminRepository;
import com.example.silkroad.services.interfaces.AdminService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin addAdmin(AdminDTO admin) throws SQLException {
        Admin adminModel = admin.dtoToModel();
        return adminRepository.addAdmin(adminModel);
    }

    @Override
    public Admin getAdminById(String id) throws SQLException {
        if (id.isEmpty()) {
            throw new SQLException("Admin id cannot be empty");
        }else {
            return adminRepository.getAdminById(id);
        }
    }

    @Override
    public Admin getAdminByEmail(String email) throws SQLException {
        if (email.isEmpty()){
            throw new SQLException("Admin email cannot be empty");
        }else {
            return adminRepository.getAdminByEmail(email);
        }
    }

    @Override
    public Admin updateAdmin(AdminDTO admin) throws SQLException {
        Admin adminModel = admin.dtoToModel();
        return adminRepository.updateAdmin(adminModel);
    }

    @Override
    public boolean deleteAdmin(String id) throws SQLException {
        if (id.isEmpty()) {
            throw new SQLException("Admin id cannot be empty");
        }else {
            return adminRepository.deleteAdmin(id);
        }
    }

    @Override
    public List<Admin> getAllAdmins() throws SQLException {
        return adminRepository.getAllAdmins();
    }
}
