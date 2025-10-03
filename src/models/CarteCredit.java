package models;

import java.time.LocalDate;

public class CarteCredit extends Carte {
    private double plafondMensuel;
    private double tauxInteret;

    public CarteCredit() {
        super();
    }

    public CarteCredit(int id, String numero, LocalDate dateExpiration, StatutCarte statut, int idClient, double plafondMensuel, double tauxInteret) {
        super(id, numero, dateExpiration, statut, idClient);
        this.plafondMensuel = plafondMensuel;
        this.tauxInteret = tauxInteret;
    }

    public CarteCredit(String numero, LocalDate dateExpiration, StatutCarte statut, int idClient, double plafondMensuel, double tauxInteret) {
        super(numero, dateExpiration, statut, idClient);
        this.plafondMensuel = plafondMensuel;
        this.tauxInteret = tauxInteret;
    }

    public double getPlafondMensuel() {
        return plafondMensuel;
    }

    public void setPlafondMensuel(double plafondMensuel) {
        this.plafondMensuel = plafondMensuel;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    @Override
    public TypeCarte getTypeCarte() {
        return TypeCarte.CREDIT;
    }

    @Override
    public String toString() {
        return "CarteCredit{" +
                "id=" + getId() +
                ", numero='" + getNumero() + '\'' +
                ", dateExpiration=" + getDateExpiration() +
                ", statut=" + getStatut() +
                ", plafondMensuel=" + plafondMensuel +
                ", tauxInteret=" + tauxInteret +
                ", idClient=" + getIdClient() +
                '}';
    }
}
