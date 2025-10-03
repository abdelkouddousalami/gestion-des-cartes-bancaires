package models;

import java.time.LocalDateTime;

public class OperationCarte {
    private final int id;
    private final LocalDateTime dateOperation;
    private final double montant;
    private final TypeOperation type;
    private final String lieu;
    private final int idCarte;

    public OperationCarte(int id, LocalDateTime dateOperation, double montant, TypeOperation type, String lieu, int idCarte) {
        this.id = id;
        this.dateOperation = dateOperation;
        this.montant = montant;
        this.type = type;
        this.lieu = lieu;
        this.idCarte = idCarte;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDateOperation() {
        return dateOperation;
    }

    public double getMontant() {
        return montant;
    }

    public TypeOperation getType() {
        return type;
    }

    public String getLieu() {
        return lieu;
    }

    public int getIdCarte() {
        return idCarte;
    }

    @Override
    public String toString() {
        return "OperationCarte{" +
                "id=" + id +
                ", dateOperation=" + dateOperation +
                ", montant=" + montant +
                ", type=" + type +
                ", lieu='" + lieu + '\'' +
                ", idCarte=" + idCarte +
                '}';
    }
}
