package com.gestionnotes.view;

import com.gestionnotes.controller.SousModuleController;
import com.gestionnotes.model.SousModule;
import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.SousModuleService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class SousModuleView {

    private final SousModuleService sousModuleService;
    private final Utilisateur utilisateur;

    public SousModuleView(SousModuleService service, Utilisateur utilisateur) {
        this.sousModuleService = service;
        this.utilisateur = utilisateur;
    }

    public void show(Stage stage) {
        Label titre = new Label("📗 Gestion des Sous-Modules");
        titre.getStyleClass().add("title");

        TextField txtNom = new TextField();
        txtNom.setPromptText("Nom Sous-Module");

        TextField txtCoef = new TextField();
        txtCoef.setPromptText("Coefficient");

        TextField txtModuleId = new TextField();
        txtModuleId.setPromptText("ID Module");

        TextField txtEnsId = new TextField();
        txtEnsId.setPromptText("ID Enseignant");

        Button btnAjouter = new Button("Ajouter");
        Button btnSupprimer = new Button("Supprimer");
        Button btnRetour = new Button("Retour");

        TableView<SousModule> table = new TableView<>();
        TableColumn<SousModule, String> colNom = new TableColumn<>("Nom");
        TableColumn<SousModule, Double> colCoef = new TableColumn<>("Coefficient");

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCoef.setCellValueFactory(new PropertyValueFactory<>("coefficient"));

        table.getColumns().addAll(colNom, colCoef);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        SousModuleController controller = new SousModuleController(sousModuleService);
        rafraichir(controller, table);

        btnAjouter.setOnAction(e -> {
            try {
                SousModule sm = new SousModule(
                        txtNom.getText(),
                        Double.parseDouble(txtCoef.getText()),
                        Integer.parseInt(txtModuleId.getText()),
                        Integer.parseInt(txtEnsId.getText())
                );
                controller.ajouter(sm);
                rafraichir(controller, table);
                txtNom.clear(); txtCoef.clear(); txtModuleId.clear(); txtEnsId.clear();
                showAlert(Alert.AlertType.INFORMATION, "Ajout", "Sous-module ajouté.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ex.getMessage());
            }
        });

        btnSupprimer.setOnAction(e -> {
            SousModule selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.supprimer(selected);
                rafraichir(controller, table);
                showAlert(Alert.AlertType.INFORMATION, "Suppression", "Sous-module supprimé.");
            }
        });

        btnRetour.setOnAction(e -> {
            MainView mainView = new MainView(stage, utilisateur);
            mainView.afficherMenuPrincipal();
        });

        HBox form = new HBox(15, txtNom, txtCoef, txtModuleId, txtEnsId, btnAjouter, btnSupprimer);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);

        VBox root = new VBox(25, titre, form, table, btnRetour);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Gestion des Sous-Modules");
        stage.setScene(scene);
        stage.show();
    }

    private void rafraichir(SousModuleController controller, TableView<SousModule> table) {
        List<SousModule> sousModules = controller.getAll();
        table.setItems(FXCollections.observableArrayList(sousModules));
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
