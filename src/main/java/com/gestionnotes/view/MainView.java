package com.gestionnotes.view;

import com.gestionnotes.controller.*;
import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {

    private final Stage primaryStage;
    private final Utilisateur utilisateur;

    public MainView(Stage primaryStage, Utilisateur utilisateur) {
        this.primaryStage = primaryStage;
        this.utilisateur = utilisateur;
        afficherMenuPrincipal();
    }

    public void afficherMenuPrincipal() {
        Label lblTitre = new Label("\uD83D\uDCDA Application de Gestion des Notes");
        lblTitre.getStyleClass().add("title");

        Label lblUtilisateur = new Label("\uD83D\uDC64 Connecté : " + utilisateur.getNomComplet() + " (" + utilisateur.getRole() + ")");
        lblUtilisateur.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        Button btnEtudiants = new Button("\uD83D\uDC68\u200D\uD83C\uDF93 Gérer les Étudiants");
        Button btnPromotions = new Button("\uD83C\uDFDB Gérer les Promotions");
        Button btnModules = new Button("\uD83D\uDCD8 Gérer les Modules");
        Button btnSousModules = new Button("\uD83D\uDCD6 Gérer les Sous-modules");
        Button btnNotes = new Button("\uD83D\uDCDD Gérer les Notes");
        Button btnStatistiques = new Button("\uD83D\uDCCA Statistiques Générales");
        Button btnQuitter = new Button("\u274C Quitter");

        // Actions
        btnEtudiants.setOnAction(e -> new EtudiantView(new EtudiantService(), utilisateur).show(primaryStage));
        btnPromotions.setOnAction(e -> new PromotionView(new PromotionService(), utilisateur).show(primaryStage));
        btnModules.setOnAction(e -> new ModuleView(new ModuleService(), utilisateur).show(primaryStage));
        btnSousModules.setOnAction(e -> new SousModuleView(new SousModuleService(), utilisateur).show(primaryStage));
        btnNotes.setOnAction(e -> new NoteView(new NoteService(), utilisateur).show(primaryStage));
        btnStatistiques.setOnAction(e -> new StatistiquesView(new StatistiquesService(), utilisateur).show(primaryStage));
        btnQuitter.setOnAction(e -> primaryStage.close());

        VBox menu = new VBox(18, lblTitre, lblUtilisateur, btnEtudiants, btnPromotions, btnModules,
                btnSousModules, btnNotes, btnStatistiques, btnQuitter);
        menu.setAlignment(Pos.CENTER);
        menu.setStyle("-fx-padding: 50;");

        // Chargement du background
        StackPane root = new StackPane();

        try {
            Image backgroundImage = new Image(getClass().getResource("/images/campus.jpeg").toExternalForm());
            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1.0, 1.0, true, true, false, false)
            );
            root.setBackground(new Background(bgImage));
        } catch (Exception ex) {
            System.out.println("Erreur chargement image: " + ex.getMessage());
        }

        root.getChildren().add(menu);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu Principal - Gestion des Notes");
        primaryStage.show();
    }
}