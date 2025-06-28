package com.gestionnotes.view;

import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.StatistiquesService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {

    private final Stage stage;
    private final Utilisateur utilisateur;

    public MainView(Stage stage, Utilisateur utilisateur) {
        this.stage = stage;
        this.utilisateur = utilisateur;
    }

    public void afficherMenuPrincipal() {
        Label bienvenue = new Label("👋 Bienvenue, " + utilisateur.getNom().toUpperCase() + " (" + utilisateur.getRole() + ")");
        bienvenue.getStyleClass().add("title");

        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);

        Button btnGestionNotes = new Button("📝 Gestion des Notes");
        btnGestionNotes.setOnAction(e -> {
            NoteView noteView = new NoteView(new com.gestionnotes.service.NoteService(), utilisateur);
            noteView.show(stage);
        });

        Button btnGestionModules = new Button("📘 Gestion des Modules");
        btnGestionModules.setOnAction(e -> {
            ModuleView view = new ModuleView(new com.gestionnotes.service.ModuleService(), utilisateur);
            view.show(stage);
        });

        Button btnGestionSousModules = new Button("📗 Gestion des Sous-Modules");
        btnGestionSousModules.setOnAction(e -> {
            SousModuleView view = new SousModuleView(new com.gestionnotes.service.SousModuleService(), utilisateur);
            view.show(stage);
        });

        Button btnGestionPromotions = new Button("🎓 Gestion des Promotions");
        btnGestionPromotions.setOnAction(e -> {
            PromotionView view = new PromotionView(new com.gestionnotes.service.PromotionService(), utilisateur);
            view.show(stage);
        });

        Button btnResultatsEtudiant = new Button("📄 Résultats Étudiant");
        btnResultatsEtudiant.setOnAction(e -> {
            ResultatsEtudiantView resultatsView = new ResultatsEtudiantView(utilisateur);
            resultatsView.start(stage);
        });

        Button btnStatistiques = new Button("📊 Statistiques");
        btnStatistiques.setOnAction(e -> {
            StatistiquesView view = new StatistiquesView(new StatistiquesService(), utilisateur);
            view.show(stage);
        });

        Button btnTableauClasse = new Button("📋 Tableau Récapitulatif Classe");
        btnTableauClasse.setOnAction(e -> {
            TableauRecapitulatifView view = new TableauRecapitulatifView(utilisateur);
            view.show(stage);
        });

        Button btnLogout = new Button("🚪 Déconnexion");
        btnLogout.setOnAction(e -> {
            LoginView loginView = new LoginView(new com.gestionnotes.service.UtilisateurService());
            loginView.afficher(stage);
        });

        menuBox.getChildren().addAll(
                btnGestionNotes,
                btnGestionModules,
                btnGestionSousModules,
                btnGestionPromotions,
                btnResultatsEtudiant,
                btnStatistiques,
                btnTableauClasse,
                btnLogout
        );

        VBox root = new VBox(30, bienvenue, menuBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("🏠 Menu Principal - Gestion des Notes");
        stage.show();
    }
}
