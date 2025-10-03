package models;

import java.time.LocalDate;

public class CarteDebit extends Carte {
    private double plafondJournalier;

    public CarteDebit() {
        super();
    }

    public CarteDebit(int id, String numero, LocalDate dateExpiration, StatutCarte statut, int idClient, double plafondJournalier) {
        super(id, numero, dateExpiration, statut, idClient);
        this.plafondJournalier = plafondJournalier;
    }

    public CarteDebit(String numero, LocalDate dateExpiration, StatutCarte statut, int idClient, double plafondJournalier) {
        super(numero, dateExpiration, statut, idClient);
        this.plafondJournalier = plafondJournalier;
    }

    public double getPlafondJournalier() {
        return plafondJournalier;
    }

    public void setPlafondJournalier(double plafondJournalier) {
        this.plafondJournalier = plafondJournalier;
    }

    @Override
    public TypeCarte getTypeCarte() {
        return TypeCarte.DEBIT;
    }

    @Override
    public String toString() {
        return "CarteDebit{" +
                "id=" + getId() +
                ", numero='" + getNumero() + '\'' +
                ", dateExpiration=" + getDateExpiration() +
                ", statut=" + getStatut() +
                ", plafondJournalier=" + plafondJournalier +
                ", idClient=" + getIdClient() +
                '}';
    }
}
