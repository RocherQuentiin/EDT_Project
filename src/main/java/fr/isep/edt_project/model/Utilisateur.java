package fr.isep.edt_project.model;

public abstract class Utilisateur {
    protected int id;
    protected String nom;
    protected String email;
    protected String motDePasse;

    public boolean seConnecter(String email, String motDePasse) {
        // ... logique de connexion ...
        return true;
    }

    public void seDeconnecter() {
        // ... logique de déconnexion ...
    }
}
