package fr.isep.edt_project.model;

import fr.isep.edt_project.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Notification {
    private int id;
    private String message;
    private int expediteurId;
    private int destinataireId;
    private String dateEnvoi;


    public void envoyer() { }
    public String afficher() { return ""; }

    public String getMessage() {
        return message;
    }

    public int getExpediteurId() {
        return expediteurId;
    }

    public Notification(int id, String message, int expediteurId, int destinataireId, String dateEnvoi) {
        this.id = id;
        this.message = message;
        this.expediteurId = expediteurId;
        this.destinataireId = destinataireId;
        this.dateEnvoi = dateEnvoi;
    }

    public static void enregistrerMessage(String message, int destinataire) {
        String query = "INSERT INTO Notification (message, destinataire_id, dateEnvoi, expediteur_id) VALUES (?, ?, ?, ?)";

        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();

             PreparedStatement statement = conn.prepareStatement(query);

            // Formater la date actuelle
            String dateEnvoi = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Paramètres de la requête
            statement.setString(1, message);
            statement.setInt(2, destinataire);
            statement.setString(3, dateEnvoi);
            statement.setString(4, Session.getUtilisateurCourant().getId() + "");

            // Exécution de la requête
            statement.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<Notification> getMessagesPourUtilisateur() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT id, message, expediteur_id, destinataire_id, dateEnvoi " +
                "FROM Notification " +
                "WHERE expediteur_id = ? OR destinataire_id = ? " +
                "ORDER BY dateEnvoi ASC";

        try{
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(query);

            // Obtention de l'utilisateur courant
            int utilisateurId = Session.getUtilisateurCourant().getId();

            // Définir les paramètres pour la requête
            statement.setInt(1, utilisateurId); // Messages envoyés
            statement.setInt(2, utilisateurId); // Messages reçus

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Créer une instance de Notification pour chaque message
                notifications.add(new Notification(
                        resultSet.getInt("id"),
                        resultSet.getString("message"),
                        resultSet.getInt("expediteur_id"),
                        resultSet.getInt("destinataire_id"),
                        resultSet.getString("dateEnvoi")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public int getDestinataireId() {
        return destinataireId;
    }
}
