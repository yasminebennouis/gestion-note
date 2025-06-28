package com.gestionnotes.service;

import com.gestionnotes.model.Module;
import com.gestionnotes.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleService {

    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public List<Module> getAll() {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT * FROM modules";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Module module = new Module();
                module.setId(rs.getInt("id"));
                module.setNom(rs.getString("nom"));
                module.setCoefficient(rs.getDouble("coefficient")); // ✅ Correction ici
                modules.add(module);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des modules", e);
        }

        return modules;
    }

    public void ajouterModule(Module module) {
        String sql = "INSERT INTO modules (nom, coefficient) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, module.getNom());
            stmt.setDouble(2, module.getCoefficient()); // ✅ Correction ici
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du module", e);
        }
    }

    public void supprimerModule(int id) {
        String sql = "DELETE FROM modules WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du module", e);
        }
    }
}