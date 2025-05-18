package fr.isep.edt_project.controller;

import java.io.IOException;

import fr.isep.edt_project.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
    public static void changeScene(Stage stage, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}
