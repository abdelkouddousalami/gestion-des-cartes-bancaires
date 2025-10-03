package service;

import models.*;
import repository.OperationDAO;
import repository.impl.OperationDAOImpl;
import exception.DatabaseException;
import exception.OperationRefuseeException;
import exception.CarteInvalideException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OperationService {
    
    private final OperationDAO operationDAO;
    private final CarteService carteService;
    
    public OperationService() {
        this.operationDAO = new OperationDAOImpl();
        this.carteService = new CarteService();
    }
    
    public OperationCarte effectuerOperation(int idCarte, double montant, TypeOperation type, String lieu) 
            throws DatabaseException, OperationRefuseeException, CarteInvalideException {
        
        Optional<Carte> optCarte = carteService.rechercherParId(idCarte);
        if (!optCarte.isPresent()) {
            throw new CarteInvalideException("Carte introuvable");
        }
        
        Carte carte = optCarte.get();
        
        if (carte.getStatut() != StatutCarte.ACTIVE) {
            throw new OperationRefuseeException("La carte n'est pas active");
        }
        
        if (!carteService.verifierPlafond(carte, montant)) {
            throw new OperationRefuseeException("Plafond depasse");
        }
        
        LocalDateTime maintenant = LocalDateTime.now();
        OperationCarte operation = operationDAO.create(maintenant, montant, type, lieu, idCarte);
        
        carteService.mettreAJourSolde(carte, montant);
        
        return operation;
    }
    
    public List<OperationCarte> obtenirOperationsParCarte(int idCarte) throws DatabaseException {
        return operationDAO.findByCarteId(idCarte);
    }
    
    public List<OperationCarte> obtenirOperationsParType(TypeOperation type) throws DatabaseException {
        return operationDAO.findByType(type);
    }
    
    public List<OperationCarte> obtenirOperationsParPeriode(LocalDateTime debut, LocalDateTime fin) throws DatabaseException {
        return operationDAO.findByDateRange(debut, fin);
    }
    
    public Optional<OperationCarte> rechercherParId(int id) throws DatabaseException {
        return operationDAO.findById(id);
    }
    
    public List<OperationCarte> obtenirToutesOperations() throws DatabaseException {
        return operationDAO.findAll();
    }
}
