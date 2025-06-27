package com.gestionnotes.service;

import com.gestionnotes.model.SousModule;
import com.gestionnotes.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SousModuleService {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public void ajouter(SousModule s) {
        String sql = "INSERT INTO sous_modules (nom, coefficient, module_id, enseignant_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, s.getNom());
            stmt.setDouble(2, s.getCoefficient());
            stmt.setInt(3, s.getModuleId());
            stmt.setInt(4, s.getEnseignantId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur ajout sous-module", e);
        }
    }

    public void supprimer(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM sous_modules WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression sous-module", e);
        }
    }

    public List<SousModule> getTous() {
        List<SousModule> liste = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM sous_modules")) {
            while (rs.next()) {
                SousModule s = new SousModule();
                s.setId(rs.getInt("id"));
                s.setNom(rs.getString("nom"));
                s.setCoefficient(rs.getDouble("coefficient"));
                s.setModuleId(rs.getInt("module_id"));
                s.setEnseignantId(rs.getInt("enseignant_id"));
                liste.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur récupération sous-modules", e);
        }
        return liste;
    }
}
