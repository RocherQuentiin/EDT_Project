package fr.isep.edt_project.model;

import java.time.LocalDateTime;

public class Horaire {
    private LocalDateTime date;
    private LocalDateTime heureDebut;
    private LocalDateTime heureFin;

    public boolean chevauche(Horaire autre) {
        return !(heureFin.isBefore(autre.heureDebut) || heureDebut.isAfter(autre.heureFin));
    }
}
