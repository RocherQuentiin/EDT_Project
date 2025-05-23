package fr.isep.edt_project.model;

import fr.isep.edt_project.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import fr.isep.edt_project.bdd.DataBaseConnection;

public abstract class Utilisateur {
    protected int id;
    protected String nom;
    protected String email;
    protected String motDePasse;
    protected Integer niveau;
    protected LocalDateTime dateInscription;

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

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

    public static Utilisateur getUserByEmail(String email) {
        Utilisateur utilisateur = null;

        String sql = "SELECT * FROM utilisateur WHERE email = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // On regarde si c’est un étudiant ou enseignant via type_utilisateur_id
                int typeId = rs.getInt("type_utilisateur_id");
                if (typeId == 3) {
                    utilisateur = new Etudiant();
                } else if (typeId == 2) {
                    utilisateur = new Enseignant();
                } else {
                    utilisateur = new Utilisateur() {};
                }

                utilisateur.setId(rs.getInt("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                utilisateur.setNiveau(typeId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }


    public int getIdParEmail(String email) throws SQLException {
        String query = "SELECT id FROM Utilisateur WHERE email = ?";

        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query);

            // Paramètre de la requête
            statement.setString(1, email);

            // Exécution de la requête
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Retourne -1 si aucun utilisateur ne correspond
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

    public List<Integer> getIdsNonAdministrateurs() {
        List<Integer> ids = new ArrayList<>();
        String query = "SELECT id FROM utilisateur " +
                "WHERE type_utilisateur_id IS NOT NULL " +
                "AND type_utilisateur_id != (SELECT id FROM typeutilisateur WHERE nom = 'Administrateur')";
        try (
                java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public Utilisateur getUserParId(int id) {
        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            String sql = "SELECT * FROM utilisateur WHERE id = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.setId(id);
                this.setNom(rs.getString("nom"));
                this.setEmail(rs.getString("email"));
                this.motDePasse = rs.getString("mot_de_passe");
                this.niveau = rs.getInt("type_utilisateur_id");
                rs.close();
                stmt.close();
                conn.close();
                return this;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found or an exception occurs
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

    public void setRole(String role) {
        if (role.equals("Administrateur")) {
            this.niveau = 1;
        }
        else if (role.equals("Enseignant")) {
            this.niveau = 2;
        }
        else {
            this.niveau = 3;
        }
    }

    public String getRole() {
        if (this.niveau == 1) {
            return "Administrateur";
        }
        else if (this.niveau == 2) {
            return "Enseignant";
        }
        else {
            return "Etudiant";
        }
    }
}
