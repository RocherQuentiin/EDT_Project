package fr.isep.edt_project;

import fr.isep.edt_project.model.Utilisateur;

public class Session {
    private static Utilisateur utilisateurCourant;

    public static Utilisateur getUtilisateurCourant() {
        return utilisateurCourant;
    }

    public static void setUtilisateurCourant(Utilisateur utilisateur) {
        utilisateurCourant = utilisateur;
    }

    public static void clear() {
        utilisateurCourant = null;
    }
}
