package ui;

import models.*;
import service.*;
import exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class MenuPrincipal {
    
    private final Scanner scanner;
    private final ClientService clientService;
    private final CarteService carteService;
    private final OperationService operationService;
    private final FraudeService fraudeService;
    private final RapportService rapportService;
    
    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.clientService = new ClientService();
        this.carteService = new CarteService();
        this.operationService = new OperationService();
        this.fraudeService = new FraudeService();
        this.rapportService = new RapportService();
    }
    
    public void afficher() {
        boolean continuer = true;
        
        while (continuer) {
            System.out.println("\n========== GESTION DES CARTES BANCAIRES ==========");
            System.out.println("1. Gestion des clients");
            System.out.println("2. Gestion des cartes");
            System.out.println("3. Operations bancaires");
            System.out.println("4. Detection de fraude");
            System.out.println("5. Rapports et statistiques");
            System.out.println("0. Quitter");
            System.out.print("Votre choix: ");
            
            int choix = lireEntier();
            
            switch (choix) {
                case 1:
                    menuClients();
                    break;
                case 2:
                    menuCartes();
                    break;
                case 3:
                    menuOperations();
                    break;
                case 4:
                    menuFraude();
                    break;
                case 5:
                    menuRapports();
                    break;
                case 0:
                    continuer = false;
                    System.out.println("Au revoir!");
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
        
        scanner.close();
    }
    
    private void menuClients() {
        System.out.println("\n--- GESTION DES CLIENTS ---");
        System.out.println("1. Creer un client");
        System.out.println("2. Rechercher un client");
        System.out.println("3. Lister tous les clients");
        System.out.println("0. Retour");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                creerClient();
                break;
            case 2:
                rechercherClient();
                break;
            case 3:
                listerClients();
                break;
            case 0:
                break;
            default:
                System.out.println("Choix invalide!");
        }
    }
    
    private void creerClient() {
        try {
            System.out.print("Nom: ");
            String nom = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Telephone: ");
            String telephone = scanner.nextLine();
            
            Client client = clientService.creerClient(nom, email, telephone);
            System.out.println("Client cree avec succes! ID: " + client.getId());
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void rechercherClient() {
        System.out.println("Rechercher par:");
        System.out.println("1. ID");
        System.out.println("2. Email");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        try {
            Optional<Client> client = Optional.empty();
            
            if (choix == 1) {
                System.out.print("ID du client: ");
                int id = lireEntier();
                client = clientService.rechercherParId(id);
            } else if (choix == 2) {
                System.out.print("Email du client: ");
                String email = scanner.nextLine();
                client = clientService.rechercherParEmail(email);
            }
            
            if (client.isPresent()) {
                System.out.println("\nClient trouve:");
                System.out.println(client.get());
            } else {
                System.out.println("Client introuvable");
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void listerClients() {
        try {
            List<Client> clients = clientService.obtenirTousClients();
            
            if (clients.isEmpty()) {
                System.out.println("Aucun client trouve");
            } else {
                System.out.println("\n--- LISTE DES CLIENTS ---");
                for (Client c : clients) {
                    System.out.println(c);
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void menuCartes() {
        System.out.println("\n--- GESTION DES CARTES ---");
        System.out.println("1. Emettre une carte");
        System.out.println("2. Activer une carte");
        System.out.println("3. Suspendre une carte");
        System.out.println("4. Bloquer une carte");
        System.out.println("5. Consulter les cartes d'un client");
        System.out.println("0. Retour");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                emettreCartes();
                break;
            case 2:
                activerCarte();
                break;
            case 3:
                suspendreCarte();
                break;
            case 4:
                bloquerCarte();
                break;
            case 5:
                consulterCartesClient();
                break;
            case 0:
                break;
            default:
                System.out.println("Choix invalide!");
        }
    }
    
    private void emettreCartes() {
        try {
            System.out.print("ID du client: ");
            int idClient = lireEntier();
            
            System.out.println("Type de carte:");
            System.out.println("1. Carte de debit");
            System.out.println("2. Carte de credit");
            System.out.println("3. Carte prepayee");
            System.out.print("Votre choix: ");
            
            int type = lireEntier();
            
            System.out.print("Duree de validite (mois): ");
            int mois = lireEntier();
            
            Carte carte = null;
            
            if (type == 1) {
                System.out.print("Plafond journalier: ");
                double plafond = lireDouble();
                carte = carteService.creerCarteDebit(idClient, plafond, mois);
            } else if (type == 2) {
                System.out.print("Plafond mensuel: ");
                double plafond = lireDouble();
                System.out.print("Taux d'interet (%): ");
                double taux = lireDouble();
                carte = carteService.creerCarteCredit(idClient, plafond, taux, mois);
            } else if (type == 3) {
                System.out.print("Solde initial: ");
                double solde = lireDouble();
                carte = carteService.creerCartePrepayee(idClient, solde, mois);
            }
            
            if (carte != null) {
                System.out.println("Carte emise avec succes!");
                System.out.println("Numero: " + carte.getNumero());
                System.out.println("Date d'expiration: " + carte.getDateExpiration());
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void activerCarte() {
        try {
            System.out.print("ID de la carte: ");
            int id = lireEntier();
            carteService.activerCarte(id);
            System.out.println("Carte activee avec succes!");
        } catch (DatabaseException | CarteInvalideException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void suspendreCarte() {
        try {
            System.out.print("ID de la carte: ");
            int id = lireEntier();
            carteService.suspendreCarte(id);
            System.out.println("Carte suspendue avec succes!");
        } catch (DatabaseException | CarteInvalideException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void bloquerCarte() {
        try {
            System.out.print("ID de la carte: ");
            int id = lireEntier();
            carteService.bloquerCarte(id);
            System.out.println("Carte bloquee avec succes!");
        } catch (DatabaseException | CarteInvalideException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void consulterCartesClient() {
        try {
            System.out.print("ID du client: ");
            int id = lireEntier();
            
            List<Carte> cartes = carteService.rechercherParClient(id);
            
            if (cartes.isEmpty()) {
                System.out.println("Aucune carte trouvee");
            } else {
                System.out.println("\n--- CARTES DU CLIENT ---");
                for (Carte c : cartes) {
                    System.out.println(c);
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void menuOperations() {
        System.out.println("\n--- OPERATIONS BANCAIRES ---");
        System.out.println("1. Effectuer une operation");
        System.out.println("2. Consulter l'historique d'une carte");
        System.out.println("0. Retour");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                effectuerOperation();
                break;
            case 2:
                consulterHistorique();
                break;
            case 0:
                break;
            default:
                System.out.println("Choix invalide!");
        }
    }
    
    private void effectuerOperation() {
        try {
            System.out.print("ID de la carte: ");
            int idCarte = lireEntier();
            
            System.out.print("Montant: ");
            double montant = lireDouble();
            
            System.out.println("Type d'operation:");
            System.out.println("1. Achat");
            System.out.println("2. Retrait");
            System.out.println("3. Paiement en ligne");
            System.out.print("Votre choix: ");
            
            int type = lireEntier();
            TypeOperation typeOp = null;
            
            if (type == 1) {
                typeOp = TypeOperation.ACHAT;
            } else if (type == 2) {
                typeOp = TypeOperation.RETRAIT;
            } else if (type == 3) {
                typeOp = TypeOperation.PAIEMENTENLIGNE;
            }
            
            System.out.print("Lieu: ");
            String lieu = scanner.nextLine();
            
            OperationCarte operation = operationService.effectuerOperation(idCarte, montant, typeOp, lieu);
            System.out.println("Operation effectuee avec succes! ID: " + operation.getId());
            
            fraudeService.analyserFraude(idCarte);
            
        } catch (DatabaseException | OperationRefuseeException | CarteInvalideException | FraudeDetecteeException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void consulterHistorique() {
        try {
            System.out.print("ID de la carte: ");
            int id = lireEntier();
            
            List<OperationCarte> operations = operationService.obtenirOperationsParCarte(id);
            
            if (operations.isEmpty()) {
                System.out.println("Aucune operation trouvee");
            } else {
                System.out.println("\n--- HISTORIQUE DES OPERATIONS ---");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                for (OperationCarte op : operations) {
                    System.out.println("ID: " + op.getId() + 
                                     " | Date: " + op.getDateOperation().format(formatter) +
                                     " | Montant: " + op.getMontant() + " MAD" +
                                     " | Type: " + op.getType() +
                                     " | Lieu: " + op.getLieu());
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void menuFraude() {
        System.out.println("\n--- DETECTION DE FRAUDE ---");
        System.out.println("1. Analyser une carte");
        System.out.println("2. Consulter les alertes d'une carte");
        System.out.println("3. Lister toutes les alertes critiques");
        System.out.println("0. Retour");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                analyserCarte();
                break;
            case 2:
                consulterAlertes();
                break;
            case 3:
                listerAlertesCritiques();
                break;
            case 0:
                break;
            default:
                System.out.println("Choix invalide!");
        }
    }
    
    private void analyserCarte() {
        try {
            System.out.print("ID de la carte: ");
            int id = lireEntier();
            fraudeService.analyserFraude(id);
            System.out.println("Analyse terminee. Consultez les alertes pour plus de details.");
        } catch (DatabaseException | FraudeDetecteeException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void consulterAlertes() {
        try {
            System.out.print("ID de la carte: ");
            int id = lireEntier();
            
            List<AlerteFraude> alertes = fraudeService.obtenirAlertesParCarte(id);
            
            if (alertes.isEmpty()) {
                System.out.println("Aucune alerte trouvee");
            } else {
                System.out.println("\n--- ALERTES DE FRAUDE ---");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                for (AlerteFraude a : alertes) {
                    System.out.println("ID: " + a.getId() +
                                     " | Date: " + a.getDateAlerte().format(formatter) +
                                     " | Niveau: " + a.getNiveau() +
                                     " | Description: " + a.getDescription());
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void listerAlertesCritiques() {
        try {
            List<AlerteFraude> alertes = fraudeService.obtenirAlertesParNiveau(NiveauAlerte.CRITIQUE);
            
            if (alertes.isEmpty()) {
                System.out.println("Aucune alerte critique trouvee");
            } else {
                System.out.println("\n--- ALERTES CRITIQUES ---");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                for (AlerteFraude a : alertes) {
                    System.out.println("ID: " + a.getId() +
                                     " | Carte ID: " + a.getIdCarte() +
                                     " | Date: " + a.getDateAlerte().format(formatter) +
                                     " | Description: " + a.getDescription());
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void menuRapports() {
        System.out.println("\n--- RAPPORTS ET STATISTIQUES ---");
        System.out.println("1. Top 5 des cartes les plus utilisees");
        System.out.println("2. Statistiques mensuelles");
        System.out.println("3. Cartes bloquees");
        System.out.println("4. Cartes suspectes");
        System.out.println("0. Retour");
        System.out.print("Votre choix: ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                afficherTop5Cartes();
                break;
            case 2:
                afficherStatistiquesMensuelles();
                break;
            case 3:
                afficherCartesBloquees();
                break;
            case 4:
                afficherCartesSuspectes();
                break;
            case 0:
                break;
            default:
                System.out.println("Choix invalide!");
        }
    }
    
    private void afficherTop5Cartes() {
        try {
            List<Map.Entry<Integer, Long>> top5 = rapportService.obtenirTop5CartesUtilisees();
            
            if (top5.isEmpty()) {
                System.out.println("Aucune donnee disponible");
            } else {
                System.out.println("\n--- TOP 5 DES CARTES LES PLUS UTILISEES ---");
                int rang = 1;
                for (Map.Entry<Integer, Long> entry : top5) {
                    System.out.println(rang + ". Carte ID: " + entry.getKey() + 
                                     " - Nombre d'operations: " + entry.getValue());
                    rang++;
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void afficherStatistiquesMensuelles() {
        try {
            Map<TypeOperation, Long> stats = rapportService.obtenirStatistiquesMensuelles();
            Map<TypeOperation, Double> montants = rapportService.obtenirMontantsTotauxMensuels();
            
            System.out.println("\n--- STATISTIQUES DU MOIS EN COURS ---");
            System.out.println("ACHATS: " + stats.get(TypeOperation.ACHAT) + 
                             " operations - Montant total: " + montants.get(TypeOperation.ACHAT) + " MAD");
            System.out.println("RETRAITS: " + stats.get(TypeOperation.RETRAIT) + 
                             " operations - Montant total: " + montants.get(TypeOperation.RETRAIT) + " MAD");
            System.out.println("PAIEMENTS EN LIGNE: " + stats.get(TypeOperation.PAIEMENTENLIGNE) + 
                             " operations - Montant total: " + montants.get(TypeOperation.PAIEMENTENLIGNE) + " MAD");
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void afficherCartesBloquees() {
        try {
            List<Carte> cartes = rapportService.obtenirCartesBloquees();
            
            if (cartes.isEmpty()) {
                System.out.println("Aucune carte bloquee");
            } else {
                System.out.println("\n--- CARTES BLOQUEES ---");
                for (Carte c : cartes) {
                    System.out.println(c);
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private void afficherCartesSuspectes() {
        try {
            List<Carte> cartes = rapportService.obtenirCartesSuspectes();
            
            if (cartes.isEmpty()) {
                System.out.println("Aucune carte suspecte");
            } else {
                System.out.println("\n--- CARTES SUSPECTES ---");
                for (Carte c : cartes) {
                    System.out.println(c);
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    private int lireEntier() {
        try {
            int valeur = Integer.parseInt(scanner.nextLine());
            return valeur;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private double lireDouble() {
        try {
            double valeur = Double.parseDouble(scanner.nextLine());
            return valeur;
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
}
