package repository.impl;

import config.DatabaseConfig;
import models.AlerteFraude;
import models.NiveauAlerte;
import repository.AlerteDAO;
import exception.DatabaseException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlerteDAOImpl implements AlerteDAO {

    @Override
    public AlerteFraude create(String description, NiveauAlerte niveau, int idCarte) throws DatabaseException {
        String sql = "INSERT INTO AlerteFraude (description, niveau, dateAlerte, idCarte) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            LocalDateTime now = LocalDateTime.now();
            stmt.setString(1, description);
            stmt.setString(2, niveau.name());
            stmt.setTimestamp(3, Timestamp.valueOf(now));
            stmt.setInt(4, idCarte);
            
            stmt.executeUpdate();
            
            int generatedId = 0;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
            
            return new AlerteFraude(generatedId, description, niveau, now, idCarte);
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la creation de l'alerte", e);
        }
    }

    @Override
    public Optional<AlerteFraude> findById(int id) throws DatabaseException {
        String sql = "SELECT * FROM AlerteFraude WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAlerte(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche de l'alerte", e);
        }
    }

    @Override
    public List<AlerteFraude> findAll() throws DatabaseException {
        String sql = "SELECT * FROM AlerteFraude";
        List<AlerteFraude> alertes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                alertes.add(mapResultSetToAlerte(rs));
            }
            
            return alertes;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recuperation des alertes", e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM AlerteFraude WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression de l'alerte", e);
        }
    }

    @Override
    public List<AlerteFraude> findByCarteId(int carteId) throws DatabaseException {
        String sql = "SELECT * FROM AlerteFraude WHERE idCarte = ? ORDER BY dateAlerte DESC";
        List<AlerteFraude> alertes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, carteId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    alertes.add(mapResultSetToAlerte(rs));
                }
            }
            
            return alertes;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche des alertes par carte", e);
        }
    }

    @Override
    public List<AlerteFraude> findByNiveau(NiveauAlerte niveau) throws DatabaseException {
        String sql = "SELECT * FROM AlerteFraude WHERE niveau = ?";
        List<AlerteFraude> alertes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, niveau.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    alertes.add(mapResultSetToAlerte(rs));
                }
            }
            
            return alertes;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche des alertes par niveau", e);
        }
    }

    private AlerteFraude mapResultSetToAlerte(ResultSet rs) throws SQLException {
        return new AlerteFraude(
            rs.getInt("id"),
            rs.getString("description"),
            NiveauAlerte.valueOf(rs.getString("niveau")),
            rs.getTimestamp("dateAlerte").toLocalDateTime(),
            rs.getInt("idCarte")
        );
    }
}
