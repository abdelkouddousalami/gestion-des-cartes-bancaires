package service;

import models.*;
import repository.OperationDAO;
import repository.AlerteDAO;
import repository.impl.OperationDAOImpl;
import repository.impl.AlerteDAOImpl;
import exception.DatabaseException;
import exception.FraudeDetecteeException;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FraudeService {
    
    private final OperationDAO operationDAO;
    private final AlerteDAO alerteDAO;
    private final CarteService carteService;
    
    private static final double MONTANT_SUSPECT = 10000.0;
    private static final int MINUTES_ENTRE_OPERATIONS = 30;
    
    public FraudeService() {
        this.operationDAO = new OperationDAOImpl();
        this.alerteDAO = new AlerteDAOImpl();
        this.carteService = new CarteService();
    }
    
    public void analyserFraude(int idCarte) throws DatabaseException, FraudeDetecteeException {
        List<OperationCarte> operations = operationDAO.findByCarteId(idCarte);
        
        if (operations.isEmpty()) {
            return;
        }
        
        verifierMontantEleve(operations, idCarte);
        verifierOperationsRapprochees(operations, idCarte);
        verifierLieuxDifferents(operations, idCarte);
    }
    
    private void verifierMontantEleve(List<OperationCarte> operations, int idCarte) throws DatabaseException {
        for (OperationCarte op : operations) {
            if (op.getMontant() > MONTANT_SUSPECT) {
                creerAlerte(
                    "Montant suspect de " + op.getMontant() + " MAD detecte",
                    NiveauAlerte.CRITIQUE,
                    idCarte
                );
            }
        }
    }
    
    private void verifierOperationsRapprochees(List<OperationCarte> operations, int idCarte) throws DatabaseException {
        for (int i = 0; i < operations.size() - 1; i++) {
            OperationCarte op1 = operations.get(i);
            OperationCarte op2 = operations.get(i + 1);
            
            Duration duree = Duration.between(op2.getDateOperation(), op1.getDateOperation());
            long minutes = Math.abs(duree.toMinutes());
            
            if (minutes < MINUTES_ENTRE_OPERATIONS) {
                creerAlerte(
                    "Operations rapprochees detectees: " + minutes + " minutes d'ecart",
                    NiveauAlerte.AVERTISSEMENT,
                    idCarte
                );
            }
        }
    }
    
    private void verifierLieuxDifferents(List<OperationCarte> operations, int idCarte) throws DatabaseException {
        Map<String, LocalDateTime> lieuxVisites = new HashMap<>();
        
        for (OperationCarte op : operations) {
            String lieu = op.getLieu();
            LocalDateTime date = op.getDateOperation();
            
            for (Map.Entry<String, LocalDateTime> entry : lieuxVisites.entrySet()) {
                String autreLieu = entry.getKey();
                LocalDateTime autreDate = entry.getValue();
                
                if (!lieu.equals(autreLieu)) {
                    Duration duree = Duration.between(autreDate, date);
                    long heures = Math.abs(duree.toHours());
                    
                    if (heures < 2) {
                        creerAlerte(
                            "Operations dans des lieux differents (" + autreLieu + " et " + lieu + ") en moins de 2 heures",
                            NiveauAlerte.CRITIQUE,
                            idCarte
                        );
                    }
                }
            }
            
            lieuxVisites.put(lieu, date);
        }
    }
    
    public void creerAlerte(String description, NiveauAlerte niveau, int idCarte) throws DatabaseException {
        alerteDAO.create(description, niveau, idCarte);
        
        if (niveau == NiveauAlerte.CRITIQUE) {
            try {
                carteService.bloquerCarte(idCarte);
            } catch (Exception e) {
                System.err.println("Erreur lors du blocage de la carte: " + e.getMessage());
            }
        }
    }
    
    public List<AlerteFraude> obtenirAlertesParCarte(int idCarte) throws DatabaseException {
        return alerteDAO.findByCarteId(idCarte);
    }
    
    public List<AlerteFraude> obtenirAlertesParNiveau(NiveauAlerte niveau) throws DatabaseException {
        return alerteDAO.findByNiveau(niveau);
    }
    
    public List<AlerteFraude> obtenirToutesAlertes() throws DatabaseException {
        return alerteDAO.findAll();
    }
}
