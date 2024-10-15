package com.example.silkroad.models;

import com.example.silkroad.models.enums.UserRole;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
public class Admin extends User{
    @Column(name = "is_super_admin", nullable = false)
    private boolean isSuperAdmin;

    public Admin() {
    }

    public Admin(String name, String email, String password, boolean isSuperAdmin) {
        super(name, email, password, UserRole.ADMIN);
        this.isSuperAdmin = isSuperAdmin;
    }

    public Admin(UUID id, String name, String email, String password, boolean isSuperAdmin) {
        super(id, name, email, password, UserRole.ADMIN);
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
}