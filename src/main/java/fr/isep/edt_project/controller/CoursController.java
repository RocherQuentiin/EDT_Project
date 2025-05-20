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
        // Initialise les colonnes
        colNomCours.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEnseignant.setCellValueFactory(new PropertyValueFactory<>("enseignant"));
        colSalle.setCellValueFactory(new PropertyValueFactory<>("salle"));
        colHoraire.setCellValueFactory(new PropertyValueFactory<>("horaire"));

        // Charge les données dans la table
        chargerListes();
        chargerCours();

        // Ajoute un listener pour remplir les champs lors de la sélection
        tableCours.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                remplirChamps(newValue);
            }
        });
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
        // Récupère le cours sélectionné
        Cours coursSelectionne = tableCours.getSelectionModel().getSelectedItem();

        if (coursSelectionne != null) {
            // Met à jour les données du cours sélectionné
            coursSelectionne.setNom(nomField.getText());
            coursSelectionne.setEnseignant(enseignantBox.getValue());
            coursSelectionne.setSalle(salleBox.getValue());

            // Met à jour l'horaire si les champs sont remplis
            if (datePicker.getValue() != null && !heureDebutField.getText().isEmpty() && !heureFinField.getText().isEmpty()) {
                Horaire horaire = new Horaire(
                        datePicker.getValue(),
                        LocalTime.parse(heureDebutField.getText()),
                        LocalTime.parse(heureFinField.getText())
                );
                coursSelectionne.setHoraire(horaire);
            }

            // Sauvegarde les modifications en base de données
            boolean success = Cours.modifierCours(coursSelectionne);

            if (success) {
                // Recharge la table après modification
                chargerCours();
                resetChamps();
            } else {
                // Gérer les erreurs (par exemple, afficher une alerte pour l'utilisateur)
                System.err.println("Erreur lors de la mise à jour du cours.");
            }
        } else {
            System.err.println("Aucun cours sélectionné !");
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

    private void remplirChamps(Cours cours) {
        // Remplir le champ du nom
        nomField.setText(cours.getNom());

        // Sélectionner l'enseignant dans la ComboBox
        enseignantBox.getSelectionModel().select(cours.getEnseignant());

        // Sélectionner la salle dans la ComboBox
        salleBox.getSelectionModel().select(cours.getSalle());

        // Remplir la DatePicker avec la date associée à l'horaire du cours
        if (cours.getHoraire() != null) {
            datePicker.setValue(cours.getHoraire().getDate());
            heureDebutField.setText(cours.getHoraire().getHeureDebut().toString());
            heureFinField.setText(cours.getHoraire().getHeureFin().toString());
        } else {
            datePicker.setValue(null);
            heureDebutField.clear();
            heureFinField.clear();
        }
    }


}