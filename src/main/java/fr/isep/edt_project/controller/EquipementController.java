package fr.isep.edt_project.controller;

import fr.isep.edt_project.model.Equipement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class EquipementController {

    @FXML
    private TableView<Equipement> tableEquipements;
    @FXML
    private TableColumn<Equipement, String> colNom;
    @FXML
    private TableColumn<Equipement, String> colNumero;
    @FXML
    private TableColumn<Equipement, String> colType;
    @FXML
    private TableColumn<Equipement, Integer> colStock;

    @FXML
    private TextField nomField;
    @FXML
    private TextField numeroField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField stockField;

    private ObservableList<Equipement> equipementList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Liaison des colonnes avec les attributs de l'équipement
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroEquipement"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        chargerEquipements();
    }

    private void chargerEquipements() {
        // Charger la liste des équipements depuis la base de données
        List<Equipement> equipements = Equipement.getAllEquipements();
        equipementList.setAll(equipements);
        tableEquipements.setItems(equipementList);
    }

    @FXML
    private void ajouterEquipement() {
        // Ajouter un nouvel équipement
        String nom = nomField.getText();
        String numero = numeroField.getText();
        String type = typeField.getText();
        int stock;

        try {
            stock = Integer.parseInt(stockField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le stock doit être un nombre entier.");
            return;
        }

        Equipement equipement = new Equipement(nom, numero, type, stock);
        if (equipement.ajouterEquipements()) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement ajouté avec succès.");
            chargerEquipements();
            resetChamps();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter l'équipement.");
        }
    }

    @FXML
    private void supprimerEquipement() {
        // Supprimer l'équipement sélectionné
        Equipement equipement = tableEquipements.getSelectionModel().getSelectedItem();

        if (equipement == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un équipement à supprimer.");
            return;
        }

        if (equipement.supprimerEquipements(equipement.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement supprimé avec succès.");
            chargerEquipements();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer l'équipement.");
        }
    }

    private void resetChamps() {
        nomField.clear();
        numeroField.clear();
        typeField.clear();
        stockField.clear();
    }

    @FXML
    private void modifierEquipement() {
        // Modifier l'équipement sélectionné
        Equipement equipement = tableEquipements.getSelectionModel().getSelectedItem();

        if (equipement == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un équipement à modifier.");
            return;
        }

        String nom = nomField.getText();
        String numero = numeroField.getText();
        String type = typeField.getText();
        int stock;

        try {
            stock = Integer.parseInt(stockField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le stock doit être un nombre entier.");
            return;
        }

        equipement.setNom(nom);
        equipement.setNumeroEquipement(numero);
        equipement.setType(type);
        equipement.setStock(stock);

        if (equipement.ajouterEquipements()) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement modifié avec succès.");
            chargerEquipements();
            resetChamps();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de modifier l'équipement.");
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