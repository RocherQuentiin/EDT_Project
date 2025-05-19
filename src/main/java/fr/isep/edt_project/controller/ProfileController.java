package fr.isep.edt_project.controller;

import fr.isep.edt_project.Session;
import fr.isep.edt_project.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ProfileController extends Controller{
    @FXML
    public TextField nameField;

    @FXML
    public PasswordField checkPasswordField;

    @FXML
    public Button backButton;

    @FXML
    public PasswordField oldPasswordField;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button editProfilButton;


    @FXML
    private void initialize() {
        editProfilButton.setOnAction(event -> handleEditProfil());
        backButton.setOnAction(event -> handleBack2Home());
        Utilisateur currentUser = Session.getUtilisateurCourant();
        nameField.setText(currentUser.getNom());
        loginField.setText(currentUser.getEmail());
    }

    private void handleBack2Home() {
        if (backButton.getScene() != null && backButton.getScene().getRoot() != null) {
            // Retire ce panneau du parent (StackPane)
            ((javafx.scene.layout.Pane) backButton.getScene().getRoot().lookup("#centerPane")).getChildren().clear();
        }
    }

    private void handleEditProfil() {
        String nom = nameField.getText();
        String email = loginField.getText();
        String oldPassword = oldPasswordField.getText();
        String newPassword = passwordField.getText();
        String checkPassword = checkPasswordField.getText();

        Utilisateur currentUser = Session.getUtilisateurCourant();

        // Vérification des champs obligatoires
        if (nom.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (!(oldPassword.isEmpty() || newPassword.isEmpty() || checkPassword.isEmpty())) {
            // Vérification de l'ancien mot de passe
            if (!currentUser.getMotDePasse().equals(oldPassword)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'ancien mot de passe est incorrect.");
                return;
            }

            // Vérification de la correspondance des nouveaux mots de passe
            if (!newPassword.equals(checkPassword)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les nouveaux mots de passe ne correspondent pas.");
                return;
            }
            try (java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection()) {
                String sql = "UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ? WHERE id = ?";
                try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nom);
                    stmt.setString(2, email);
                    stmt.setString(3, newPassword);
                    stmt.setInt(4, currentUser.getId());
                    stmt.executeUpdate();
                }
                // Mettre à jour l'utilisateur courant en session
                currentUser.setNom(nom);
                currentUser.setEmail(email);
                currentUser.setMotDePasse(newPassword);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil modifié avec succès !");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de modifier le profil : " + e.getMessage());
            }
        } else {
            try (java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection()) {
                String sql = "UPDATE utilisateur SET nom = ?, email = ? WHERE id = ?";
                try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nom);
                    stmt.setString(2, email);
                    stmt.setInt(3, currentUser.getId());
                    stmt.executeUpdate();
                }
                // Mettre à jour l'utilisateur courant en session
                currentUser.setNom(nom);
                currentUser.setEmail(email);
                currentUser.setMotDePasse(newPassword);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil modifié avec succès !");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de modifier le profil : " + e.getMessage());
            }
        }


    }
}
