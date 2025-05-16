package fr.isep.edt_project.controller;

import java.io.IOException;

import fr.isep.edt_project.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        registerButton.setOnAction(event -> openRegisterWindow());
    }

    private void handleLogin() {
        String email = loginField.getText();
        String password = passwordField.getText();

        // Ici, tu peux instancier une classe concrète de Utilisateur (ex: Etudiant, Enseignant, etc.)
        // Exemple simple avec une classe anonyme pour la démo :
        Utilisateur utilisateur = new Utilisateur() {};

        boolean success = utilisateur.seConnecter(email, password);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue !");
            // TODO : Rediriger vers la page principale selon le type d'utilisateur
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Email ou mot de passe incorrect.");
        }
    }

    private void openRegisterWindow() {
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/isep/edt_project/register-view.fxml"));            stage.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'inscription !");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}