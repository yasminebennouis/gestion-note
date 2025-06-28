package com.gestionnotes.service;

import com.gestionnotes.model.Promotion;
import com.gestionnotes.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionService {

    private final Connection connection;

    public PromotionService() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void ajouterPromotion(Promotion promo) {
        String sql = "INSERT INTO promotions (nom, annee, specialite) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, promo.getNom());
            stmt.setString(2, promo.getAnnee());
            stmt.setString(3, promo.getSpecialite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout", e);
        }
    }

    public void supprimerPromotion(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM promotions WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }

    public List<Promotion> getAll() {
        List<Promotion> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM promotions");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Promotion p = new Promotion();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setAnnee(rs.getString("annee"));
                p.setSpecialite(rs.getString("specialite"));
                p.setActive(rs.getBoolean("active"));
                list.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lecture promotions", e);
        }
        return list;
    }
}