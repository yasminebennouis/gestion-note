package com.gestionnotes.view;

import com.gestionnotes.controller.ModuleController;
import com.gestionnotes.model.Module;
import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.ModuleService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ModuleView {

    private final ModuleService moduleService;
    private final Utilisateur utilisateur;

    public ModuleView(ModuleService service, Utilisateur utilisateur) {
        this.moduleService = service;
        this.utilisateur = utilisateur;
    }

    public void show(Stage stage) {
        Label lblTitre = new Label("📘 Gestion des Modules");
        lblTitre.getStyleClass().add("title");

        TextField txtNom = new TextField();
        txtNom.setPromptText("Nom du Module");

        TextField txtCoef = new TextField();
        txtCoef.setPromptText("Coefficient");

        TextField txtPromoId = new TextField();
        txtPromoId.setPromptText("ID Promotion");

        TextField txtEnsId = new TextField();
        txtEnsId.setPromptText("ID Enseignant");

        Button btnAjouter = new Button("Ajouter");
        Button btnSupprimer = new Button("Supprimer");
        Button btnRetour = new Button("Retour");

        TableView<Module> table = new TableView<>();
        TableColumn<Module, String> colNom = new TableColumn<>("Nom");
        TableColumn<Module, Double> colCoef = new TableColumn<>("Coefficient");

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCoef.setCellValueFactory(new PropertyValueFactory<>("coefficient"));

        table.getColumns().addAll(colNom, colCoef);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ModuleController controller = new ModuleController(moduleService);
        rafraichir(controller, table);

        btnAjouter.setOnAction(e -> {
            try {
                Module module = new Module(
                        txtNom.getText(),
                        Double.parseDouble(txtCoef.getText()),
                        Integer.parseInt(txtPromoId.getText()),
                        Integer.parseInt(txtEnsId.getText())
                );
                controller.ajouter(module);
                rafraichir(controller, table);
                txtNom.clear(); txtCoef.clear(); txtPromoId.clear(); txtEnsId.clear();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Module ajouté avec succès.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ex.getMessage());
            }
        });

        btnSupprimer.setOnAction(e -> {
            Module selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.supprimer(selected);
                rafraichir(controller, table);
                showAlert(Alert.AlertType.INFORMATION, "Suppression", "Module supprimé.");
            }
        });

        btnRetour.setOnAction(e -> {
            MainView mainView = new MainView(stage, utilisateur);
            mainView.afficherMenuPrincipal();
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(txtNom, 0, 0);
        form.add(txtCoef, 1, 0);
        form.add(txtPromoId, 2, 0);
        form.add(txtEnsId, 3, 0);
        form.add(btnAjouter, 4, 0);
        form.add(btnSupprimer, 5, 0);

        VBox root = new VBox(20, lblTitre, form, table, btnRetour);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 1200, 650);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Gestion des Modules");
        stage.setScene(scene);
        stage.show();
    }

    private void rafraichir(ModuleController controller, TableView<Module> table) {
        List<Module> modules = controller.getAll();
        table.setItems(FXCollections.observableArrayList(modules));
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}