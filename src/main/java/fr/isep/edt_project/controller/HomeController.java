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
    private StackPane centerPane;

    @FXML
    private MenuItem profileButton;

    @FXML
    private MenuItem logoutButton;

    @FXML
    public void initialize() {
        profileButton.setOnAction(event -> showProfileView());
        logoutButton.setOnAction(event -> logout());
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
}