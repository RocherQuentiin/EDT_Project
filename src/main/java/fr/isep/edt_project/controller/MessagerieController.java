package fr.isep.edt_project.controller;


import fr.isep.edt_project.model.Notification;
import fr.isep.edt_project.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.List;

public class MessagerieController extends Controller {

    @FXML
    private ListView<HBox> messageListView;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    @FXML
    private ComboBox<String> userComboBox;

    private final Utilisateur utilisateurModel = new Utilisateur(){};

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    public void initialize() {
        // Initialiser la liste des utilisateurs dans la ComboBox
        chargerUtilisateurs();

        // Charger les options de filtre
        filterComboBox.getItems().add("Tous les messages");
        filterComboBox.getItems().addAll(utilisateurModel.getLoginsNonAdministrateurs());

        // Détecter les changements dans le filtre
        filterComboBox.setOnAction(event -> filtrerMessages());

        // Charger les messages par défaut (Tous les messages)
        chargerMessages();

        // Associer une action au bouton "Envoyer"
        sendButton.setOnAction(event -> sendMessage());

        // Envoyer un message en appuyant sur "Entrée"
        messageInput.setOnAction(event -> sendMessage());
    }

    private void sendMessage() {
        // Vérifier si un interlocuteur est défini
        String selectedUser = userComboBox.getSelectionModel().getSelectedItem();
        String message = messageInput.getText();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner un utilisateur pour envoyer un message.");
            return;
        }

        if (!message.trim().isEmpty()) {
            // Ajouter le message dans la liste
            ajouterMessageBox(message, true, "Vous"); // Vous = l'expéditeur

            // Nettoyer le champ de saisie
            messageInput.clear();

            // Enregistrer le message dans la base de données
            int destinataireId;
            try {
                destinataireId = utilisateurModel.getIdParEmail(selectedUser);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de récupérer l'utilisateur.");
                return;
            }
            if (destinataireId != 0) {
                Notification.enregistrerMessage(message, destinataireId);
            }
        }
    }


    private void chargerUtilisateurs() {
        // Appeler le modèle pour récupérer les logins
        List<String> utilisateurs = utilisateurModel.getLoginsNonAdministrateurs();

        // Vérifiez si des utilisateurs ont été récupérés
        if (utilisateurs.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Aucun utilisateur n'a été trouvé");
        } else {
            // Ajouter les utilisateurs à la ComboBox
            userComboBox.getItems().addAll(utilisateurs);
        }
    }

    private void chargerMessages() {
        // Récupérer les messages depuis le modèle
        List<Notification> messages = Notification.getMessagesPourUtilisateur();

        // ID de l'utilisateur courant
        int utilisateurId = fr.isep.edt_project.Session.getUtilisateurCourant().getId();

        for (Notification message : messages) {
            boolean isSentByUser = message.getExpediteurId() == utilisateurId;

            // Récupérer le nom de l'expéditeur
            String senderName = utilisateurModel.getUserParId(message.getExpediteurId()).getNom();

            // Utiliser la méthode modifiée pour inclure le nom et la bande colorée
            ajouterMessageBox(message.getMessage(), isSentByUser, senderName);
        }
    }

    private void ajouterMessageBox(String texteMessage, boolean isSentByUser, String senderName) {
        // Conteneur général (HBox) pour le message
        HBox messageBox = new HBox();
        messageBox.setSpacing(10);

        // Bande colorée sur le côté pour indiquer l'origine
        Region colorBand = new Region();
        colorBand.setPrefWidth(10);
        colorBand.setPrefHeight(30); // Hauteur des messages
        colorBand.setStyle("-fx-background-color: " + (isSentByUser ? "blue" : "green") + ";");

        // Texte du message, incluant l'expéditeur
        Text msgText = new Text((isSentByUser ? "Vous" : senderName) + " : " + texteMessage);

        // Ajouter les éléments au conteneur en fonction de l'origine du message
        if (isSentByUser) {
            messageBox.getChildren().addAll(colorBand, msgText);
            messageBox.setAlignment(Pos.CENTER_LEFT); // Aligné à gauche pour les messages envoyés par l'utilisateur
        } else {
            messageBox.getChildren().addAll(msgText, colorBand);
            messageBox.setAlignment(Pos.CENTER_RIGHT); // Aligné à droite pour les messages reçus
        }

        // Ajouter le style au conteneur principal
        messageBox.setStyle("-fx-padding: 5; -fx-border-width: 1; -fx-border-color: lightgray; -fx-background-radius: 5;");

        // Ajouter l'élément à la liste des messages
        messageListView.getItems().add(messageBox);
    }

    private void filtrerMessages() {
        // Effacer les messages actuels
        messageListView.getItems().clear();

        // Récupérer la sélection du filtre
        String selectedFilter = filterComboBox.getSelectionModel().getSelectedItem();

        if (selectedFilter == null || selectedFilter.equals("Tous les messages")) {
            // Charger tous les messages
            chargerMessages();
            // Réinitialiser la combinaison des destinataires pour envoi
            userComboBox.getSelectionModel().clearSelection();
        } else {
            // Charger uniquement les messages échangés avec l'utilisateur sélectionné
            chargerMessagesAvecUtilisateur(selectedFilter);

            // Mettre à jour la sélection automatique du destinataire dans userComboBox
            userComboBox.getSelectionModel().select(selectedFilter);
        }
    }

    private void chargerMessagesAvecUtilisateur(String emailUtilisateur) {
        // Obtenir l'ID de l'utilisateur sélectionné
        int utilisateurCibleId;
        try {
            utilisateurCibleId = utilisateurModel.getIdParEmail(emailUtilisateur);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de récupérer l'utilisateur.");
            return;
        }

        // Récupérer les messages de la base
        List<Notification> messages = Notification.getMessagesPourUtilisateur();

        // ID de l'utilisateur courant
        int utilisateurId = fr.isep.edt_project.Session.getUtilisateurCourant().getId();

        // Filtrer les messages échangés seulement avec cet utilisateur
        for (Notification message : messages) {
            boolean isRelated = (message.getExpediteurId() == utilisateurId && message.getDestinataireId() == utilisateurCibleId) ||
                    (message.getExpediteurId() == utilisateurCibleId && message.getDestinataireId() == utilisateurId);

            if (isRelated) {
                boolean isSentByUser = message.getExpediteurId() == utilisateurId;

                // Ajouter le message à la vue
                String senderName = utilisateurModel.getUserByEmail(message.getExpediteurId()+"").getNom();
                ajouterMessageBox(message.getMessage(), isSentByUser, senderName);
            }
        }
    }



}