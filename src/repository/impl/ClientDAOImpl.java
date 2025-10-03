package repository.impl;

import config.DatabaseConfig;
import models.Client;
import repository.ClientDAO;
import exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAOImpl implements ClientDAO {

    @Override
    public Client create(Client client) throws DatabaseException {
        String sql = "INSERT INTO Client (nom, email, telephone) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getTelephone());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }
            
            return client;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la creation du client", e);
        }
    }

    @Override
    public Optional<Client> findById(int id) throws DatabaseException {
        String sql = "SELECT * FROM Client WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToClient(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche du client", e);
        }
    }

    @Override
    public List<Client> findAll() throws DatabaseException {
        String sql = "SELECT * FROM Client";
        List<Client> clients = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
            
            return clients;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recuperation des clients", e);
        }
    }

    @Override
    public Client update(Client client) throws DatabaseException {
        String sql = "UPDATE Client SET nom = ?, email = ?, telephone = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getTelephone());
            stmt.setInt(4, client.getId());
            
            stmt.executeUpdate();
            
            return client;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la mise a jour du client", e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM Client WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression du client", e);
        }
    }

    @Override
    public Optional<Client> findByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM Client WHERE email = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToClient(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche du client par email", e);
        }
    }

    @Override
    public Optional<Client> findByTelephone(String telephone) throws DatabaseException {
        String sql = "SELECT * FROM Client WHERE telephone = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, telephone);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToClient(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche du client par telephone", e);
        }
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        return new Client(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("email"),
            rs.getString("telephone")
        );
    }
}
