package models;

import java.time.LocalDate;

public class CartePrepayee extends Carte {
    private double soldeDisponible;

    public CartePrepayee() {
        super();
    }

    public CartePrepayee(int id, String numero, LocalDate dateExpiration, StatutCarte statut, int idClient, double soldeDisponible) {
        super(id, numero, dateExpiration, statut, idClient);
        this.soldeDisponible = soldeDisponible;
    }

    public CartePrepayee(String numero, LocalDate dateExpiration, StatutCarte statut, int idClient, double soldeDisponible) {
        super(numero, dateExpiration, statut, idClient);
        this.soldeDisponible = soldeDisponible;
    }

    public double getSoldeDisponible() {
        return soldeDisponible;
    }

    public void setSoldeDisponible(double soldeDisponible) {
        this.soldeDisponible = soldeDisponible;
    }

    @Override
    public TypeCarte getTypeCarte() {
        return TypeCarte.PREPAYEE;
    }

    @Override
    public String toString() {
        return "CartePrepayee{" +
                "id=" + getId() +
                ", numero='" + getNumero() + '\'' +
                ", dateExpiration=" + getDateExpiration() +
                ", statut=" + getStatut() +
                ", soldeDisponible=" + soldeDisponible +
                ", idClient=" + getIdClient() +
                '}';
    }
}
