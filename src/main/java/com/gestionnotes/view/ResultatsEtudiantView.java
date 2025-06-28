package com.gestionnotes.view;

import com.gestionnotes.model.Etudiant;
import com.gestionnotes.model.Note;
import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.EtudiantService;
import com.gestionnotes.service.NoteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class ResultatsEtudiantView {

    private final Utilisateur utilisateur;

    public ResultatsEtudiantView(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void start(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("📘 Résultats d’un Étudiant");
        title.getStyleClass().add("title");

        TextField txtId = new TextField();
        txtId.setPromptText("Saisir l'ID de l'étudiant");

        Button btnAfficher = new Button("Afficher les résultats");

        TableView<Map<String, String>> table = new TableView<>();

        table.getColumns().addAll(
                createColumn("Nom", "nom"),
                createColumn("Prénom", "prenom"),
                createColumn("Module", "module"),
                createColumn("Moyenne", "moyenne"),
                createColumn("Sous-module 1", "sm1"),
                createColumn("Note 1", "note1"),
                createColumn("Sous-module 2", "sm2"),
                createColumn("Note 2", "note2")
        );

        btnAfficher.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                EtudiantService etudiantService = new EtudiantService();
                NoteService noteService = new NoteService();

                Etudiant etu = etudiantService.getAll().stream()
                        .filter(et -> et.getId() == id)
                        .findFirst().orElse(null);

                if (etu == null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Étudiant introuvable !");
                    return;
                }

                List<Note> notes = noteService.getToutesLesNotes().stream()
                        .filter(n -> n.getEtudiantId() == id)
                        .collect(Collectors.toList());

                Map<String, List<Note>> notesParModule = new HashMap<>();
                for (Note note : notes) {
                    String nomModule = note.getNomSousModule().split("-")[0].trim(); // suppose que "RO - Analyse"
                    notesParModule.computeIfAbsent(nomModule, k -> new ArrayList<>()).add(note);
                }

                List<Map<String, String>> rows = new ArrayList<>();
                for (Map.Entry<String, List<Note>> entry : notesParModule.entrySet()) {
                    String module = entry.getKey();
                    List<Note> n = entry.getValue();
                    double moyenne = n.stream().mapToDouble(Note::getValeur).average().orElse(0.0);

                    Map<String, String> ligne = new HashMap<>();
                    ligne.put("nom", etu.getNom());
                    ligne.put("prenom", etu.getPrenom());
                    ligne.put("module", module);
                    ligne.put("moyenne", String.format("%.2f", moyenne));

                    // Ajout de 2 sous-modules max
                    if (n.size() > 0) {
                        ligne.put("sm1", n.get(0).getNomSousModule());
                        ligne.put("note1", String.valueOf(n.get(0).getValeur()));
                    }
                    if (n.size() > 1) {
                        ligne.put("sm2", n.get(1).getNomSousModule());
                        ligne.put("note2", String.valueOf(n.get(1).getValeur()));
                    }

                    rows.add(ligne);
                }

                table.setItems(FXCollections.observableArrayList(rows));

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "ID invalide !");
            }
        });

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> {
            MainView mainView = new MainView(stage, utilisateur);
            mainView.afficherMenuPrincipal();
        });

        root.getChildren().addAll(title, txtId, btnAfficher, table, btnRetour);
        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Résultats Étudiant");
        stage.show();
    }

    private TableColumn<Map<String, String>, String> createColumn(String title, String key) {
        TableColumn<Map<String, String>, String> col = new TableColumn<>(title);
        col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrDefault(key, "")));
        return col;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
