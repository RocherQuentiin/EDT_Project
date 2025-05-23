package fr.isep.edt_project.controller;

import fr.isep.edt_project.model.Cours;
import fr.isep.edt_project.model.EmploiDuTemps;
import fr.isep.edt_project.model.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class EmploiDuTempsManagementController extends Controller {

    @FXML
    private ComboBox<Cours> coursComboBox;

    @FXML
    private ComboBox<Integer> etudiantComboBox;

    @FXML
    private TableView<Cours> emploiDuTempsTable;

    private int currentEmploiDuTempsId;

    @FXML
    public void initialize() {
        // Initialiser les colonnes du tableau
        TableColumn<Cours, String> nomColumn = new TableColumn<>("Cours");
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emploiDuTempsTable.getColumns().add(nomColumn);

        // Charger les données dans les combo boxes
        chargerCoursDisponibles();
        chargerEtudiantsDisponibles();

        // Charger la table par défaut
        etudiantComboBox.setOnAction(event -> chargerEmploiDuTemps());
    }

    private void chargerCoursDisponibles() {
        List<Cours> coursList = Cours.getAllCours();
        ObservableList<Cours> observableCours = FXCollections.observableArrayList(coursList);
        coursComboBox.setItems(observableCours);
    }

    private void chargerEtudiantsDisponibles() {
        Etudiant etudiant = new Etudiant();
        List<Integer> etudiantsIds = etudiant.getIdsNonAdministrateurs(); // Implémentez cette méthode dans une classe DAO
        ObservableList<Integer> observableEtudiants = FXCollections.observableArrayList(etudiantsIds);
        etudiantComboBox.setItems(observableEtudiants);
    }

    @FXML
    private void ajouterCours() {
        Cours coursSelectionne = coursComboBox.getSelectionModel().getSelectedItem();
        Integer etudiantId = etudiantComboBox.getSelectionModel().getSelectedItem();

        if (coursSelectionne != null && etudiantId != null) {
            boolean success = EmploiDuTemps.ajouterCours(currentEmploiDuTempsId, coursSelectionne.getId(), etudiantId);
            if (success) {
                chargerEmploiDuTemps();
            } else {
                System.out.println("Erreur : Impossible d'ajouter le cours.");
            }
        }
    }

    private void chargerEmploiDuTemps() {
        Integer etudiantId = etudiantComboBox.getSelectionModel().getSelectedItem();
        if (etudiantId != null) {
            currentEmploiDuTempsId = EmploiDuTemps.recupererEmploiDuTempsIdParEtudiant(etudiantId); // Implémentez cette méthode
            List<Cours> coursList = EmploiDuTemps.recupererCoursParEmploiDuTemps(currentEmploiDuTempsId);
            ObservableList<Cours> observableCours = FXCollections.observableArrayList(coursList);
            System.out.println(observableCours.toString());
            emploiDuTempsTable.setItems(observableCours);
        }
    }



    public void retour(ActionEvent actionEvent) {

    }
}