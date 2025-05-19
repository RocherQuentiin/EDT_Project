package fr.isep.edt_project.controller;

import fr.isep.edt_project.model.Cours;
import fr.isep.edt_project.model.Enseignant;
import fr.isep.edt_project.model.Salle;
import fr.isep.edt_project.model.Horaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class CoursController extends Controller {

    @FXML
    private TableView<Cours> tableCours;

    @FXML
    private TableColumn<Cours, String> colNomCours;

    @FXML
    private TableColumn<Cours, String> colEnseignant;

    @FXML
    private TableColumn<Cours, String> colSalle;

    @FXML
    private TableColumn<Cours, String> colHoraire;

    @FXML
    private TextField nomField;

    @FXML
    private ComboBox<Enseignant> enseignantBox;

    @FXML
    private ComboBox<Salle> salleBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField heureDebutField;

    @FXML
    private TextField heureFinField;

    private ObservableList<Cours> coursList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Lier les colonnes à leurs propriétés
        colNomCours.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEnseignant.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEnseignant().getNom()));
        colSalle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSalle().getNumeroSalle()));
        colHoraire.setCellValueFactory(data -> {
            Horaire horaire = data.getValue().getHoraire();
            return new javafx.beans.property.SimpleStringProperty(horaire.getDate() + " " + horaire.getHeureDebut() + " - " + horaire.getHeureFin());
        });

        // Charger les listes pour les enseignants et les salles
        chargerListes();

        // Charger les cours dans la table
        chargerCours();
    }

    private void chargerListes() {
        List<Enseignant> enseignants = Enseignant.getAllEnseignants(); // Implémentez cette méthode dans le modèle
        List<Salle> salles = Salle.getAllSalles(); // Implémentez cette méthode dans le modèle

        enseignantBox.setItems(FXCollections.observableArrayList(enseignants));
        salleBox.setItems(FXCollections.observableArrayList(salles));
    }

    private void chargerCours() {
        List<Cours> cours = Cours.getAllCours();
        coursList.setAll(cours);
        tableCours.setItems(coursList);
    }

    @FXML
    public void ajouterCours() {
        try {
            // Récupérer les données saisies
            String nom = nomField.getText();
            Enseignant enseignant = enseignantBox.getValue();
            Salle salle = salleBox.getValue();
            LocalDate date = datePicker.getValue();
            LocalTime heureDebut = LocalTime.parse(heureDebutField.getText());
            LocalTime heureFin = LocalTime.parse(heureFinField.getText());

            // Valider les champs
            if (nom.isEmpty() || enseignant == null || salle == null || date == null || heureDebut == null || heureFin == null) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez remplir tous les champs correctement");
                return;
            }

            if (heureDebut.isAfter(heureFin)) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "L'heure de début doit être avant l'heure de fin");
                return;
            }

            // Créer un objet Horaire
            Horaire horaire = new Horaire(date, heureDebut, heureFin);

            // Créer un nouveau cours
            Cours nouveauCours = new Cours(0, nom, enseignant, salle, horaire, null);

            // Ajouter le cours en base de données
            if (Cours.ajouterCoursAvecHoraire(nouveauCours)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Cours ajouté avec succès");
                chargerCours();
                resetChamps();
            } else {
                showAlert( Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du cours");
            }
        } catch (Exception e) {
            showAlert( Alert.AlertType.ERROR, "Erreur", "Format invalide pour les heures (HH:mm)");
        }
    }

    private void resetChamps() {
        nomField.clear();
        enseignantBox.setValue(null);
        salleBox.setValue(null);
        datePicker.setValue(null);
        heureDebutField.clear();
        heureFinField.clear();
    }

    @FXML
    public void modifierCours() {
        Cours coursSelectionne = tableCours.getSelectionModel().getSelectedItem();

        if (coursSelectionne == null) {
            showAlert( Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un cours à modifier.");
            return;
        }

        try {
            // Récupérer les nouvelles valeurs saisies
            String nom = nomField.getText();
            Enseignant enseignant = enseignantBox.getValue();
            Salle salle = salleBox.getValue();
            LocalDate date = datePicker.getValue();
            LocalTime heureDebut = LocalTime.parse(heureDebutField.getText());
            LocalTime heureFin = LocalTime.parse(heureFinField.getText());

            // Valider les champs
            if (nom.isEmpty() || enseignant == null || salle == null || date == null || heureDebut == null || heureFin == null) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez remplir tous les champs correctement.");
                return;
            }

            if (heureDebut.isAfter(heureFin)) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "L'heure de début doit être avant l'heure de fin.");
                return;
            }

            // Mettre à jour les valeurs de l'objet cours sélectionné
            coursSelectionne.setNom(nom);
            coursSelectionne.setEnseignant(enseignant);
            coursSelectionne.setSalle(salle);
            coursSelectionne.setHoraire(new Horaire(date, heureDebut, heureFin));

            // Appeler la méthode de mise à jour
            if (Cours.modifierCours(coursSelectionne)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Cours modifié avec succès.");
                chargerCours(); // Recharger les données dans la table
                resetChamps();
            } else {
                showAlert( Alert.AlertType.ERROR, "Erreur", "La modification du cours a échoué.");
            }

        } catch (Exception e) {
            showAlert( Alert.AlertType.ERROR, "Erreur", "Format invalide pour les heures (HH:mm).");
        }
    }

    @FXML
    public void supprimerCours() {
        Cours coursSelectionne = tableCours.getSelectionModel().getSelectedItem();

        if (coursSelectionne == null) {
            showAlert(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner un cours à supprimer.");
            return;
        }

        // Confirmation de la suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer le cours : " + coursSelectionne.getNom() + " ?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Appeler la méthode de suppression
            if (Cours.supprimerCours(coursSelectionne.getId())) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Cours supprimé avec succès.");
                chargerCours(); // Recharger les données dans la table
            } else {
                showAlert( Alert.AlertType.ERROR, "Erreur", "La suppression du cours a échoué.");
            }
        }
    }


}