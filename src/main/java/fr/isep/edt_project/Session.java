package fr.isep.edt_project;

import fr.isep.edt_project.model.Utilisateur;

public class Session {
    private static Utilisateur utilisateurCourant;

    public static Utilisateur getUtilisateurCourant() {
        return utilisateurCourant;
    }

    public static void setUtilisateurCourant(Utilisateur utilisateur) {
        String niveau = utilisateur.getNiveau();
        if (niveau==null || niveau.isEmpty()) {
            utilisateur.setNiveau(3);
        }
        utilisateurCourant = utilisateur;
    }

    public static void clear() {
        utilisateurCourant = null;
    }
}
