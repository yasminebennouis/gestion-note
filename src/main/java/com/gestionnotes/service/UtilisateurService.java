package com.gestionnotes.service;

import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurService {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public Utilisateur login(String username, String password) {
        String sql = "SELECT * FROM utilisateurs WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));

                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                user.setNomComplet(prenom + " " + nom);

                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la connexion", e);
        }
        return null;
    }
}
