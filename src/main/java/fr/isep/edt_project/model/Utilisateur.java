package fr.isep.edt_project.model;

public abstract class Utilisateur {
    protected int id;
    protected String nom;
    protected String email;
    protected String motDePasse;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getEmail() {
        return email;
    }

    public boolean seConnecter(String email, String motDePasse) {
        // ... logique de connexion ...
        return true;
    }

    public void seDeconnecter() {
        // ... logique de déconnexion ...
    }
}
