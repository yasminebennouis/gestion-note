package com.gestionnotes.service;

import com.gestionnotes.model.Note;
import com.gestionnotes.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteService {

    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public void ajouterNote(Note note) {
        String sql = "INSERT INTO notes (valeur, etudiant_id, sous_module_id, date_saisie) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, note.getValeur());
            stmt.setInt(2, note.getEtudiantId());
            stmt.setInt(3, note.getSousModuleId());
            stmt.setDate(4, Date.valueOf(note.getDateSaisie()));
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Erreur : l'étudiant ou le sous-module n'existe pas, ou la note est déjà saisie.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de l'ajout de la note", e);
        }
    }

    public void supprimerNote(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM notes WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la note", e);
        }
    }

    public List<Note> getToutesLesNotes() {
        List<Note> notes = new ArrayList<>();
        String sql = """
                SELECT n.*, e.nom AS nom_etudiant, sm.nom AS nom_sous_module
                FROM notes n
                JOIN etudiants e ON n.etudiant_id = e.id
                JOIN sous_modules sm ON n.sous_module_id = sm.id
                """;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setValeur(rs.getDouble("valeur"));
                note.setEtudiantId(rs.getInt("etudiant_id"));
                note.setSousModuleId(rs.getInt("sous_module_id"));
                note.setDateSaisie(rs.getDate("date_saisie").toLocalDate());
                note.setNomEtudiant(rs.getString("nom_etudiant"));
                note.setNomSousModule(rs.getString("nom_sous_module"));
                notes.add(note);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des notes", e);
        }
        return notes;
    }
}