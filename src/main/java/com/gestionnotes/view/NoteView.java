package com.gestionnotes.view;

import com.gestionnotes.controller.NoteController;
import com.gestionnotes.model.Note;
import com.gestionnotes.model.Utilisateur;
import com.gestionnotes.service.NoteService;
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

public class NoteView {

    private final NoteService noteService;
    private final Utilisateur utilisateur;

    public NoteView(NoteService service, Utilisateur utilisateur) {
        this.noteService = service;
        this.utilisateur = utilisateur;
    }

    public void show(Stage stage) {
        Label lblTitre = new Label("📝 Gestion des Notes");
        lblTitre.getStyleClass().add("title");

        TextField txtValeur = new TextField();
        txtValeur.setPromptText("Valeur");

        TextField txtEtudiantId = new TextField();
        txtEtudiantId.setPromptText("ID Étudiant");

        TextField txtSousModuleId = new TextField();
        txtSousModuleId.setPromptText("ID Sous-module");

        DatePicker datePicker = new DatePicker();

        Button btnAjouter = new Button("Ajouter");
        Button btnSupprimer = new Button("Supprimer");
        Button btnImporter = new Button("Importer Excel");
        Button btnRetour = new Button("Retour");

        TableView<Note> table = new TableView<>();
        TableColumn<Note, Double> colValeur = new TableColumn<>("Valeur");
        TableColumn<Note, String> colEtudiant = new TableColumn<>("Étudiant");
        TableColumn<Note, String> colSousModule = new TableColumn<>("Sous-module");
        TableColumn<Note, LocalDate> colDate = new TableColumn<>("Date");

        colValeur.setCellValueFactory(new PropertyValueFactory<>("valeur"));
        colEtudiant.setCellValueFactory(new PropertyValueFactory<>("nomEtudiant"));
        colSousModule.setCellValueFactory(new PropertyValueFactory<>("nomSousModule"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateSaisie"));

        table.getColumns().addAll(colValeur, colEtudiant, colSousModule, colDate);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        NoteController controller = new NoteController(noteService);
        rafraichir(controller, table);

        btnAjouter.setOnAction(e -> {
            try {
                if (datePicker.getValue() == null)
                    throw new Exception("Date de saisie obligatoire.");
                Note note = new Note(
                        Double.parseDouble(txtValeur.getText()),
                        Integer.parseInt(txtEtudiantId.getText()),
                        Integer.parseInt(txtSousModuleId.getText()),
                        datePicker.getValue()
                );
                controller.ajouter(note);
                rafraichir(controller, table);
                txtValeur.clear(); txtEtudiantId.clear(); txtSousModuleId.clear(); datePicker.setValue(null);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Note ajoutée.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", ex.getMessage());
            }
        });

        btnSupprimer.setOnAction(e -> {
            Note selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.supprimer(selected);
                rafraichir(controller, table);
                showAlert(Alert.AlertType.INFORMATION, "Suppression", "Note supprimée.");
            }
        });

        btnImporter.setOnAction(e -> {
            try {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Sélectionner fichier Excel");
                File file = chooser.showOpenDialog(stage);
                if (file != null) {
                    List<Note> notes = ExcelHelper.importerNotes(file);
                    for (Note n : notes) {
                        controller.ajouter(n);
                    }
                    rafraichir(controller, table);
                    showAlert(Alert.AlertType.INFORMATION, "Import", "Importation terminée.");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur import", ex.getMessage());
            }
        });

        btnRetour.setOnAction(e -> {
            MainView mainView = new MainView(stage, utilisateur);
            mainView.afficherMenuPrincipal();
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(txtValeur, 0, 0);
        form.add(txtEtudiantId, 1, 0);
        form.add(txtSousModuleId, 2, 0);
        form.add(datePicker, 3, 0);
        form.add(btnAjouter, 4, 0);
        form.add(btnSupprimer, 5, 0);
        form.add(btnImporter, 6, 0);

        VBox root = new VBox(20, lblTitre, form, table, btnRetour);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 1300, 700);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Gestion des Notes");
        stage.setScene(scene);
        stage.show();
    }

    private void rafraichir(NoteController controller, TableView<Note> table) {
        List<Note> notes = controller.getAll();
        table.setItems(FXCollections.observableArrayList(notes));
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
