package fr.isep.edt_project.controller;

import fr.isep.edt_project.model.Salle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SalleController {

    @FXML
    private TableView<Salle> tableSalles;
    @FXML private TableColumn<Salle, Integer> colId;
    @FXML private TableColumn<Salle, String> colNumero;
    @FXML private TableColumn<Salle, Integer> colCapacite;
    @FXML private TableColumn<Salle, String> colLocalisation;

    @FXML private TextField numeroField;
    @FXML private TextField capaciteField;
    @FXML private TextField localisationField;

    private ObservableList<Salle> listeSalles = FXCollections.observableArrayList();
    private int compteurId = 1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colNumero.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNumeroSalle()));
        colCapacite.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCapacite()).asObject());
        colLocalisation.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocalisation()));

        tableSalles.setItems(listeSalles);
    }

    @FXML
    public void ajouterSalle() {
        try {
            String numero = numeroField.getText();
            int capacite = Integer.parseInt(capaciteField.getText());
            String localisation = localisationField.getText();

            Salle nouvelleSalle = new Salle(compteurId++, numero, capacite, localisation);
            listeSalles.add(nouvelleSalle);

            numeroField.clear();
            capaciteField.clear();
            localisationField.clear();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Capacité invalide (doit être un nombre)");
            alert.showAndWait();
        }
    }
}
