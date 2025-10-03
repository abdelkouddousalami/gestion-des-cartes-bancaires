package service;

import models.*;
import repository.CarteDAO;
import repository.impl.CarteDAOImpl;
import exception.DatabaseException;
import exception.CarteInvalideException;
import util.CarteUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CarteService {
    
    private final CarteDAO carteDAO;
    
    public CarteService() {
        this.carteDAO = new CarteDAOImpl();
    }
    
    public Carte creerCarteDebit(int idClient, double plafondJournalier, int moisValidite) throws DatabaseException {
        String numero = CarteUtil.genererNumeroCarte();
        LocalDate dateExpiration = LocalDate.now().plusMonths(moisValidite);
        
        Carte carte = new CarteDebit(numero, dateExpiration, StatutCarte.ACTIVE, idClient, plafondJournalier);
        return carteDAO.create(carte);
    }
    
    public Carte creerCarteCredit(int idClient, double plafondMensuel, double tauxInteret, int moisValidite) throws DatabaseException {
        String numero = CarteUtil.genererNumeroCarte();
        LocalDate dateExpiration = LocalDate.now().plusMonths(moisValidite);
        
        Carte carte = new CarteCredit(numero, dateExpiration, StatutCarte.ACTIVE, idClient, plafondMensuel, tauxInteret);
        return carteDAO.create(carte);
    }
    
    public Carte creerCartePrepayee(int idClient, double soldeInitial, int moisValidite) throws DatabaseException {
        String numero = CarteUtil.genererNumeroCarte();
        LocalDate dateExpiration = LocalDate.now().plusMonths(moisValidite);
        
        Carte carte = new CartePrepayee(numero, dateExpiration, StatutCarte.ACTIVE, idClient, soldeInitial);
        return carteDAO.create(carte);
    }
    
    public void activerCarte(int idCarte) throws DatabaseException, CarteInvalideException {
        Optional<Carte> optCarte = carteDAO.findById(idCarte);
        if (!optCarte.isPresent()) {
            throw new CarteInvalideException("Carte introuvable");
        }
        
        Carte carte = optCarte.get();
        carte.setStatut(StatutCarte.ACTIVE);
        carteDAO.update(carte);
    }
    
    public void suspendreCarte(int idCarte) throws DatabaseException, CarteInvalideException {
        Optional<Carte> optCarte = carteDAO.findById(idCarte);
        if (!optCarte.isPresent()) {
            throw new CarteInvalideException("Carte introuvable");
        }
        
        Carte carte = optCarte.get();
        carte.setStatut(StatutCarte.SUSPENDUE);
        carteDAO.update(carte);
    }
    
    public void bloquerCarte(int idCarte) throws DatabaseException, CarteInvalideException {
        Optional<Carte> optCarte = carteDAO.findById(idCarte);
        if (!optCarte.isPresent()) {
            throw new CarteInvalideException("Carte introuvable");
        }
        
        Carte carte = optCarte.get();
        carte.setStatut(StatutCarte.BLOQUEE);
        carteDAO.update(carte);
    }
    
    public Optional<Carte> rechercherParId(int id) throws DatabaseException {
        return carteDAO.findById(id);
    }
    
    public Optional<Carte> rechercherParNumero(String numero) throws DatabaseException {
        return carteDAO.findByNumero(numero);
    }
    
    public List<Carte> rechercherParClient(int idClient) throws DatabaseException {
        return carteDAO.findByClientId(idClient);
    }
    
    public List<Carte> obtenirToutesCartes() throws DatabaseException {
        return carteDAO.findAll();
    }
    
    public boolean verifierPlafond(Carte carte, double montant) {
        if (carte instanceof CarteDebit) {
            CarteDebit cd = (CarteDebit) carte;
            return montant <= cd.getPlafondJournalier();
        } else if (carte instanceof CarteCredit) {
            CarteCredit cc = (CarteCredit) carte;
            return montant <= cc.getPlafondMensuel();
        } else if (carte instanceof CartePrepayee) {
            CartePrepayee cp = (CartePrepayee) carte;
            return montant <= cp.getSoldeDisponible();
        }
        return false;
    }
    
    public void mettreAJourSolde(Carte carte, double montant) throws DatabaseException {
        if (carte instanceof CartePrepayee) {
            CartePrepayee cp = (CartePrepayee) carte;
            cp.setSoldeDisponible(cp.getSoldeDisponible() - montant);
            carteDAO.update(cp);
        }
    }
}
