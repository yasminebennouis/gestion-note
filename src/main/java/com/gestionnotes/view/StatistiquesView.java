package com.gestionnotes.view;

import com.gestionnotes.model.Etudiant;
import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.EtudiantService;
import com.gestionnotes.service.StatistiquesService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class StatistiquesView {

    private final StatistiquesService statistiquesService;
    private final Utilisateur utilisateur;

    public StatistiquesView(StatistiquesService service, Utilisateur utilisateur) {
        this.statistiquesService = service;
        this.utilisateur = utilisateur;
    }

    public void show(Stage stage) {
        Label title = new Label("📊 Statistiques des Étudiants");
        title.getStyleClass().add("title");

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);

        Button btnMoyennes = new Button("📈 Moyennes par Étudiant");
        btnMoyennes.setOnAction(evt -> {
            StringBuilder sb = new StringBuilder("Moyennes générales par étudiant :\n");
            EtudiantService etuService = new EtudiantService();
            Map<Integer, Double> moyennes = statistiquesService.calculerMoyennesEtudiants();
            List<Etudiant> etudiants = etuService.getAll();

            for (Etudiant e : etudiants) {
                if (moyennes.containsKey(e.getId())) {
                    sb.append(String.format("- %s %s : %.2f\n", e.getNom(), e.getPrenom(), moyennes.get(e.getId())));
                }
            }

            resultArea.setText(sb.toString());
        });

        Button btnClassement = new Button("🏅 Classement des Étudiants");
        btnClassement.setOnAction(evt -> {
            StringBuilder sb = new StringBuilder("Classement des étudiants par moyenne :\n");
            EtudiantService etuService = new EtudiantService();
            Map<Integer, Double> moyennes = statistiquesService.calculerMoyennesEtudiants();
            List<Etudiant> etudiants = etuService.getAll();

            List<Map.Entry<Integer, Double>> sorted = new ArrayList<>(moyennes.entrySet());
            sorted.sort(Map.Entry.<Integer, Double>comparingByValue().reversed());

            for (Map.Entry<Integer, Double> entry : sorted) {
                Etudiant e = etudiants.stream().filter(et -> et.getId() == entry.getKey()).findFirst().orElse(null);
                if (e != null) {
                    sb.append(String.format("- %s %s : %.2f\n", e.getNom(), e.getPrenom(), entry.getValue()));
                }
            }

            resultArea.setText(sb.toString());
        });

        Button btnEchec = new Button("❌ Étudiants en Échec");
        btnEchec.setOnAction(evt -> {
            StringBuilder sb = new StringBuilder("Étudiants avec une moyenne < 10 :\n");
            EtudiantService etuService = new EtudiantService();
            List<Integer> ids = statistiquesService.getEtudiantsEnEchec();
            List<Etudiant> etudiants = etuService.getAll();

            for (Etudiant e : etudiants) {
                if (ids.contains(e.getId())) {
                    sb.append("- ").append(e.getNom()).append(" ").append(e.getPrenom()).append("\n");
                }
            }

            resultArea.setText(sb.toString());
        });

        Button btnMoyenneClasse = new Button("📌 Moyenne Générale de la Classe");
        btnMoyenneClasse.setOnAction(evt -> {
            double moyenne = statistiquesService.moyenneClasse();
            resultArea.setText("📌 Moyenne générale de la classe : " + String.format("%.2f", moyenne));
        });

        Button btnMeilleur = new Button("🏆 Meilleure Moyenne");
        btnMeilleur.setOnAction(evt -> {
            double max = statistiquesService.meilleureMoyenne();
            resultArea.setText("🏆 Meilleure moyenne obtenue : " + String.format("%.2f", max));
        });

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(event -> {
            MainView mv = new MainView(stage, utilisateur);
            mv.afficherMenuPrincipal();
        });

        HBox btnBox = new HBox(10, btnMoyennes, btnClassement, btnEchec, btnMoyenneClasse, btnMeilleur);
        btnBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, title, btnBox, resultArea, btnRetour);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Statistiques");
        stage.show();
    }
}
