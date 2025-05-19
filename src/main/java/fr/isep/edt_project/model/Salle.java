package fr.isep.edt_project.model;

public class Salle {
    private int id;
    private String numeroSalle;
    private int capacite;
    private String localisation;

    public Salle(int id, String numeroSalle, int capacite, String localisation) {
        this.id = id;
        this.numeroSalle = numeroSalle;
        this.capacite = capacite;
        this.localisation = localisation;
    }

    public int getId() {
        return id;
    }

    public String getNumeroSalle() {
        return numeroSalle;
    }

    public int getCapacite() {
        return capacite;
    }

    public String getLocalisation() {
        return localisation;
    }

    public boolean estDisponible(Horaire h) { return true; }
    public String afficherInfos() { return numeroSalle + " (" + capacite + " places)"; }
    public boolean aConflict(Horaire h) { return false; }
}
