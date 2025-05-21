package fr.isep.edt_project.model;

import fr.isep.edt_project.bdd.DataBaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmploiDuTemps {

    private Utilisateur utilisateur;
    private List<Cours> cours;

    public EmploiDuTemps() {
        this.cours = new ArrayList<>();
    }

    public EmploiDuTemps(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.cours = new ArrayList<>();
    }

    public void ajouterCours(Cours c) {
        cours.add(c);
    }

    public void supprimerCours(Cours c) {
        cours.remove(c);
    }

    public String afficher() {
        return cours.stream()
                .map(Cours::toString)
                .collect(Collectors.joining("\n"));
    }

    // Permet de charger les cours d'un étudiant depuis la BDD
    public void chargerDepuisBDD(int utilisateurId) {
        String sql = "SELECT c.id AS cours_id, c.nom AS cours_nom, " +
                "h.date, h.heureDebut, h.heureFin, " +
                "s.numero_salle, s.localisation " +
                "FROM cours_etudiant ce " +
                "JOIN cours c ON ce.cours_id = c.id " +
                "JOIN horaire h ON c.horaire_id = h.id " +
                "JOIN salle s ON c.salle_id = s.id " +
                "WHERE ce.etudiant_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cours cours = new Cours();
                cours.setId(rs.getInt("cours_id"));
                cours.setNom(rs.getString("cours_nom"));

                Horaire horaire = new Horaire(
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("heureDebut").toLocalTime(),
                        rs.getTime("heureFin").toLocalTime()
                );

                cours.setHoraire(horaire);

                Salle salle = new Salle();
                salle.setNumeroSalle(rs.getString("numero_salle"));
                salle.setLocalisation(rs.getString("localisation"));
                cours.setSalle(salle);

                this.cours.add(cours);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // voici mes getters et setters :
    public List<Cours> getCours() {
        return cours;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setCours(List<Cours> cours) {
        this.cours = cours;
    }
}
