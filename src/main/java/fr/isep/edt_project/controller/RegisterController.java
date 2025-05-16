package fr.isep.edt_project.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

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

    private final String CSV_FILE = "users.csv";
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

    try (java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection()) {
        // Vérifier si l'utilisateur existe déjà
        String checkSql = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        try (java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, login);
            try (java.sql.ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Un utilisateur avec cet email existe déjà !");
                    return;
                }
            }
        }

        // Insérer le nouvel utilisateur
        String insertSql = "INSERT INTO utilisateur (nom, email, mot_de_passe) VALUES (?, ?, ?)";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setString(1, name); // Ici, login comme nom pour l'exemple
            stmt.setString(2, login); // login = email
            stmt.setString(3, password);
            stmt.executeUpdate();
        }

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès !");
        goBackToAuth();
    } catch (Exception e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'enregistrer le compte dans la base de données !" + e.getMessage());
    }
}

    private void goBackToAuth() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/isep/edt_project/login-view.fxml"));
            stage.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de revenir à la fenêtre d'authentification !");
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