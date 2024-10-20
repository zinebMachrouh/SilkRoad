package com.example.silkroad.services;

import com.example.silkroad.dto.AdminDTO;
import com.example.silkroad.models.Admin;
import com.example.silkroad.repositories.interfaces.AdminRepository;
import com.example.silkroad.services.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {
    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAdmin() throws SQLException {
        AdminDTO adminDTO = new AdminDTO("Jane Doe", "jane.doe@example.com", "securePassword", "randomSalt", false);
        Admin admin = new Admin("Jane Doe", "jane.doe@example.com", "securePassword", "randomSalt", false);

        when(adminRepository.addAdmin(any(Admin.class))).thenReturn(admin);

        Admin result = adminService.addAdmin(adminDTO);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        verify(adminRepository, times(1)).addAdmin(any(Admin.class));
    }

    @Test
    void testUpdateAdmin() throws SQLException {
        AdminDTO adminDTO = new AdminDTO("John Doe", "john.doe@example.com", "securePassword", "randomSalt", true);
        Admin admin = new Admin("John Doe", "john.doe@example.com", "securePassword", "randomSalt", true);

        when(adminRepository.updateAdmin(any(Admin.class))).thenReturn(admin);

        Admin result = adminService.updateAdmin(adminDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(adminRepository, times(1)).updateAdmin(any(Admin.class));
    }

    @Test
    void testDeleteAdmin() throws SQLException {
        String adminId = "admin-uuid";

        when(adminRepository.deleteAdmin(adminId)).thenReturn(true);

        boolean result = adminService.deleteAdmin(adminId);

        assertTrue(result);
        verify(adminRepository, times(1)).deleteAdmin(adminId);
    }

    @Test
    void testGetAdminById() throws SQLException {
        String adminId = "admin-uuid";
        Admin admin = new Admin("Jane Doe", "jane.doe@example.com", "securePassword", "randomSalt", false);

        when(adminRepository.getAdminById(adminId)).thenReturn(admin);

        Admin result = adminService.getAdminById(adminId);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        verify(adminRepository, times(1)).getAdminById(adminId);
    }

    @Test
    void testGetAllAdmins() throws SQLException {
        Admin admin1 = new Admin("Admin 1", "admin1@example.com", "password1", "salt1", true);
        Admin admin2 = new Admin("Admin 2", "admin2@example.com", "password2", "salt2", false);
        List<Admin> adminList = Arrays.asList(admin1, admin2);

        when(adminRepository.getAllAdmins()).thenReturn(adminList);

        List<Admin> result = adminService.getAllAdmins();

        assertEquals(2, result.size());
        assertEquals("Admin 1", result.get(0).getName());
        assertEquals("Admin 2", result.get(1).getName());
        verify(adminRepository, times(1)).getAllAdmins();
    }
}

