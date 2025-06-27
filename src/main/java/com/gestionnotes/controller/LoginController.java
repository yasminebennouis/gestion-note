package com.gestionnotes.controller;

import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.UtilisateurService;
import com.gestionnotes.util.AlertHelper;
import com.gestionnotes.view.MainView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private final UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            AlertHelper.showWarning("Champs manquants", "Veuillez saisir le nom d'utilisateur et le mot de passe.");
            return;
        }

        Utilisateur utilisateur = utilisateurService.login(username, password);

        if (utilisateur != null) {
            AlertHelper.showInfo("Connexion réussie", "Bienvenue " + utilisateur.getNomComplet());
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            new MainView(stage, utilisateur); // le constructeur appelle déjà afficherMenuPrincipal()
        } else {
            AlertHelper.showError("Échec de connexion", "Nom d'utilisateur ou mot de passe incorrect.");
        }
    }
}
