package com.gestionnotes.view;

import com.gestionnotes.controller.PromotionController;
import com.gestionnotes.model.Promotion;
import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.PromotionService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class PromotionView {

    private final PromotionService promotionService;
    private final Utilisateur utilisateur;

    public PromotionView(PromotionService service, Utilisateur utilisateur) {
        this.promotionService = service;
        this.utilisateur = utilisateur;
    }

    public void show(Stage stage) {
        Label lblTitre = new Label("🎓 Gestion des Promotions");
        lblTitre.getStyleClass().add("title");

        TextField txtNom = new TextField();
        txtNom.setPromptText("Nom Promotion");

        TextField txtAnnee = new TextField();
        txtAnnee.setPromptText("Année");

        TextField txtSpecialite = new TextField();
        txtSpecialite.setPromptText("Spécialité");

        Button btnAjouter = new Button("Ajouter");
        Button btnSupprimer = new Button("Supprimer");
        Button btnRetour = new Button("Retour");

        TableView<Promotion> table = new TableView<>();
        TableColumn<Promotion, String> colNom = new TableColumn<>("Nom");
        TableColumn<Promotion, String> colAnnee = new TableColumn<>("Année");
        TableColumn<Promotion, String> colSpecialite = new TableColumn<>("Spécialité");

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colSpecialite.setCellValueFactory(new PropertyValueFactory<>("specialite"));

        table.getColumns().addAll(colNom, colAnnee, colSpecialite);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        PromotionController controller = new PromotionController(promotionService);
        rafraichir(controller, table);

        btnAjouter.setOnAction(e -> {
            try {
                Promotion promotion = new Promotion(
                        txtNom.getText(),
                        txtAnnee.getText(),
                        txtSpecialite.getText()
                );
                controller.ajouter(promotion);
                rafraichir(controller, table);
                txtNom.clear();
                txtAnnee.clear();
                txtSpecialite.clear();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Promotion ajoutée.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ex.getMessage());
            }
        });

        btnSupprimer.setOnAction(e -> {
            Promotion selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.supprimer(selected);
                rafraichir(controller, table);
                showAlert(Alert.AlertType.INFORMATION, "Suppression", "Promotion supprimée.");
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
        form.add(txtAnnee, 1, 0);
        form.add(txtSpecialite, 2, 0);
        form.add(btnAjouter, 3, 0);
        form.add(btnSupprimer, 4, 0);

        VBox root = new VBox(20, lblTitre, form, table, btnRetour);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Gestion des Promotions");
        stage.setScene(scene);
        stage.show();
    }

    private void rafraichir(PromotionController controller, TableView<Promotion> table) {
        List<Promotion> promotions = controller.getAll();
        table.setItems(FXCollections.observableArrayList(promotions));
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}