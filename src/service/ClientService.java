package service;

import models.Client;
import repository.ClientDAO;
import repository.impl.ClientDAOImpl;
import exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public class ClientService {
    
    private final ClientDAO clientDAO;
    
    public ClientService() {
        this.clientDAO = new ClientDAOImpl();
    }
    
    public Client creerClient(String nom, String email, String telephone) throws DatabaseException {
        Optional<Client> existant = clientDAO.findByEmail(email);
        if (existant.isPresent()) {
            throw new DatabaseException("Un client avec cet email existe deja");
        }
        
        Client client = new Client(nom, email, telephone);
        return clientDAO.create(client);
    }
    
    public Optional<Client> rechercherParId(int id) throws DatabaseException {
        return clientDAO.findById(id);
    }
    
    public Optional<Client> rechercherParEmail(String email) throws DatabaseException {
        return clientDAO.findByEmail(email);
    }
    
    public Optional<Client> rechercherParTelephone(String telephone) throws DatabaseException {
        return clientDAO.findByTelephone(telephone);
    }
    
    public List<Client> obtenirTousClients() throws DatabaseException {
        return clientDAO.findAll();
    }
    
    public Client modifierClient(Client client) throws DatabaseException {
        return clientDAO.update(client);
    }
    
    public void supprimerClient(int id) throws DatabaseException {
        clientDAO.delete(id);
    }
}
