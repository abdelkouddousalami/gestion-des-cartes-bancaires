package service;

import models.*;
import repository.CarteDAO;
import repository.OperationDAO;
import repository.impl.CarteDAOImpl;
import repository.impl.OperationDAOImpl;
import exception.DatabaseException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class RapportService {
    
    private final CarteDAO carteDAO;
    private final OperationDAO operationDAO;
    
    public RapportService() {
        this.carteDAO = new CarteDAOImpl();
        this.operationDAO = new OperationDAOImpl();
    }
    
    public List<Map.Entry<Integer, Long>> obtenirTop5CartesUtilisees() throws DatabaseException {
        List<OperationCarte> operations = operationDAO.findAll();
        
        Map<Integer, Long> compteurOperations = new HashMap<>();
        
        for (OperationCarte op : operations) {
            int idCarte = op.getIdCarte();
            compteurOperations.put(idCarte, compteurOperations.getOrDefault(idCarte, 0L) + 1);
        }
        
        List<Map.Entry<Integer, Long>> liste = new ArrayList<>(compteurOperations.entrySet());
        Collections.sort(liste, new Comparator<Map.Entry<Integer, Long>>() {
            @Override
            public int compare(Map.Entry<Integer, Long> e1, Map.Entry<Integer, Long> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });
        
        int limite = Math.min(5, liste.size());
        return liste.subList(0, limite);
    }
    
    public Map<TypeOperation, Long> obtenirStatistiquesMensuelles() throws DatabaseException {
        YearMonth moisActuel = YearMonth.now();
        LocalDateTime debutMois = moisActuel.atDay(1).atStartOfDay();
        LocalDateTime finMois = moisActuel.atEndOfMonth().atTime(23, 59, 59);
        
        List<OperationCarte> operations = operationDAO.findByDateRange(debutMois, finMois);
        
        Map<TypeOperation, Long> statistiques = new HashMap<>();
        statistiques.put(TypeOperation.ACHAT, 0L);
        statistiques.put(TypeOperation.RETRAIT, 0L);
        statistiques.put(TypeOperation.PAIEMENTENLIGNE, 0L);
        
        for (OperationCarte op : operations) {
            TypeOperation type = op.getType();
            statistiques.put(type, statistiques.get(type) + 1);
        }
        
        return statistiques;
    }
    
    public Map<TypeOperation, Double> obtenirMontantsTotauxMensuels() throws DatabaseException {
        YearMonth moisActuel = YearMonth.now();
        LocalDateTime debutMois = moisActuel.atDay(1).atStartOfDay();
        LocalDateTime finMois = moisActuel.atEndOfMonth().atTime(23, 59, 59);
        
        List<OperationCarte> operations = operationDAO.findByDateRange(debutMois, finMois);
        
        Map<TypeOperation, Double> montants = new HashMap<>();
        montants.put(TypeOperation.ACHAT, 0.0);
        montants.put(TypeOperation.RETRAIT, 0.0);
        montants.put(TypeOperation.PAIEMENTENLIGNE, 0.0);
        
        for (OperationCarte op : operations) {
            TypeOperation type = op.getType();
            montants.put(type, montants.get(type) + op.getMontant());
        }
        
        return montants;
    }
    
    public List<Carte> obtenirCartesBloquees() throws DatabaseException {
        List<Carte> toutesCartes = carteDAO.findAll();
        List<Carte> cartesBloquees = new ArrayList<>();
        
        for (Carte carte : toutesCartes) {
            if (carte.getStatut() == StatutCarte.BLOQUEE) {
                cartesBloquees.add(carte);
            }
        }
        
        return cartesBloquees;
    }
    
    public List<Carte> obtenirCartesSuspectes() throws DatabaseException {
        List<Carte> toutesCartes = carteDAO.findAll();
        List<Carte> cartesSuspectes = new ArrayList<>();
        
        for (Carte carte : toutesCartes) {
            if (carte.getStatut() == StatutCarte.BLOQUEE || carte.getStatut() == StatutCarte.SUSPENDUE) {
                cartesSuspectes.add(carte);
            }
        }
        
        return cartesSuspectes;
    }
}
