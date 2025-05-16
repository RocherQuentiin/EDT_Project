package fr.isep.edt_project.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Equipement {
    private String nom;
    private String numeroEquipement;
    private String type;
    private int stock;

    public boolean ajouterEquipements(Connection conn) {
        String sql = "INSERT INTO equipements (nom, numero_equipement, type, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.nom);
            pstmt.setString(2, this.numeroEquipement);
            pstmt.setString(3, this.type);
            pstmt.setInt(4, this.stock);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean supprimerEquipements(Connection conn, int id) {
        String sql = "DELETE FROM equipements WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
