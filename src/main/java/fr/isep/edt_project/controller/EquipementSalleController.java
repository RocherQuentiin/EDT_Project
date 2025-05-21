package fr.isep.edt_project.controller;

import fr.isep.edt_project.model.Equipement;
import fr.isep.edt_project.model.EquipementSalle;
import fr.isep.edt_project.model.Salle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class EquipementSalleController extends Controller {

    // FXML Elements
    @FXML
    private TableView<Equipement> equipementTable;

    @FXML
    private TableColumn<Equipement, String> colEquipementNom;

    @FXML
    private TableColumn<Equipement, String> colEquipementType;

    @FXML
    private TableColumn<Equipement, Integer> colEquipementStock;

    @FXML
    private ComboBox<Salle> salleComboBox;

    @FXML
    private ComboBox<Equipement> equipementComboBox;

    @FXML
    private Button addEquipementButton;

    @FXML
    private Button deleteEquipementButton;

    // Liste Observable pour les équipements
    private ObservableList<Equipement> equipementList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer les colonnes de la table
        colEquipementNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEquipementType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colEquipementStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Charger les salles et équipements disponibles
        chargerSalles();
        chargerEquipements();

        // Listener sur la sélection d'une salle
        salleComboBox.setOnAction(event -> chargerEquipementsSalle());
    }

    private void chargerSalles() {
        // Remplir la liste des salles dans la ComboBox
        List<Salle> salles = Salle.getAllSalles();
        salleComboBox.setItems(FXCollections.observableArrayList(salles));
    }

    private void chargerEquipements() {
        // Charger les équipements dans la ComboBox
        List<Equipement> equipements = Equipement.getAllEquipements();
        System.out.println(equipements);
        equipementComboBox.setItems(FXCollections.observableArrayList(equipements));
    }

    private void chargerEquipementsSalle() {
        Salle selectedSalle = salleComboBox.getValue();
        if (selectedSalle != null) {
            List<Equipement> equipements = EquipementSalle.getEquipementsParSalle(selectedSalle.getId());
            equipementList.setAll(equipements);
            equipementTable.setItems(equipementList);
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