package com.gestionnotes.util;

import java.sql.*;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_notes?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";  // Mets ton vrai mot de passe MySQL ici
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private DatabaseConnection() {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            initializeDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la reconnexion", e);
        }
        return connection;
    }

    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Table utilisateurs
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS utilisateurs (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    role ENUM('ADMIN', 'ENSEIGNANT', 'RESPONSABLE') DEFAULT 'ENSEIGNANT',
                    nom VARCHAR(100),
                    prenom VARCHAR(100),
                    email VARCHAR(150),
                    actif BOOLEAN DEFAULT TRUE,
                    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Autres tables (tu les as déjà correctes)
            // ...

            // Insertion par défaut
            stmt.execute("""
                INSERT IGNORE INTO utilisateurs (username, password, role, nom, prenom, email) VALUES 
                ('admin', 'admin123', 'ADMIN', 'Administrateur', 'Système', 'admin@aiac.ma'),
                ('prof1', 'prof123', 'ENSEIGNANT', 'Alami', 'Mohammed', 'malami@aiac.ma'),
                ('responsable', 'resp123', 'RESPONSABLE', 'Bennani', 'Fatima', 'fbennani@aiac.ma')
            """);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création des tables", e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur fermeture base : " + e.getMessage());
        }
    }
}
