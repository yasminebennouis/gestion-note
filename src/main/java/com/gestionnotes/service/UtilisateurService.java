package com.gestionnotes.service;

import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurService {

    private final Connection connection;

    public UtilisateurService() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public Utilisateur login(String username, String password) {
        String query = "SELECT * FROM utilisateur WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setUsername(rs.getString("username"));
                utilisateur.setPassword(rs.getString("password"));
                utilisateur.setRole(rs.getString("role"));
                utilisateur.setNom(rs.getString("nom"));
                return utilisateur;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la connexion");
        }
        return null;
    }
}
