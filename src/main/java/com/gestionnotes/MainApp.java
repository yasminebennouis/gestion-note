package com.gestionnotes;

import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.UtilisateurService;
import com.gestionnotes.util.AlertHelper;
import com.gestionnotes.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final UtilisateurService utilisateurService = new UtilisateurService();

    @Override
    public void start(Stage primaryStage) {
        afficherLogin(primaryStage);
    }

    private void afficherLogin(Stage stage) {
        Label lblTitre = new Label("🔐 Connexion");
        lblTitre.getStyleClass().add("title");

        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Nom d'utilisateur");
        txtUsername.setMaxWidth(250);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Mot de passe");
        txtPassword.setMaxWidth(250);

        ComboBox<String> cbRoles = new ComboBox<>();
        cbRoles.getItems().addAll("ADMIN", "ENSEIGNANT", "RESPONSABLE");
        cbRoles.setPromptText("Rôle");
        cbRoles.setMaxWidth(250);

        Button btnConnexion = new Button("Se connecter");
        Label lblMessage = new Label();
        lblMessage.setStyle("-fx-text-fill: red;");

        btnConnexion.setOnAction(e -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String role = cbRoles.getValue();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                lblMessage.setText("⚠️ Tous les champs sont obligatoires.");
                return;
            }

            Utilisateur utilisateur = utilisateurService.login(username, password);
            if (utilisateur != null && utilisateur.getRole().equalsIgnoreCase(role)) {
                MainView mainView = new MainView(stage, utilisateur);
                mainView.afficherMenuPrincipal();
            } else {
                lblMessage.setText("❌ Informations invalides ou rôle incorrect.");
            }
        });

        VBox root = new VBox(20, lblTitre, txtUsername, txtPassword, cbRoles, btnConnexion, lblMessage);
        root.setPadding(new javafx.geometry.Insets(40));
        root.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(root, 450, 450);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Connexion");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}