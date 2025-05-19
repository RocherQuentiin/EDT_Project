package fr.isep.edt_project.model;

import java.util.List;
import java.util.stream.Collectors;

public class EmploiDuTemps {
    private Utilisateur utilisateur;
    private List<Cours> cours;

    public void ajouterCours(Cours c) { cours.add(c); }
    public void supprimerCours(Cours c) { cours.remove(c); }
    public String afficher() {
        return cours.stream().map(Cours::toString).collect(Collectors.joining("\n")); }
}
