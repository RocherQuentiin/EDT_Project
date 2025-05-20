package fr.isep.edt_project.controller;

import java.io.IOException;

import fr.isep.edt_project.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Controller {

    public static void changeScene(Stage stage, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    // Ma méthode générique qui retourne le contrôleur pour ma implémenter mon calendrier
    protected <T> T changeSceneAndGetController(Stage stage, String fxmlPath, int width, int height, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), width, height);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        return loader.getController();
    }

    void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
