package repository;

import models.Client;
import exception.DatabaseException;
import java.util.List;
import java.util.Optional;

public interface ClientDAO {
    Client create(Client client) throws DatabaseException;
    Optional<Client> findById(int id) throws DatabaseException;
    List<Client> findAll() throws DatabaseException;
    Client update(Client client) throws DatabaseException;
    void delete(int id) throws DatabaseException;
    Optional<Client> findByEmail(String email) throws DatabaseException;
    Optional<Client> findByTelephone(String telephone) throws DatabaseException;
}
