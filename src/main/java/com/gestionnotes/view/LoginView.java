package com.gestionnotes.view;

import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.UtilisateurService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {

    private final UtilisateurService utilisateurService;

    public LoginView(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    public void afficher(Stage stage) {
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
        btnConnexion.setDefaultButton(true);

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

            Utilisateur user = utilisateurService.login(username, password);
            if (user != null && user.getRole().equalsIgnoreCase(role)) {
                MainView mainView = new MainView(stage, user);
                mainView.afficherMenuPrincipal();  // ✅ Lancement de la nouvelle scène
            } else {
                lblMessage.setText("❌ Informations invalides ou rôle incorrect.");
            }
        });

        VBox form = new VBox(15, txtUsername, txtPassword, cbRoles, btnConnexion, lblMessage);
        form.setAlignment(Pos.CENTER);

        VBox root = new VBox(30, lblTitre, form);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 450, 450);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Connexion - Gestion des Notes");
        stage.show();
    }
}