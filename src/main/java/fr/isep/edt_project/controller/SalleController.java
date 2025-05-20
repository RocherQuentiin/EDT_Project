package fr.isep.edt_project.controller;

import fr.isep.edt_project.model.Salle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class SalleController {

    @FXML
    private TableView<Salle> tableSalles;
    @FXML private TableColumn<Salle, String> colNumero;
    @FXML private TableColumn<Salle, Integer> colCapacite;
    @FXML private TableColumn<Salle, String> colLocalisation;

    @FXML private TextField numeroField;
    @FXML private TextField capaciteField;
    @FXML private TextField localisationField;

    private ObservableList<Salle> listeSalles = FXCollections.observableArrayList();

    // Initialisation du contrôleur
    @FXML
    public void initialize() {
        // Configuration des colonnes
        colNumero.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNumeroSalle()));
        colCapacite.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCapacite()).asObject());
        colLocalisation.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocalisation()));

        // Chargement initial des données
        chargerSalles();
    }

    private void chargerSalles() {
        List<Salle> salles = Salle.getAllSalles();
        listeSalles.setAll(salles);
        tableSalles.setItems(listeSalles);
    }

    @FXML
    public void ajouterSalle() {
        try {
            String numero = numeroField.getText();
            int capacite = Integer.parseInt(capaciteField.getText());
            String localisation = localisationField.getText();

            Salle nouvelleSalle = new Salle(numero, capacite, localisation);
            if (Salle.ajouterSalle(nouvelleSalle)) {
                chargerSalles();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salle ajoutée avec succès !");
                alert.showAndWait();
            }

            numeroField.clear();
            capaciteField.clear();
            localisationField.clear();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Capacité invalide. Veuillez entrer un nombre.");
            alert.showAndWait();
        }
    }

    @FXML
    public void supprimerSalle() {
        Salle salleSelectionnee = tableSalles.getSelectionModel().getSelectedItem();
        if (salleSelectionnee != null) {
            if (Salle.supprimerSalle(salleSelectionnee.getId())) {
                listeSalles.remove(salleSelectionnee);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salle supprimée avec succès !");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une salle à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    public void chargerSalleSelectionnee() {
        Salle salleSelectionnee = tableSalles.getSelectionModel().getSelectedItem();
        if (salleSelectionnee != null) {
            numeroField.setText(salleSelectionnee.getNumeroSalle());
            capaciteField.setText(String.valueOf(salleSelectionnee.getCapacite()));
            localisationField.setText(salleSelectionnee.getLocalisation());
        }
    }

    @FXML
    public void modifierSalle() {
        Salle salleSelectionnee = tableSalles.getSelectionModel().getSelectedItem();
        if (salleSelectionnee != null) {
            try {
                salleSelectionnee.setNumeroSalle(numeroField.getText());
                salleSelectionnee.setCapacite(Integer.parseInt(capaciteField.getText()));
                salleSelectionnee.setLocalisation(localisationField.getText());

                if (salleSelectionnee.modifierSalle()) {
                    chargerSalles();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salle modifiée avec succès !");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Capacité invalide. Veuillez entrer un nombre.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une salle à modifier.");
            alert.showAndWait();
        }
    }
}