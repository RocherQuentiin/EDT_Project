package fr.isep.edt_project.model;

import fr.isep.edt_project.bdd.DataBaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Cours {
    private int id;
    private String nom;
    private Enseignant enseignant;
    private Salle salle;
    private Horaire horaire;
    private List<Etudiant> etudiants;

    // Constructeur
    public Cours(int id, String nom, Enseignant enseignant, Salle salle, Horaire horaire, List<Etudiant> etudiants) {
        this.id = id;
        this.nom = nom;
        this.enseignant = enseignant;
        this.salle = salle;
        this.horaire = horaire;
        this.etudiants = etudiants;
    }

    // Getters et setters
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

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Horaire getHoraire() {
        return horaire;
    }
    public void setHoraire(Horaire horaire) {
        this.horaire = horaire;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    // Ajouter un cours et créer automatiquement l'horaire associé
    public static boolean ajouterCoursAvecHoraire(Cours cours) {
        // Étape 1 : Ajouter l'horaire à la table "Horaire"
        int horaireId = Horaire.ajouterHoraire(cours.getHoraire());

        if (horaireId == -1) {
            System.err.println("Erreur : L'ajout de l'horaire a échoué !");
            return false; // Si l'insertion de l'horaire a échoué, arrêter ici.
        }

        String query = "INSERT INTO Cours (nom, enseignant_id, salle_id, horaire_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Définir les paramètres pour l'insertion
            preparedStatement.setString(1, cours.getNom());
            preparedStatement.setInt(2, cours.getEnseignant().getId());
            preparedStatement.setInt(3, cours.getSalle().getId());
            preparedStatement.setInt(4, horaireId); // Utiliser l'ID de l'horaire inséré

            // Exécuter la requête
            return preparedStatement.executeUpdate() > 0; // Retourner vrai si une ligne a été insérée

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Échec de l'insertion
        }
    }

    // Récupérer tous les cours de la base
    public static List<Cours> getAllCours() {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT c.id AS coursId, c.nom AS coursNom, " +
                "h.date AS horaireDate, h.heureDebut AS horaireHeureDebut, h.heureFin AS horaireHeureFin, " +
                "u.id AS enseignantId, u.nom AS enseignantNom, " +
                "s.id AS salleId, s.numero_salle AS salleNumero " +
                "FROM Cours c " +
                "JOIN Horaire h ON c.horaire_id = h.id " +
                "JOIN Salle s ON c.salle_id = s.id " +
                "JOIN Utilisateur u ON c.enseignant_id = u.id " +
                "WHERE u.type_utilisateur_id = 2";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // Récupérer les données du cours
                int coursId = resultSet.getInt("coursId");
                String coursNom = resultSet.getString("coursNom");

                // Récupérer les informations de l'horaire
                LocalDate date = resultSet.getDate("horaireDate").toLocalDate();
                LocalTime heureDebut = resultSet.getTime("horaireHeureDebut").toLocalTime();
                LocalTime heureFin = resultSet.getTime("horaireHeureFin").toLocalTime();
                Horaire horaire = new Horaire(date, heureDebut, heureFin);

                // Récupérer les informations de l'enseignant
                Enseignant enseignant = new Enseignant();
                enseignant.setId(resultSet.getInt("enseignantId"));
                enseignant.setNom(resultSet.getString("enseignantNom"));

                // Récupérer les informations de la salle
                Salle salle = new Salle();
                salle.setId(resultSet.getInt("salleId"));
                salle.setNumeroSalle(resultSet.getString("salleNumero"));

                // Créer un objet Cours et l'ajouter à la liste
                Cours cours = new Cours(coursId, coursNom, enseignant, salle, horaire, null);
                coursList.add(cours);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coursList;
    }

    // Mettre à jour un cours
    public static boolean modifierCours(Cours cours) {
        String updateCoursQuery = "UPDATE Cours SET nom = ?, enseignant_id = ?, salle_id = ? WHERE id = ?";
        String updateHoraireQuery = "UPDATE Horaire SET date = ?, heureDebut = ?, heureFin = ? WHERE cours_id = ?";
        try (Connection connection = DataBaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction
            try (PreparedStatement updateCoursStmt = connection.prepareStatement(updateCoursQuery);
                 PreparedStatement updateHoraireStmt = connection.prepareStatement(updateHoraireQuery)) {
                // Update Cours table
                updateCoursStmt.setString(1, cours.getNom());
                updateCoursStmt.setInt(2, cours.getEnseignant().getId());
                updateCoursStmt.setInt(3, cours.getSalle().getId());
                updateCoursStmt.setInt(4, cours.getId());
                updateCoursStmt.executeUpdate();
                // Update Horaire table
                updateHoraireStmt.setDate(1, Date.valueOf(cours.getHoraire().getDate()));
                updateHoraireStmt.setTime(2, Time.valueOf(cours.getHoraire().getHeureDebut()));
                updateHoraireStmt.setTime(3, Time.valueOf(cours.getHoraire().getHeureFin()));
                updateHoraireStmt.setInt(4, cours.getId());
                updateHoraireStmt.executeUpdate();
                connection.commit(); // Commit transaction
                return true;
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on error
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un cours
    public static boolean supprimerCours(int coursId) {
        String query = "DELETE FROM Cours WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, coursId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}