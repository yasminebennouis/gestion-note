package com.gestionnotes.service;

import com.gestionnotes.model.Note;
import com.gestionnotes.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class StatistiquesService {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public Map<Integer, Double> calculerMoyennesEtudiants() {
        Map<Integer, Double> moyennes = new HashMap<>();
        String sql = """
            SELECT etudiant_id, 
                   SUM(valeur * sm.coefficient) / SUM(sm.coefficient) AS moyenne
            FROM notes n
            JOIN sous_modules sm ON n.sous_module_id = sm.id
            GROUP BY etudiant_id
        """;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                moyennes.put(rs.getInt("etudiant_id"), rs.getDouble("moyenne"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul des moyennes", e);
        }
        return moyennes;
    }

    public List<Integer> getEtudiantsEnEchec() {
        List<Integer> ids = new ArrayList<>();
        for (var entry : calculerMoyennesEtudiants().entrySet()) {
            if (entry.getValue() < 10) {
                ids.add(entry.getKey());
            }
        }
        return ids;
    }

    public double moyenneClasse() {
        var moyennes = calculerMoyennesEtudiants().values();
        return moyennes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double meilleureMoyenne() {
        return calculerMoyennesEtudiants().values().stream()
                .mapToDouble(Double::doubleValue).max().orElse(0.0);
    }
}
