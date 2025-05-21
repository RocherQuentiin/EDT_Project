package fr.isep.edt_project.controller;

import fr.isep.edt_project.model.Equipement;
import fr.isep.edt_project.model.EquipementSalle;
import fr.isep.edt_project.model.Salle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class EquipementSalleController extends Controller {

    @FXML
    private TableView<Equipement> equipementTable;

    @FXML
    private TableColumn<Equipement, String> colEquipementNom;

    @FXML
    private TableColumn<Equipement, String> colEquipementType;

    @FXML
    private TableColumn<Equipement, String> colEquipementStock;

    @FXML
    private ComboBox<Salle> salleComboBox;

    @FXML
    private ComboBox<Equipement> equipementComboBox;

    @FXML
    private Button addEquipementButton;

    @FXML
    private Button deleteEquipementButton;

    private ObservableList<Equipement> equipementList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer les colonnes de la table
        colEquipementNom.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getNom());
        });
        colEquipementType.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getType());
        });
        colEquipementStock.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getStockString());
        });

        // Charger les salles et équipements disponibles
        chargerSalles();
        chargerEquipements();

        // Listener sur la sélection d'une salle
        salleComboBox.setOnAction(event -> chargerEquipementsSalle());
        addEquipementButton.setOnAction(event -> ajouterEquipement());
        deleteEquipementButton.setOnAction(event -> supprimerEquipement());
    }

    private void chargerSalles() {
        List<Salle> salles = Salle.getAllSalles();
        ObservableList<Salle> salleObservableList = FXCollections.observableArrayList(salles);
        salleComboBox.setItems(salleObservableList);

        if (!salleObservableList.isEmpty()) {
            salleComboBox.getSelectionModel().selectFirst();
            chargerEquipementsSalle(); // Charger les équipements de la première salle par défaut
        } else {
            System.out.println("Aucune salle disponible !");
        }
    }

    private void chargerEquipements() {
        List<Equipement> equipements = Equipement.getAllEquipements();
        equipementComboBox.setItems(FXCollections.observableArrayList(equipements));
    }

    private void chargerEquipementsSalle() {
        Salle selectedSalle = salleComboBox.getValue();
        if (selectedSalle != null) {
            List<Equipement> equipements = EquipementSalle.getEquipementsParSalle(selectedSalle.getId());
            equipementList.setAll(equipements);
            equipementTable.setItems(equipementList);
        } else {
            System.out.println("Aucune salle sélectionnée !");
            equipementTable.getItems().clear(); // Efface les lignes si aucune salle n'est sélectionnée
        }
    }

    @FXML
    private void ajouterEquipement() {
        Salle selectedSalle = salleComboBox.getValue();
        Equipement selectedEquipement = equipementComboBox.getValue();

        if (selectedSalle != null && selectedEquipement != null) {
            boolean success = EquipementSalle.ajouterEquipementASalle(selectedSalle.getId(), selectedEquipement.getId());
            if (success) {
                chargerEquipementsSalle();
            }
        }
    }

    @FXML
    private void supprimerEquipement() {
        Salle selectedSalle = salleComboBox.getValue();
        Equipement selectedEquipement = equipementTable.getSelectionModel().getSelectedItem();

        if (selectedSalle != null && selectedEquipement != null) {
            boolean success = EquipementSalle.supprimerEquipementDeSalle(selectedSalle.getId(), selectedEquipement.getId());
            if (success) {
                chargerEquipementsSalle();
            }
        }
    }
}