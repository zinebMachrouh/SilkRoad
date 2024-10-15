package com.example.silkroad.dto;

import com.example.silkroad.models.Admin;
import com.example.silkroad.models.enums.UserRole;

import java.util.UUID;

public class AdminDTO extends UserDTO {
    private boolean isSuperAdmin;

    public AdminDTO() {
    }

    public AdminDTO(String name, String email, String password, String salt, boolean isSuperAdmin) {
        super(name, email, password, salt, UserRole.ADMIN);
        this.isSuperAdmin = isSuperAdmin;
    }

    public AdminDTO(UUID id, String name, String email, String password, String salt, boolean isSuperAdmin) {
        super(id, name, email, password, salt, UserRole.ADMIN);
        this.isSuperAdmin = isSuperAdmin;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }
    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    @Override
    public String toString() {
        return super.toString()+
                ",isSuperAdmin=" + isSuperAdmin +
                "} ";
    }

    public Admin dtoToModel() {
        return new Admin(getId(), getName(), getEmail(), getPassword(), getSalt(), isSuperAdmin());
    }

    public static AdminDTO modelToDTO(Admin admin) {
        return new AdminDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getPassword(), admin.getSalt(), admin.isSuperAdmin());
    }
}
