package fr.isep.edt_project.model;

import fr.isep.edt_project.Session;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {
    private String message;
    private String destinataire;
    private String dateEnvoi;

    public void envoyer() { }
    public String afficher() { return ""; }

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
}
