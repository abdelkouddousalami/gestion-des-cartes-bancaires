package repository;

import models.Carte;
import exception.DatabaseException;
import java.util.List;
import java.util.Optional;

public interface CarteDAO {
    Carte create(Carte carte) throws DatabaseException;
    Optional<Carte> findById(int id) throws DatabaseException;
    List<Carte> findAll() throws DatabaseException;
    Carte update(Carte carte) throws DatabaseException;
    void delete(int id) throws DatabaseException;
    List<Carte> findByClientId(int clientId) throws DatabaseException;
    Optional<Carte> findByNumero(String numero) throws DatabaseException;
}
