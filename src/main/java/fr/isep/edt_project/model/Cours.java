package fr.isep.edt_project.model;

import java.util.List;

public class Cours {
    private int id;
    private String nom;
    private Enseignant enseignant;
    private Salle salle;
    private Horaire horaire;
    private List<Etudiant> etudiants;

    public void ajouterEtudiant(Etudiant e) { etudiants.add(e); }
    public void supprimerEtudiant(Etudiant e) { etudiants.remove(e); }
}
