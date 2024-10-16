package com.example.silkroad.services.interfaces;

import com.example.silkroad.dto.AdminDTO;
import com.example.silkroad.dto.ClientDTO;
import com.example.silkroad.models.Admin;
import com.example.silkroad.models.Client;

import java.sql.SQLException;
import java.util.List;

public interface ClientService {
    public Client addClient(ClientDTO client) throws SQLException;
    public Client getClientById(String id) throws SQLException;
    public Client getClientByEmail(String email) throws SQLException;
    public Client updateClient(ClientDTO client) throws SQLException;
    public boolean deleteClient(String id) throws SQLException;
    public List<Client> getAllClients() throws SQLException;
}
