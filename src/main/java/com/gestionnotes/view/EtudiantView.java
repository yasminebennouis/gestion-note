package com.gestionnotes.view;

import com.gestionnotes.controller.EtudiantController;
import com.gestionnotes.model.Etudiant;
import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.EtudiantService;
import com.gestionnotes.util.ExcelHelper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class EtudiantView {

    private final EtudiantService etudiantService;
    private final Utilisateur utilisateur;

    public EtudiantView(EtudiantService service, Utilisateur utilisateur) {
        this.etudiantService = service;
        this.utilisateur = utilisateur;
    }

    public void show(Stage stage) {
        Label lblTitre = new Label("📚 Gestion des Étudiants");
        lblTitre.getStyleClass().add("title");

        TextField txtNom = new TextField(); txtNom.setPromptText("Nom");
        TextField txtPrenom = new TextField(); txtPrenom.setPromptText("Prénom");
        TextField txtEmail = new TextField(); txtEmail.setPromptText("Email");
        DatePicker dateNaissance = new DatePicker();
        TextField txtPromoId = new TextField(); txtPromoId.setPromptText("ID Promotion");

        Button btnAjouter = new Button("Ajouter");
        Button btnSupprimer = new Button("Supprimer");
        Button btnImporter = new Button("Importer Excel");
        Button btnRetour = new Button("Retour");

        TableView<Etudiant> table = new TableView<>();
        TableColumn<Etudiant, String> colNom = new TableColumn<>("Nom");
        TableColumn<Etudiant, String> colPrenom = new TableColumn<>("Prénom");
        TableColumn<Etudiant, String> colEmail = new TableColumn<>("Email");

        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.getColumns().addAll(colNom, colPrenom, colEmail);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        EtudiantController controller = new EtudiantController(etudiantService);
        rafraichir(controller, table);

        btnAjouter.setOnAction(e -> {
            try {
                if (dateNaissance.getValue() == null) throw new Exception("Date de naissance non saisie");
                Etudiant etu = new Etudiant(
                        txtNom.getText(),
                        txtPrenom.getText(),
                        txtEmail.getText(),
                        dateNaissance.getValue(),
                        Integer.parseInt(txtPromoId.getText())
                );
                etudiantService.ajouterEtudiant(etu);
                rafraichir(controller, table);
                txtNom.clear(); txtPrenom.clear(); txtEmail.clear(); txtPromoId.clear(); dateNaissance.setValue(null);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Étudiant ajouté avec succès.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ex.getMessage());
            }
        });

        btnSupprimer.setOnAction(e -> {
            Etudiant selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.supprimer(selected);
                rafraichir(controller, table);
                showAlert(Alert.AlertType.INFORMATION, "Suppression", "Étudiant supprimé.");
            }
        });

        btnImporter.setOnAction(e -> {
            try {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Sélectionner un fichier Excel");
                File file = chooser.showOpenDialog(stage);
                if (file != null) {
                    int promotionId = Integer.parseInt(txtPromoId.getText());
                    List<Etudiant> etudiants = ExcelHelper.importerEtudiants(file, promotionId);
                    for (Etudiant eImport : etudiants) {
                        etudiantService.ajouterEtudiant(eImport);
                    }
                    rafraichir(controller, table);
                    showAlert(Alert.AlertType.INFORMATION, "Import", "Importation terminée.");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur Import", ex.getMessage());
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
        form.add(txtPrenom, 1, 0);
        form.add(txtEmail, 2, 0);
        form.add(dateNaissance, 0, 1);
        form.add(txtPromoId, 1, 1);
        form.add(btnAjouter, 2, 1);
        form.add(btnSupprimer, 3, 1);
        form.add(btnImporter, 4, 1);

        VBox root = new VBox(20, lblTitre, form, table, btnRetour);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 1200, 650);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Gestion des Étudiants");
        stage.setScene(scene);
        stage.show();
    }

    private void rafraichir(EtudiantController controller, TableView<Etudiant> table) {
        List<Etudiant> etudiants = controller.getAll();
        table.setItems(FXCollections.observableArrayList(etudiants));
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
