package models;

import java.time.LocalDateTime;

public class AlerteFraude {
    private final int id;
    private final String description;
    private final NiveauAlerte niveau;
    private final LocalDateTime dateAlerte;
    private final int idCarte;

    public AlerteFraude(int id, String description, NiveauAlerte niveau, LocalDateTime dateAlerte, int idCarte) {
        this.id = id;
        this.description = description;
        this.niveau = niveau;
        this.dateAlerte = dateAlerte;
        this.idCarte = idCarte;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public NiveauAlerte getNiveau() {
        return niveau;
    }

    public LocalDateTime getDateAlerte() {
        return dateAlerte;
    }

    public int getIdCarte() {
        return idCarte;
    }

    @Override
    public String toString() {
        return "AlerteFraude{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", niveau=" + niveau +
                ", dateAlerte=" + dateAlerte +
                ", idCarte=" + idCarte +
                '}';
    }
}
