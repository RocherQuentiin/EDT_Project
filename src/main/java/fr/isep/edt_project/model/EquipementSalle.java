package fr.isep.edt_project.model;

import fr.isep.edt_project.bdd.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipementSalle {
    private Equipement equipement;
    private Salle salle;

    public EquipementSalle(Equipement equipement, Salle salle) {
        this.equipement = equipement;
        this.salle = salle;
    }

    public Equipement getEquipement() {
        return equipement;
    }

    public Salle getSalle() {
        return salle;
    }

    // Récupérer la liste des équipements associés à une salle
    public static List<Equipement> getEquipementsParSalle(int salleId) {
        List<Equipement> equipements = new ArrayList<>();
        String query = "SELECT e.id, e.nom, e.numero_equipement, e.type, e.stock " +
                        "FROM Salle_Equipement se " +
                        "JOIN Equipement e ON se.equipement_id = e.id " +
                        "WHERE se.salle_id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, salleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                equipements.add(new Equipement(
                        rs.getString("nom"),
                        rs.getString("numero_equipement"),
                        rs.getString("type"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return equipements;
    }

    // Ajouter un équipement à une salle
    public static boolean ajouterEquipementASalle(int salleId, int equipementId) {
        String query = "INSERT INTO Salle_Equipement (salle_id, equipement_id) VALUES (?, ?)";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, salleId);
            ps.setInt(2, equipementId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un équipement d'une salle
    public static boolean supprimerEquipementDeSalle(int salleId, int equipementId) {
        String query = "DELETE FROM Salle_Equipement WHERE salle_id = ? AND equipement_id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, salleId);
            ps.setInt(2, equipementId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}