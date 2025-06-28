package com.gestionnotes.service;

import com.gestionnotes.model.SousModule;
import com.gestionnotes.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SousModuleService {

    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public void ajouter(SousModule sm) {
        String sql = "INSERT INTO sous_modules (nom, coefficient, module_id, enseignant_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sm.getNom());
            stmt.setDouble(2, sm.getCoefficient());
            stmt.setInt(3, sm.getModuleId());
            stmt.setInt(4, sm.getEnseignantId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du sous-module", e);
        }
    }

    public void supprimer(int id) {
        String sql = "DELETE FROM sous_modules WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du sous-module", e);
        }
    }

    public List<SousModule> getAll() {
        List<SousModule> liste = new ArrayList<>();
        String sql = "SELECT * FROM sous_modules";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SousModule sm = new SousModule();
                sm.setId(rs.getInt("id"));
                sm.setNom(rs.getString("nom"));
                sm.setCoefficient(rs.getDouble("coefficient"));
                sm.setModuleId(rs.getInt("module_id"));
                sm.setEnseignantId(rs.getInt("enseignant_id"));
                liste.add(sm);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des sous-modules", e);
        }
        return liste;
    }
}
