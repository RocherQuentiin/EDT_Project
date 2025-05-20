package fr.isep.edt_project.controller;

import com.calendarfx.view.CalendarView;
import com.calendarfx.model.CalendarSource;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

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
        calendarView.getCalendarSources().add(source);

        rootPane.setCenter(calendarView);
    }
}
