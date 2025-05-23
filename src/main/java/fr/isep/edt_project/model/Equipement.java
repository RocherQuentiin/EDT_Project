package fr.isep.edt_project.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Equipement {
    private int id;
    private String nom;
    private String numeroEquipement;
    private String type;
    private int stock;

    public Equipement(String nom, String numeroEquipement, String type, int stock) {
        this.nom = nom;
        this.numeroEquipement = numeroEquipement;
        this.type = type;
        this.stock = stock;
    }

    public Equipement(int id, String nom, String numeroEquipement, String type, int stock) {
        this.id = id;
        this.nom = nom;
        this.numeroEquipement = numeroEquipement;
        this.type = type;
        this.stock = stock;
    }

    public static List<Equipement> getAllEquipements() {
        String sql = "SELECT * FROM equipement";
        try {
            java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            List<Equipement> allEquipements = new ArrayList<>();
            while (rs.next()) {
                Equipement equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("numero_equipement"),
                        rs.getString("type"),
                        rs.getInt("stock")
                );
                allEquipements.add(equipement);
            }
            return allEquipements;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean ajouterEquipements() {
        String sql = "INSERT INTO equipement (nom, numero_equipement, type, stock) VALUES (?, ?, ?, ?)";
        try (
                java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
    public boolean supprimerEquipements(int id) {
        String sql = "DELETE FROM equipement WHERE id = ?";
        try (java.sql.Connection conn = fr.isep.edt_project.bdd.DataBaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public String getStringId(){
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumeroEquipement() {
        return numeroEquipement;
    }

    public void setNumeroEquipement(String numeroEquipement) {
        this.numeroEquipement = numeroEquipement;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStock() {
        return stock;
    }

    public String getStockString() {
        return String.valueOf(stock);
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    @Override
    public String toString() {
        return nom;
    }
}
