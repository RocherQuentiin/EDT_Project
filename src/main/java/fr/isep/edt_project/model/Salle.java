package fr.isep.edt_project.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Salle {
    private int id;
    private String numeroSalle;
    private int capacite;
    private String localisation;

    public Salle(int id, String numeroSalle, int capacite, String localisation) {
        this.id = id;
        this.numeroSalle = numeroSalle;
        this.capacite = capacite;
        this.localisation = localisation;
    }

    public Salle(String numeroSalle, int capacite, String localisation) {
        this.numeroSalle = numeroSalle;
        this.capacite = capacite;
        this.localisation = localisation;
    }

    public Salle() {

    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public String getNumeroSalle() {
        return numeroSalle;
    }

    public int getCapacite() {
        return capacite;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setNumeroSalle(String nouveauNumero) {
        this.numeroSalle = nouveauNumero;
    }

    public void setCapacite(int nouvelleCapacite) {
        this.capacite = nouvelleCapacite;
    }

    public void setLocalisation(String nouvelleLocalisation) {
        this.localisation = nouvelleLocalisation;
    }

    // Méthode pour ajouter une salle à la base de données
    public static boolean ajouterSalle(Salle salle) {
        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            String query = "INSERT INTO Salle (numero_salle, capacite, localisation) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, salle.getNumeroSalle());
            statement.setInt(2, salle.getCapacite());
            statement.setString(3, salle.getLocalisation());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour récupérer toutes les salles de la base de données
    public static List<Salle> getAllSalles() {
        List<Salle> salles = new ArrayList<>();
        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            String query = "SELECT * FROM Salle";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                salles.add(new Salle(
                        resultSet.getInt("id"),
                        resultSet.getString("numero_salle"),
                        resultSet.getInt("capacite"),
                        resultSet.getString("localisation")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salles;
    }

    // Méthode pour modifier une salle dans la base de données
    public boolean modifierSalle() {
        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            String query = "UPDATE Salle SET numero_salle = ?, capacite = ?, localisation = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, this.numeroSalle);
            statement.setInt(2, this.capacite);
            statement.setString(3, this.localisation);
            statement.setInt(4, this.id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour supprimer une salle de la base de données
    public static boolean supprimerSalle(int salleId) {
        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            String query = "DELETE FROM Salle WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, salleId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setId(int salleId) {
        this.id = salleId;
    }
}