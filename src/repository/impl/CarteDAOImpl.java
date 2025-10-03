package repository.impl;

import config.DatabaseConfig;
import models.*;
import repository.CarteDAO;
import exception.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarteDAOImpl implements CarteDAO {

    @Override
    public Carte create(Carte carte) throws DatabaseException {
        String sql = "INSERT INTO Carte (numero, dateExpiration, statut, typeCarte, plafondJournalier, plafondMensuel, tauxInteret, soldeDisponible, idClient) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, carte.getNumero());
            stmt.setDate(2, Date.valueOf(carte.getDateExpiration()));
            stmt.setString(3, carte.getStatut().name());
            stmt.setString(4, carte.getTypeCarte().name());
            
            if (carte instanceof CarteDebit) {
                CarteDebit cd = (CarteDebit) carte;
                stmt.setDouble(5, cd.getPlafondJournalier());
                stmt.setNull(6, Types.DOUBLE);
                stmt.setNull(7, Types.DOUBLE);
                stmt.setNull(8, Types.DOUBLE);
            } else if (carte instanceof CarteCredit) {
                CarteCredit cc = (CarteCredit) carte;
                stmt.setNull(5, Types.DOUBLE);
                stmt.setDouble(6, cc.getPlafondMensuel());
                stmt.setDouble(7, cc.getTauxInteret());
                stmt.setNull(8, Types.DOUBLE);
            } else if (carte instanceof CartePrepayee) {
                CartePrepayee cp = (CartePrepayee) carte;
                stmt.setNull(5, Types.DOUBLE);
                stmt.setNull(6, Types.DOUBLE);
                stmt.setNull(7, Types.DOUBLE);
                stmt.setDouble(8, cp.getSoldeDisponible());
            }
            
            stmt.setInt(9, carte.getIdClient());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    carte.setId(rs.getInt(1));
                }
            }
            
            return carte;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la creation de la carte", e);
        }
    }

    @Override
    public Optional<Carte> findById(int id) throws DatabaseException {
        String sql = "SELECT * FROM Carte WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCarte(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche de la carte", e);
        }
    }

    @Override
    public List<Carte> findAll() throws DatabaseException {
        String sql = "SELECT * FROM Carte";
        List<Carte> cartes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                cartes.add(mapResultSetToCarte(rs));
            }
            
            return cartes;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recuperation des cartes", e);
        }
    }

    @Override
    public Carte update(Carte carte) throws DatabaseException {
        String sql = "UPDATE Carte SET numero = ?, dateExpiration = ?, statut = ?, typeCarte = ?, plafondJournalier = ?, plafondMensuel = ?, tauxInteret = ?, soldeDisponible = ?, idClient = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, carte.getNumero());
            stmt.setDate(2, Date.valueOf(carte.getDateExpiration()));
            stmt.setString(3, carte.getStatut().name());
            stmt.setString(4, carte.getTypeCarte().name());
            
            if (carte instanceof CarteDebit) {
                CarteDebit cd = (CarteDebit) carte;
                stmt.setDouble(5, cd.getPlafondJournalier());
                stmt.setNull(6, Types.DOUBLE);
                stmt.setNull(7, Types.DOUBLE);
                stmt.setNull(8, Types.DOUBLE);
            } else if (carte instanceof CarteCredit) {
                CarteCredit cc = (CarteCredit) carte;
                stmt.setNull(5, Types.DOUBLE);
                stmt.setDouble(6, cc.getPlafondMensuel());
                stmt.setDouble(7, cc.getTauxInteret());
                stmt.setNull(8, Types.DOUBLE);
            } else if (carte instanceof CartePrepayee) {
                CartePrepayee cp = (CartePrepayee) carte;
                stmt.setNull(5, Types.DOUBLE);
                stmt.setNull(6, Types.DOUBLE);
                stmt.setNull(7, Types.DOUBLE);
                stmt.setDouble(8, cp.getSoldeDisponible());
            }
            
            stmt.setInt(9, carte.getIdClient());
            stmt.setInt(10, carte.getId());
            
            stmt.executeUpdate();
            
            return carte;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la mise a jour de la carte", e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM Carte WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression de la carte", e);
        }
    }

    @Override
    public List<Carte> findByClientId(int clientId) throws DatabaseException {
        String sql = "SELECT * FROM Carte WHERE idClient = ?";
        List<Carte> cartes = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cartes.add(mapResultSetToCarte(rs));
                }
            }
            
            return cartes;
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche des cartes par client", e);
        }
    }

    @Override
    public Optional<Carte> findByNumero(String numero) throws DatabaseException {
        String sql = "SELECT * FROM Carte WHERE numero = ?";
        
        try (Connection conn = DatabaseConfig.getconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numero);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCarte(rs));
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche de la carte par numero", e);
        }
    }

    private Carte mapResultSetToCarte(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String numero = rs.getString("numero");
        LocalDate dateExpiration = rs.getDate("dateExpiration").toLocalDate();
        StatutCarte statut = StatutCarte.valueOf(rs.getString("statut"));
        TypeCarte typeCarte = TypeCarte.valueOf(rs.getString("typeCarte"));
        int idClient = rs.getInt("idClient");
        
        if (typeCarte == TypeCarte.DEBIT) {
            double plafondJournalier = rs.getDouble("plafondJournalier");
            return new CarteDebit(id, numero, dateExpiration, statut, idClient, plafondJournalier);
        } else if (typeCarte == TypeCarte.CREDIT) {
            double plafondMensuel = rs.getDouble("plafondMensuel");
            double tauxInteret = rs.getDouble("tauxInteret");
            return new CarteCredit(id, numero, dateExpiration, statut, idClient, plafondMensuel, tauxInteret);
        } else {
            double soldeDisponible = rs.getDouble("soldeDisponible");
            return new CartePrepayee(id, numero, dateExpiration, statut, idClient, soldeDisponible);
        }
    }
}
