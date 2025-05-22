package fr.isep.edt_project.controller;

import com.calendarfx.view.CalendarView;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import fr.isep.edt_project.Session;
import fr.isep.edt_project.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import jdk.jshell.execution.Util;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class EmploiDuTempsController implements Initializable {

    @FXML
    private BorderPane rootPane;

    private CalendarView calendarView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendarView = new CalendarView();

        calendarView.setToday(LocalDate.now());
        calendarView.setTime(LocalTime.now());
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);

        CalendarSource source = new CalendarSource("EDT ISEP");

        // permet d'ajouter le calendrier des cours
        coursCalendar.setReadOnly(true); // on empêche l'ajout manuel car on ne le veut pas
        source.getCalendars().add(coursCalendar);
        calendarView.getCalendarSources().add(source);

        rootPane.setCenter(calendarView);

        calendarView.setDate(LocalDate.of(2025, 5, 21)); // <- La date à afficher
        calendarView.showWeekPage(); // <- Pour voir la semaine entière

        chargerCoursDepuisBDD();
    }

    private Calendar coursCalendar = new Calendar("Cours");

    private void chargerCoursDepuisBDD() {
        Etudiant etudiant = (Etudiant) Session.getUtilisateurCourant();
        System.out.println("ID de l'utilisateur connecté : " + etudiant.getId());


        for (Cours cours : Cours.getCoursByEtudiantId(etudiant.getId())) {
            System.out.println("Cours récupéré : " + cours.getNom() + " à " + cours.getHoraire().getDate());
            Horaire horaire = cours.getHoraire();
            Salle salle = cours.getSalle();

            System.out.println("Cours chargé : " + cours.getNom());
            System.out.println("Date : " + cours.getHoraire());
            System.out.println("Salle : " + cours.getSalle());


            Entry<String> entry = new Entry<>(cours.getNom());
            entry.setLocation(salle.getNumeroSalle() + " - " + salle.getLocalisation());
            entry.setInterval(
                    horaire.getDate().atTime(horaire.getHeureDebut()),
                    horaire.getDate().atTime(horaire.getHeureFin())
            );

            coursCalendar.addEntry(entry);
        }
    }

}
