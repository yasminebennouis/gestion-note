package com.gestionnotes.view;

import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.StatistiquesService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Map;

public class StatistiquesView {

    private final StatistiquesService service;
    private final Utilisateur utilisateur;

    public StatistiquesView(StatistiquesService service, Utilisateur utilisateur) {
        this.service = service;
        this.utilisateur = utilisateur;
    }

    public void show(Stage stage) {
        Label title = new Label("📊 Statistiques des Étudiants");
        title.getStyleClass().add("title");

        VBox statsBox = new VBox(15);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(20));

        Label lblMoyenneClasse = new Label();
        Label lblMeilleureMoyenne = new Label();
        Label lblNbEchec = new Label();

        TextArea txtMoyennesEtudiants = new TextArea();
        txtMoyennesEtudiants.setEditable(false);
        txtMoyennesEtudiants.setPrefHeight(250);

        Button btnCharger = new Button("Charger Statistiques");
        btnCharger.setOnAction(e -> {
            double moyenneClasse = service.moyenneClasse();
            double meilleure = service.meilleureMoyenne();
            Map<Integer, Double> moyennes = service.calculerMoyennesEtudiants();
            int nbEchecs = service.getEtudiantsEnEchec().size();

            lblMoyenneClasse.setText("Moyenne Classe : " + String.format("%.2f", moyenneClasse));
            lblMeilleureMoyenne.setText("Meilleure Moyenne : " + String.format("%.2f", meilleure));
            lblNbEchec.setText("Étudiants en échec (<10) : " + nbEchecs);

            StringBuilder details = new StringBuilder("Moyennes par Étudiant :\n");
            moyennes.forEach((id, moy) -> details.append("Étudiant ").append(id).append(" → ").append(String.format("%.2f", moy)).append("\n"));

            txtMoyennesEtudiants.setText(details.toString());
        });

        statsBox.getChildren().addAll(lblMoyenneClasse, lblMeilleureMoyenne, lblNbEchec, txtMoyennesEtudiants);

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(e -> {
            MainView mainView = new MainView(stage, utilisateur);
            mainView.afficherMenuPrincipal();
        });

        VBox root = new VBox(25, title, btnCharger, statsBox, btnRetour);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Statistiques");
        stage.show();
    }
}
