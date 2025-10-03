package models;

import java.time.LocalDate;

public abstract class Carte {
    private int id;
    private String numero;
    private LocalDate dateExpiration;
    private StatutCarte statut;
    private int idClient;

    public Carte() {
    }

    public Carte(int id, String numero, LocalDate dateExpiration, StatutCarte statut, int idClient) {
        this.id = id;
        this.numero = numero;
        this.dateExpiration = dateExpiration;
        this.statut = statut;
        this.idClient = idClient;
    }

    public Carte(String numero, LocalDate dateExpiration, StatutCarte statut, int idClient) {
        this.numero = numero;
        this.dateExpiration = dateExpiration;
        this.statut = statut;
        this.idClient = idClient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public StatutCarte getStatut() {
        return statut;
    }

    public void setStatut(StatutCarte statut) {
        this.statut = statut;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public abstract TypeCarte getTypeCarte();

    @Override
    public String toString() {
        return "Carte{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", dateExpiration=" + dateExpiration +
                ", statut=" + statut +
                ", idClient=" + idClient +
                '}';
    }
}
