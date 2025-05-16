package fr.isep.edt_project.model;

import java.util.ArrayList;

public class Enseignant extends Utilisateur {
    private ArrayList<Cours> coursEnseignes;

    public void consulterEmploiDuTemps() { }
    public String infosCours() { return ""; }
    public boolean notifierAnomalie() { return false; }
}
