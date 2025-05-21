package fr.isep.edt_project.controller;

import java.io.IOException;

import fr.isep.edt_project.Session;
import fr.isep.edt_project.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController extends Controller{

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

        Utilisateur utilisateur = Utilisateur.getUserByEmail(email);

        if (utilisateur != null && utilisateur.seConnecter(email, password)) {
            Session.setUtilisateurCourant(utilisateur);

            System.out.println("Utilisateur courant = " + utilisateur.getNom());

            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + utilisateur.getNom() + "!");

            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                HomeController controller = changeSceneAndGetController(
                        stage,
                        "/fr/isep/edt_project/home-view.fxml",
                        1000, 700,
                        "Accueil"
                );
                controller.showCalendarView(); // Affiche le calendrier

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de revenir à la fenêtre d'authentification !");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Email ou mot de passe incorrect.");
        }
    }

    private void openRegisterWindow() {
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            changeScene(stage, "/fr/isep/edt_project/register-view.fxml");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'inscription !");
        }
    }

}