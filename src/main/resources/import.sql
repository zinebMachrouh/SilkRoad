INSERT INTO users (id, name, email, password, salt, role) VALUES ('8d4f1c64-4387-4c63-8b9b-487c78e9e4b5','admin', 'admin@admin.com', 'password','salt', 'ADMIN');
INSERT Into admins (is_super_admin, id) VALUES (true, '8d4f1c64-4387-4c63-8b9b-487c78e9e4b5');