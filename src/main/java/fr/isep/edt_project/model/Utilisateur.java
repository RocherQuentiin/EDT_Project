package fr.isep.edt_project.model;

import fr.isep.edt_project.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Utilisateur {
    protected int id;
    protected String nom;
    protected String email;
    protected String motDePasse;
    protected Integer niveau;

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
            String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ? AND mot_de_passe = ?";
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

    public boolean inscription(String nom, String email, String motDePasse) {
        // Vérifier si l'utilisateur existe déjà avant l'inscription
        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            String checkSql = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
            java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            java.sql.ResultSet rs = checkStmt.executeQuery();
            boolean exists = false;
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
            rs.close();
            checkStmt.close();

            if (!exists) {
                String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe, type_utilisateur_id) VALUES (?, ?, ?, 3)";
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nom);
                stmt.setString(2, email);
                stmt.setString(3, motDePasse);

                stmt.executeUpdate();
                stmt.close();
                conn.close();
                return true;
            } else {
                System.out.println("Un utilisateur avec cet email existe déjà.");
                conn.close();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Utilisateur getUserByEmail(String email){
        try{
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            String sql = "SELECT * FROM utilisateur WHERE email = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                this.setEmail(email);
                this.setNom(rs.getString("nom"));
                this.motDePasse = rs.getString("mot_de_passe");
                this.id = rs.getInt("id");
                rs.close();
                stmt.close();
                conn.close();
                return this;
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }
    public void seDeconnecter() {
        // ... logique de déconnexion ...
    }

    public String getNiveau() {
        String niveau = null;
        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            String sql = "SELECT type_utilisateur_id FROM utilisateur WHERE id = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.id);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                niveau = rs.getString("type_utilisateur_id");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public List<String> getLoginsNonAdministrateurs() {
        List<String> logins = new ArrayList<>();

        String query = "SELECT email FROM utilisateur " +
                "WHERE type_utilisateur_id IS NOT NULL " +
                "AND type_utilisateur_id != (SELECT id FROM typeutilisateur WHERE nom = 'Administrateur')";

        try (
                java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Ajouter le login (email) à la liste
                logins.add(resultSet.getString("email"));
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logins;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                '}';
    }
}
