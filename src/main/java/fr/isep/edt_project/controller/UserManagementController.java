package fr.isep.edt_project.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.isep.edt_project.bdd.DataBaseConnection;
import fr.isep.edt_project.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserManagementController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Utilisateur> userTable;

    @FXML
    private TableColumn<Utilisateur, String> nameColumn;

    @FXML
    private TableColumn<Utilisateur, String> emailColumn;

    @FXML
    private TableColumn<Utilisateur, String> roleColumn;

    @FXML
    private TableColumn<Utilisateur, String> dateColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> roleComboBox;

    // Liste Observable des utilisateurs
    private ObservableList<Utilisateur> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration des colonnes pour la TableView
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Charger les données dans la TableView
        loadUsers();

        // Initialiser la ComboBox des rôles
        roleComboBox.setItems(FXCollections.observableArrayList("Administrateur", "Enseignant", "Etudiant"));

        // Sélection d'un utilisateur dans la TableView
        userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showUserDetails(newValue));
    }

    private void loadUsers() {
        userList.clear();
        String query = "SELECT u.id, u.nom, u.email, t.nom as role, u.date_inscription " +
                       "FROM Utilisateur u JOIN TypeUtilisateur t ON u.type_utilisateur_id = t.id";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Utilisateur user = new Utilisateur(){};
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userTable.setItems(userList);
    }

    @FXML
    private void onSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            userTable.setItems(userList);
        } else {
            ObservableList<Utilisateur> filteredList = FXCollections.observableArrayList();
            for (Utilisateur user : userList) {
                if (user.getNom().toLowerCase().contains(searchTerm) || user.getEmail().toLowerCase().contains(searchTerm)) {
                    filteredList.add(user);
                }
            }
            userTable.setItems(filteredList);
        }
    }

    @FXML
    private void onResetSearch() {
        searchField.clear();
        userTable.setItems(userList);
    }

    @FXML
    private void showUserDetails(Utilisateur user) {
        if (user != null) {
            nameField.setText(user.getNom());
            emailField.setText(user.getEmail());
            roleComboBox.setValue(user.getRole());
        }
    }

    @FXML
    private void onSaveChanges() {
        Utilisateur selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setNom(nameField.getText());
            selectedUser.setEmail(emailField.getText());
            selectedUser.setRole(roleComboBox.getValue());

            // Mise à jour dans la base de données
            String updateQuery = "UPDATE Utilisateur SET nom = ?, email = ?, type_utilisateur_id = " +
                                 "(SELECT id FROM TypeUtilisateur WHERE nom = ?) WHERE id = ?";
            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setString(1, selectedUser.getNom());
                statement.setString(2, selectedUser.getEmail());
                statement.setString(3, selectedUser.getRole());
                statement.setInt(4, selectedUser.getId());
                statement.executeUpdate();
                loadUsers(); // Rafraîchir la table
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}