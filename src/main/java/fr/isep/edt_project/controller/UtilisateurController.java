package fr.isep.edt_project.controller;

import fr.isep.edt_project.model.Utilisateur;

public class UtilisateurController {
    private Utilisateur utilisateur;

    public UtilisateurController(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public boolean login(String email, String motDePasse) {
        return utilisateur.seConnecter(email, motDePasse);
    }

    public void logout() {
        utilisateur.seDeconnecter();
    }
}
