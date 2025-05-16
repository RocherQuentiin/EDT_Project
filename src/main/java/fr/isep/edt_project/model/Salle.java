package fr.isep.edt_project.model;

public class Salle {
    private int id;
    private String numeroSalle;
    private int capacite;
    private String localisation;

    public boolean estDisponible() { return true; }
    public String afficherInfos() { return ""; }
    public boolean aConflict() { return false; }
}
