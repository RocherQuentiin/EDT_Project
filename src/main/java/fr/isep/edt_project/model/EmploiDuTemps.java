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

    public static int creerEmploiDuTempsPourEtudiant(int utilisateurId) {
        String sql = "INSERT INTO emploidutemps (utilisateur_id) VALUES (?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Affecter la valeur utilisateurId au paramètre
            preparedStatement.setInt(1, utilisateurId);
            System.out.println("preparedStatement : " + preparedStatement + " utilisateurId : " + utilisateurId + " connection : " + connection + " statement : " + sql + "");

            // Exécuter la requête
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Récupérer l'identifiant généré pour ce nouvel emploi du temps
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Retourne l'id généré
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Retourne -1 en cas d'échec de création
    }




    public static boolean ajouterCours(int currentEmploiDuTempsId, int cours_id, int utilisateurId) {
        if (currentEmploiDuTempsId == -1) {
            currentEmploiDuTempsId = creerEmploiDuTempsPourEtudiant(utilisateurId);
        }
        String sql = "INSERT INTO emploidutemps_cours (emploiDuTemps_id, cours_id) VALUES (?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentEmploiDuTempsId);
            stmt.setInt(2, cours_id);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Retourne vrai si une ligne est insérée
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // En cas d'exception, retourne faux
        }
    }

    public static List<Cours> recupererCoursParEmploiDuTemps(int currentEmploiDuTempsId) {
        List<Cours> coursList = new ArrayList<>();

        String sql = "SELECT c.id AS cours_id, c.nom AS cours_nom, " +
                "h.date AS horaire_date, h.heureDebut AS horaire_debut, h.heureFin AS horaire_fin, " +
                "s.numero_salle AS salle_numero, s.localisation AS salle_localisation, " +
                "u.id AS enseignant_id, u.nom AS enseignant_nom " +
                "FROM EmploiDuTemps_Cours etc " +
                "JOIN Cours c ON etc.cours_id = c.id " +
                "JOIN Horaire h ON c.horaire_id = h.id " +
                "JOIN Salle s ON c.salle_id = s.id " +
                "JOIN Utilisateur u ON c.enseignant_id = u.id " +
                "WHERE etc.emploiDuTemps_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, currentEmploiDuTempsId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Récupérer les informations du cours
                Cours cours = new Cours();
                cours.setId(rs.getInt("cours_id"));
                cours.setNom(rs.getString("cours_nom"));

                // Récupérer l'horaire associé
                Horaire horaire = new Horaire(
                        rs.getDate("horaire_date").toLocalDate(),
                        rs.getTime("horaire_debut").toLocalTime(),
                        rs.getTime("horaire_fin").toLocalTime()
                );
                cours.setHoraire(horaire);

                // Récupérer la salle associée
                Salle salle = new Salle();
                salle.setNumeroSalle(rs.getString("salle_numero"));
                salle.setLocalisation(rs.getString("salle_localisation"));
                cours.setSalle(salle);

                // Récupérer l'enseignant associé
                Enseignant enseignant = new Enseignant();
                enseignant.setId(rs.getInt("enseignant_id"));
                enseignant.setNom(rs.getString("enseignant_nom"));
                cours.setEnseignant(enseignant);

                // Ajouter le cours à la liste
                coursList.add(cours);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coursList;
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
    public static int recupererEmploiDuTempsIdParEtudiant(Integer etudiantId) {
        String sql = "SELECT emploiDuTemps_id FROM Etudiant WHERE id = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, etudiantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("emploiDuTemps_id"); // Retourne l'ID trouvé
            } else {
                System.out.println("Aucun emploi du temps trouvé pour l'étudiant avec l'ID : " + etudiantId);
                return -1; // Retourne -1 si aucune correspondance n'est trouvée
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Retourne -1 en cas d'erreur
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
