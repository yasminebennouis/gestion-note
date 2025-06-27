package com.gestionnotes.service;

import com.gestionnotes.model.Etudiant;
import com.gestionnotes.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EtudiantService {

    private final Connection connection;

    public EtudiantService() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void ajouterEtudiant(Etudiant etu) {
        String sql = "INSERT INTO etudiants (nom, prenom, email, date_naissance, promotion_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, etu.getNom());
            stmt.setString(2, etu.getPrenom());
            stmt.setString(3, etu.getEmail());
            stmt.setDate(4, Date.valueOf(etu.getDateNaissance()));
            stmt.setInt(5, etu.getPromotionId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'étudiant", e);
        }
    }

    public void supprimerEtudiant(int id) {
        String sql = "DELETE FROM etudiants WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'étudiant", e);
        }
    }

    public List<Etudiant> getAll() {
        List<Etudiant> liste = new ArrayList<>();
        String sql = "SELECT * FROM etudiants";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Etudiant etu = new Etudiant();
                etu.setId(rs.getInt("id"));
                etu.setNom(rs.getString("nom"));
                etu.setPrenom(rs.getString("prenom"));
                etu.setEmail(rs.getString("email"));
                etu.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
                etu.setPromotionId(rs.getInt("promotion_id"));
                etu.setArchive(rs.getBoolean("archive"));
                liste.add(etu);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des étudiants", e);
        }

        return liste;
    }
}