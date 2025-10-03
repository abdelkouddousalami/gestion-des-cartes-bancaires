package repository;

import models.AlerteFraude;
import models.NiveauAlerte;
import exception.DatabaseException;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface AlerteDAO {
    AlerteFraude create(String description, NiveauAlerte niveau, int idCarte) throws DatabaseException;
    Optional<AlerteFraude> findById(int id) throws DatabaseException;
    List<AlerteFraude> findAll() throws DatabaseException;
    void delete(int id) throws DatabaseException;
    List<AlerteFraude> findByCarteId(int carteId) throws DatabaseException;
    List<AlerteFraude> findByNiveau(NiveauAlerte niveau) throws DatabaseException;
}
