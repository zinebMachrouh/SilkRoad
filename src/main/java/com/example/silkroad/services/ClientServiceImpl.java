package com.example.silkroad.services;

import com.example.silkroad.dto.ClientDTO;
import com.example.silkroad.models.Admin;
import com.example.silkroad.models.Client;
import com.example.silkroad.repositories.interfaces.AdminRepository;
import com.example.silkroad.repositories.interfaces.ClientRepository;
import com.example.silkroad.services.interfaces.ClientService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client addClient(ClientDTO client) throws SQLException {
        Client clientModel = client.dtoToModel();
        return clientRepository.addClient(clientModel);
    }

    @Override
    public Client getClientById(String id) throws SQLException {
        if (id.isEmpty()) {
            throw new SQLException("Client id cannot be empty");
        }else {
            return clientRepository.getClientById(id);
        }
    }

    @Override
    public Client getClientByEmail(String email) throws SQLException {
        if (email.isEmpty()){
            throw new SQLException("Client email cannot be empty");
        }else {
            return clientRepository.getClientByEmail(email);
        }
    }

    @Override
    public Client updateClient(ClientDTO client) throws SQLException {
        Client clientModel = client.dtoToModel();
        return clientRepository.updateClient(clientModel);
    }

    @Override
    public boolean deleteClient(String id) throws SQLException {
        if (id.isEmpty()) {
            throw new SQLException("Client id cannot be empty");
        }else {
            return clientRepository.deleteClient(id);
        }
    }

    @Override
    public List<Client> getAllClients() throws SQLException {
        return clientRepository.getAllClients();
    }
}
