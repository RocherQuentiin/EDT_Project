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
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean seConnecter(String email, String motDePasse) {
        // Vérifier si l'utilisateur existe dans la base de données avec cet email et mot de passe
        // Exemple basique avec JDBC (à adapter selon votre gestion de la BDD)
        boolean isAuthenticated = false;
        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM utilisateurs WHERE email = ? AND mot_de_passe = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
            isAuthenticated = true;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAuthenticated;
    }

    public void seDeconnecter() {
        // ... logique de déconnexion ...
    }
}
