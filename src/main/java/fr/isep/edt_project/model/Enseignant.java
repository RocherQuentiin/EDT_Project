package fr.isep.edt_project.model;

import fr.isep.edt_project.bdd.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Enseignant extends Utilisateur {
    private ArrayList<Cours> coursEnseignes;

    public static List<Enseignant> getAllEnseignants() {
        List<Enseignant> enseignants = new ArrayList<>();
        String query = "SELECT u.id, u.nom, u.email, u.date_inscription " +
                "FROM Utilisateur u " +
                "JOIN TypeUtilisateur t ON u.type_utilisateur_id = t.id " +
                "JOIN Enseignant e ON e.id = u.id " +
                "WHERE t.nom = 'Enseignant'";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Enseignant enseignant = new Enseignant();
                enseignant.setId(resultSet.getInt("id"));
                enseignant.setNom(resultSet.getString("nom"));
                enseignant.setEmail(resultSet.getString("email"));
                enseignant.setNiveau(2); // Niveau 2 = Enseignant
                enseignants.add(enseignant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enseignants;
    }

    public void consulterEmploiDuTemps() {
        // Implémentation spécifique
    }

    public String infosCours() {
        return "";
    }

    public boolean notifierAnomalie() {
        return false;
    }
}