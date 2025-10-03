package repository.impl;

import config.DatabaseConfig;
import models.OperationCarte;
import models.TypeOperation;
import repository.OperationDAO;
import exception.DatabaseException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OperationDAOImpl implements OperationDAO {

    @Override
    public OperationCarte create(LocalDateTime dateOperation, double montant, TypeOperation type, String lieu, int idCarte) throws DatabaseException {
        String sql = "INSERT INTO OperationCarte (dateOperation, montant, type, lieu, idCarte) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(dateOperation));
            stmt.setDouble(2, montant);
            stmt.setString(3, type.name());
            stmt.setString(4, lieu);
            stmt.setInt(5, idCarte);
            
            stmt.executeUpdate();
            
            int generatedId = 0;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
            
            return new OperationCarte(generatedId, dateOperation, montant, type, lieu, idCarte);
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la creation de l'operation", e);
        }
    }

    @Override
    public Optional<OperationCarte> findById(int id) throws DatabaseException {
        String sql = "SELECT * FROM OperationCarte WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToOperation(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche de l'operation", e);
        }
    }

    @Override
    public List<OperationCarte> findAll() throws DatabaseException {
        String sql = "SELECT * FROM OperationCarte";
        List<OperationCarte> operations = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                operations.add(mapResultSetToOperation(rs));
            }
            
            return operations;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recuperation des operations", e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM OperationCarte WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression de l'operation", e);
        }
    }

    @Override
    public List<OperationCarte> findByCarteId(int carteId) throws DatabaseException {
        String sql = "SELECT * FROM OperationCarte WHERE idCarte = ? ORDER BY dateOperation DESC";
        List<OperationCarte> operations = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, carteId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    operations.add(mapResultSetToOperation(rs));
                }
            }
            
            return operations;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche des operations par carte", e);
        }
    }

    @Override
    public List<OperationCarte> findByType(TypeOperation type) throws DatabaseException {
        String sql = "SELECT * FROM OperationCarte WHERE type = ?";
        List<OperationCarte> operations = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, type.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    operations.add(mapResultSetToOperation(rs));
                }
            }
            
            return operations;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche des operations par type", e);
        }
    }

    @Override
    public List<OperationCarte> findByDateRange(LocalDateTime debut, LocalDateTime fin) throws DatabaseException {
        String sql = "SELECT * FROM OperationCarte WHERE dateOperation BETWEEN ? AND ?";
        List<OperationCarte> operations = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(debut));
            stmt.setTimestamp(2, Timestamp.valueOf(fin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    operations.add(mapResultSetToOperation(rs));
                }
            }
            
            return operations;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche des operations par date", e);
        }
    }

    private OperationCarte mapResultSetToOperation(ResultSet rs) throws SQLException {
        return new OperationCarte(
            rs.getInt("id"),
            rs.getTimestamp("dateOperation").toLocalDateTime(),
            rs.getDouble("montant"),
            TypeOperation.valueOf(rs.getString("type")),
            rs.getString("lieu"),
            rs.getInt("idCarte")
        );
    }
}
