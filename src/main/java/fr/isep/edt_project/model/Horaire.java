package fr.isep.edt_project.model;

import fr.isep.edt_project.bdd.DataBaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Horaire {
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    public Horaire(LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public static int ajouterHoraire(Horaire horaire) {
        String query = "INSERT INTO Horaire (date, heureDebut, heureFin) VALUES (?, ?, ?)";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Définir les paramètres pour l'insertion
            preparedStatement.setDate(1, java.sql.Date.valueOf(horaire.getDate())); // Convertir LocalDate en SQL Date
            preparedStatement.setTime(2, java.sql.Time.valueOf(horaire.getHeureDebut())); // Convertir LocalTime en SQL Time
            preparedStatement.setTime(3, java.sql.Time.valueOf(horaire.getHeureFin()));

            // Exécuter la requête
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Récupérer l'identifiant généré
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Retourner l'identifiant généré
                    }
                }
            }
            return -1; // Aucun identifiant généré

        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Échec de l'insertion
        }
    }

    @Override
    public String toString() {
        return date + " " + heureDebut + " - " + heureFin;
    }
}