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
        // Initialisation des colonnes de la table
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroEquipement"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Charger les données dans la table
        chargerEquipements();

        // Ajouter un écouteur pour les clics sur une ligne
        tableEquipements.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> remplirChampsAvecSelection(newValue)
        );
    }

    private void chargerEquipements() {
        // Récupérer les équipements depuis la base de données et remplir la liste observable
        List<Equipement> equipements = Equipement.getAllEquipements();
        if (equipements != null) {
            equipementList.setAll(equipements);
        }

        // Associer la liste au TableView
        tableEquipements.setItems(equipementList);
    }

    /**
     * Remplit les champs avec les données de l'équipement sélectionné dans la table.
     *
     * @param equipement L'équipement sélectionné.
     */
    private void remplirChampsAvecSelection(Equipement equipement) {
        if (equipement != null) {
            nomField.setText(equipement.getNom());
            numeroField.setText(equipement.getNumeroEquipement());
            typeField.setText(equipement.getType());
            stockField.setText(String.valueOf(equipement.getStock()));
        } else {
            // Réinitialiser les champs si aucune ligne n'est sélectionnée
            resetChamps();
        }
    }

    @FXML
    private void ajouterEquipement() {
        String nom = nomField.getText();
        String numero = numeroField.getText();
        String type = typeField.getText();
        int stock;

        try {
            stock = Integer.parseInt(stockField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un nombre valide pour le stock.");
            return;
        }

        Equipement equipement = new Equipement(nom, numero, type, stock);
        if (equipement.ajouterEquipements()) {
            equipementList.add(equipement);
            resetChamps();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement ajouté avec succès.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Échec", "Impossible d'ajouter l'équipement.");
        }
    }

    @FXML
    private void supprimerEquipement() {
        Equipement selectedEquipement = tableEquipements.getSelectionModel().getSelectedItem();
        if (selectedEquipement != null) {
            if (selectedEquipement.supprimerEquipements(selectedEquipement.getId())) {
                equipementList.remove(selectedEquipement);
                resetChamps();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement supprimé avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Échec", "Impossible de supprimer l'équipement.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un équipement à supprimer.");
        }
    }

    @FXML
    private void modifierEquipement() {
        Equipement equipement = tableEquipements.getSelectionModel().getSelectedItem();
        if (equipement != null) {
            equipement.setNom(nomField.getText());
            equipement.setNumeroEquipement(numeroField.getText());
            equipement.setType(typeField.getText());

            try {
                equipement.setStock(Integer.parseInt(stockField.getText()));
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez entrer un nombre valide pour le stock.");
                return;
            }

            // Rafraîchir la vue pour voir les modifications
            tableEquipements.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Équipement modifié avec succès.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un équipement à modifier.");
        }
    }

    private void resetChamps() {
        nomField.clear();
        numeroField.clear();
        typeField.clear();
        stockField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}