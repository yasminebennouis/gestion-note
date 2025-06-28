package com.gestionnotes.view;

import com.gestionnotes.model.*;
import com.gestionnotes.service.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class TableauRecapitulatifView {

    private final Utilisateur utilisateur;

    public TableauRecapitulatifView(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void show(Stage stage) {
        Label title = new Label("📊 Tableau Récapitulatif par Classe");
        title.getStyleClass().add("title");

        ComboBox<Promotion> promotionComboBox = new ComboBox<>();
        PromotionService promotionService = new PromotionService();
        promotionComboBox.setItems(FXCollections.observableArrayList(promotionService.getAll()));
        promotionComboBox.setPromptText("Sélectionner une promotion");

        TableView<Map<String, String>> table = new TableView<>();

        TableColumn<Map<String, String>, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("nom")));

        TableColumn<Map<String, String>, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("prenom")));

        TableColumn<Map<String, String>, String> colModule = new TableColumn<>("Module");
        colModule.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("module")));

        TableColumn<Map<String, String>, String> colMoyenne = new TableColumn<>("Moyenne Module");
        colMoyenne.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("moyenne")));

        TableColumn<Map<String, String>, String> colSous1 = new TableColumn<>("Sous-Module 1");
        colSous1.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("sous1")));

        TableColumn<Map<String, String>, String> colNote1 = new TableColumn<>("Note 1");
        colNote1.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("note1")));

        TableColumn<Map<String, String>, String> colSous2 = new TableColumn<>("Sous-Module 2");
        colSous2.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("sous2")));

        TableColumn<Map<String, String>, String> colNote2 = new TableColumn<>("Note 2");
        colNote2.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("note2")));

        table.getColumns().addAll(colNom, colPrenom, colModule, colMoyenne, colSous1, colNote1, colSous2, colNote2);

        Label lblMoyPromo = new Label();
        lblMoyPromo.setStyle("-fx-font-weight: bold;");

        Button btnAfficher = new Button("Afficher Résultats");
        btnAfficher.setOnAction(e -> {
            Promotion promo = promotionComboBox.getValue();
            if (promo == null) return;

            EtudiantService etudiantService = new EtudiantService();
            NoteService noteService = new NoteService();
            ModuleService moduleService = new ModuleService();
            SousModuleService sousModuleService = new SousModuleService();

            List<Etudiant> etudiants = etudiantService.getAll().stream()
                    .filter(et -> et.getPromotionId() == promo.getId())
                    .collect(Collectors.toList());

            List<Note> notes = noteService.getToutesLesNotes();
            List<com.gestionnotes.model.Module> modules = moduleService.getAll();  // ✅ Explicit import
            List<SousModule> sousModules = sousModuleService.getAll();

            List<Map<String, String>> rows = new ArrayList<>();
            List<Double> toutesLesMoyennes = new ArrayList<>();

            for (Etudiant etu : etudiants) {
                for (com.gestionnotes.model.Module mod : modules) {
                    List<SousModule> sousDeCeModule = sousModules.stream()
                            .filter(sm -> sm.getModuleId() == mod.getId())
                            .collect(Collectors.toList());

                    List<Note> notesEtudiantPourModule = notes.stream()
                            .filter(n -> n.getEtudiantId() == etu.getId() &&
                                    sousDeCeModule.stream().anyMatch(sm -> sm.getId() == n.getSousModuleId()))
                            .collect(Collectors.toList());

                    if (!notesEtudiantPourModule.isEmpty()) {
                        double somme = 0;
                        double totalCoef = 0;

                        for (Note n : notesEtudiantPourModule) {
                            SousModule sm = sousDeCeModule.stream()
                                    .filter(s -> s.getId() == n.getSousModuleId())
                                    .findFirst().orElse(null);
                            if (sm != null) {
                                somme += n.getValeur() * sm.getCoefficient();
                                totalCoef += sm.getCoefficient();
                            }
                        }

                        double moyenneModule = totalCoef != 0 ? somme / totalCoef : 0;
                        toutesLesMoyennes.add(moyenneModule);

                        Map<String, String> map = new HashMap<>();
                        map.put("nom", etu.getNom());
                        map.put("prenom", etu.getPrenom());
                        map.put("module", mod.getNom());
                        map.put("moyenne", String.format("%.2f", moyenneModule));

                        for (int i = 0; i < 2; i++) {
                            if (i < sousDeCeModule.size()) {
                                SousModule sm = sousDeCeModule.get(i);
                                map.put("sous" + (i + 1), sm.getNom());

                                Note note = notes.stream()
                                        .filter(n -> n.getEtudiantId() == etu.getId() && n.getSousModuleId() == sm.getId())
                                        .findFirst().orElse(null);

                                map.put("note" + (i + 1), note != null ? String.format("%.2f", note.getValeur()) : "");
                            } else {
                                map.put("sous" + (i + 1), "");
                                map.put("note" + (i + 1), "");
                            }
                        }

                        rows.add(map);
                    } else {
                        // Module sans sous-module ou sans note => moyenne nulle
                        Map<String, String> ligne = new HashMap<>();
                        ligne.put("nom", etu.getNom());
                        ligne.put("prenom", etu.getPrenom());
                        ligne.put("module", mod.getNom());
                        ligne.put("moyenne", "0.00");
                        ligne.put("sous1", "");
                        ligne.put("note1", "");
                        ligne.put("sous2", "");
                        ligne.put("note2", "");
                        rows.add(ligne);
                    }
                }
            }

            table.setItems(FXCollections.observableArrayList(rows));

            double moyennePromo = toutesLesMoyennes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            lblMoyPromo.setText("📌 Moyenne générale de la promotion : " + String.format("%.2f", moyennePromo));
        });

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> new MainView(stage, utilisateur).afficherMenuPrincipal());

        VBox root = new VBox(20, title, promotionComboBox, btnAfficher, table, lblMoyPromo, btnRetour);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 1200, 650);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Tableau Récapitulatif par Classe");
        stage.show();
    }
}
