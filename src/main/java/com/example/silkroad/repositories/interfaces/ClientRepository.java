package com.example.silkroad.repositories.interfaces;

import com.example.silkroad.models.Client;

import java.sql.SQLException;
import java.util.List;

public interface ClientRepository {
    public Client addClient(Client admin) throws SQLException;
    public Client getClientById(String id) throws SQLException;
    public Client getClientByEmail(String email) throws SQLException;
    public Client updateClient(Client admin) throws SQLException;
    public boolean deleteClient(String id) throws SQLException;
    public List<Client> getAllClients() throws SQLException;
}
