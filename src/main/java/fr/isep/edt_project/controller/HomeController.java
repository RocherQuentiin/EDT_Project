package fr.isep.edt_project.controller;

import fr.isep.edt_project.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController extends Controller{

    @FXML
    public MenuItem salleToolButton;

    @FXML
    public MenuItem userManagemntButton;

    @FXML
    private StackPane centerPane;

    @FXML
    private MenuItem profileButton;

    @FXML
    private MenuItem logoutButton;

    @FXML
    private MenuItem messagingToolButton;

    @FXML
    public MenuItem coursToolButton;

    @FXML
    public void initialize() {
        profileButton.setOnAction(event -> showProfileView());
        logoutButton.setOnAction(event -> logout());
        messagingToolButton.setOnAction(event -> openMessagingTool());
        salleToolButton.setOnAction(event -> openSalleManagementTool());
        coursToolButton.setOnAction(event -> openCoursManagementTool());
        userManagemntButton.setOnAction(event -> openUserManagementTool());

        // Rendre accessibles les outils qu'aux administrateurs
        if (Session.getUtilisateurCourant().getNiveau().equals("1")) { // Niveau administrateur
            salleToolButton.setVisible(true);
            coursToolButton.setVisible(true);
            userManagemntButton.setVisible(true);
        }
    }

    private void openCoursManagementTool() {
        try {
            Node coursView = FXMLLoader.load(getClass().getResource("/fr/isep/edt_project/cours-view.fxml"));
            centerPane.getChildren().setAll(coursView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showProfileView() {
        try {
            Node profileView = FXMLLoader.load(getClass().getResource("/fr/isep/edt_project/profile-view.fxml"));
            centerPane.getChildren().setAll(profileView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout(){
        Session.clear();
        Stage stage = (Stage) logoutButton.getParentPopup().getOwnerWindow();
        try {
            changeScene(stage,"/fr/isep/edt_project/login-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openMessagingTool() {
        try {
            // Charger le fichier FXML de l'outil de messagerie
            Node messagingView = FXMLLoader.load(getClass().getResource("/fr/isep/edt_project/messagerie-view.fxml"));

            centerPane.getChildren().setAll(messagingView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openSalleManagementTool() {
        try {
            Node salleView = FXMLLoader.load(getClass().getResource("/fr/isep/edt_project/salle-view.fxml"));
            centerPane.getChildren().setAll(salleView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUserManagementTool() {
        try {
            Node userManagementView = FXMLLoader.load(getClass().getResource("/fr/isep/edt_project/user-management-view.fxml"));
            centerPane.getChildren().setAll(userManagementView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}