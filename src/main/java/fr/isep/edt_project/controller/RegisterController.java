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

public class RegisterController extends Controller {

    public PasswordField checkPasswordField;
    @FXML
    public TextField nameField;
    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    protected Utilisateur utilisateur = new Utilisateur() {};

    @FXML
    private void initialize() {
        registerButton.setOnAction(event -> handleRegister());
        backButton.setOnAction(event -> goBackToAuth());
    }

    private void handleRegister() {
    String name = nameField.getText();
    String login = loginField.getText();
    String password = passwordField.getText();

    if (login.isEmpty() || password.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez remplir tous les champs !");
        return;
    }
        boolean isInscrit = utilisateur.inscription(name, login, password);
        if (isInscrit) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès !");
            goBackToAuth();
        }
        else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'enregistrer le compte dans la base de données !" );
        }
    }

    private void goBackToAuth() {
        try {
            Utilisateur utilisateur = new Utilisateur() {};
            utilisateur = utilisateur.getUserByEmail(loginField.getText());
            Session.setUtilisateurCourant(utilisateur);
            Stage stage = (Stage) backButton.getScene().getWindow();
            changeScene(stage, "/fr/isep/edt_project/home-view.fxml");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de revenir à la fenêtre d'authentification !");
        }
    }

}