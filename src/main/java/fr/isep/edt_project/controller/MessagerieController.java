package fr.isep.edt_project.controller;


import fr.isep.edt_project.model.Notification;
import fr.isep.edt_project.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class MessagerieController {

    @FXML
    private ListView<String> messageListView;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    @FXML
    private ComboBox<String> userComboBox;

    private final Utilisateur utilisateurModel = new Utilisateur(){};


    @FXML
    public void initialize() {
        // Initialiser la liste des utilisateurs dans la ComboBox
        chargerUtilisateurs();

        // Associer une action au bouton "Envoyer"
        sendButton.setOnAction(event -> sendMessage());

        // Optionnel : Envoyer un message en appuyant sur "Entrée"
        messageInput.setOnAction(event -> sendMessage());
    }

    private void sendMessage() {
        String selectedUser = userComboBox.getSelectionModel().getSelectedItem();
        String message = messageInput.getText();

        if (selectedUser == null) {
            messageListView.getItems().add("Erreur : Veuillez sélectionner un utilisateur !");
            return;
        }

        if (!message.trim().isEmpty()) {
            // Ajouter le message dans la liste
            messageListView.getItems().add("Vous à " + selectedUser + " : " + message);

            // Nettoyer le champ de saisie
            messageInput.clear();

            // Enregistrer le message dans la base de données
            int destinataireId = 0;
            try {
                destinataireId = utilisateurModel.getIdParEmail(selectedUser);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (destinataireId != 0) {
                Notification.enregistrerMessage(message, destinataireId);
            }

            // Simuler une réponse pour l'interface
            simulateReply(selectedUser);
        }
    }


    /**
     * Simuler une réponse de l'utilisateur sélectionné
     */
    private void simulateReply(String selectedUser) {
        // Ajouter une réponse après un court délai
        new Thread(() -> {
            try {
                // Attente de 1 seconde
                Thread.sleep(1000);

                // Ajouter une réponse fictive (à exécuter sur le thread de l'interface)
                javafx.application.Platform.runLater(() ->
                        messageListView.getItems().add(selectedUser + ": Merci pour votre message !")
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void chargerUtilisateurs() {
        // Appeler le modèle pour récupérer les logins
        List<String> utilisateurs = utilisateurModel.getLoginsNonAdministrateurs();

        // Vérifiez si des utilisateurs ont été récupérés
        if (utilisateurs.isEmpty()) {
            messageListView.getItems().add("Aucun utilisateur trouvé !");
        } else {
            // Ajouter les utilisateurs à la ComboBox
            userComboBox.getItems().addAll(utilisateurs);
        }
    }

}