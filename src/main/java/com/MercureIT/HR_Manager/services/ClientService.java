package com.MercureIT.HR_Manager.services;

import com.MercureIT.HR_Manager.models.Client;
import com.MercureIT.HR_Manager.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Get all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Save a new client
    public void save(Client client) {
        clientRepository.save(client);
    }

    // Find client by ID
    public Optional<Client> findById(Integer id) {
        return clientRepository.findById(id);
    }

    // Update client
    public void update(Client client) {
        Optional<Client> existingClient = clientRepository.findById(client.getId());
        if (existingClient.isPresent()) {
            Client updatedClient = existingClient.get();
            updatedClient.setFirstname(client.getFirstname());
            updatedClient.setLastname(client.getLastname());
            updatedClient.setEmail(client.getEmail());
            updatedClient.setPhone(client.getPhone());
            updatedClient.setDepartment(client.getDepartment());
            updatedClient.setPayment(client.getPayment());
            clientRepository.save(updatedClient);
        }
    }

    // Delete client
    public void delete(Integer id) {
        clientRepository.deleteById(id);
    }

    // Search clients by keyword
    public List<Client> searchClients(String keyword) {
        return clientRepository.findByFirstnameContainingOrLastnameContainingOrEmailContaining(keyword, keyword, keyword);
    }
    public Integer countClients() {
        return Math.toIntExact(clientRepository.count());
    }
    public void updateClient(Integer id, Client updatedClient) {
        Client existingClient = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));
        existingClient.setFirstname(updatedClient.getFirstname());
        existingClient.setLastname(updatedClient.getLastname());
        existingClient.setEmail(updatedClient.getEmail());
        existingClient.setPhone(updatedClient.getPhone());
        existingClient.setPayment(updatedClient.getPayment());
        existingClient.setDepartment(updatedClient.getDepartment());
        clientRepository.save(existingClient);
    }

}
