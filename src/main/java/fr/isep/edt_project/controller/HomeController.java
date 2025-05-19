package fr.isep.edt_project.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class HomeController {

    @FXML
    private StackPane centerPane;

    @FXML
    private Button profileButton;

    @FXML
    public void initialize() {
        profileButton.setOnAction(event -> showProfileView());
    }

    private void showProfileView() {
        try {
            Node profileView = FXMLLoader.load(getClass().getResource("/fr/isep/edt_project/profile-view.fxml"));
            centerPane.getChildren().setAll(profileView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}