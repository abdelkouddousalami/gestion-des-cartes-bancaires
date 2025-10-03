package repository;

import models.OperationCarte;
import models.TypeOperation;
import exception.DatabaseException;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface OperationDAO {
    OperationCarte create(LocalDateTime dateOperation, double montant, TypeOperation type, String lieu, int idCarte) throws DatabaseException;
    Optional<OperationCarte> findById(int id) throws DatabaseException;
    List<OperationCarte> findAll() throws DatabaseException;
    void delete(int id) throws DatabaseException;
    List<OperationCarte> findByCarteId(int carteId) throws DatabaseException;
    List<OperationCarte> findByType(TypeOperation type) throws DatabaseException;
    List<OperationCarte> findByDateRange(LocalDateTime debut, LocalDateTime fin) throws DatabaseException;
}
