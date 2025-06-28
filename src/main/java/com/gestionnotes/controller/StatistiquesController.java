package com.gestionnotes.controller;

import com.gestionnotes.service.StatistiquesService;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.Map;

public class StatistiquesController {

    private final StatistiquesService service;

    public StatistiquesController(StatistiquesService service) {
        this.service = service;
    }

    public void afficherStatistiques(Label lblMoyenneClasse, Label lblMeilleureMoyenne, Label lblNbEchecs, TextArea txtMoyennes) {
        double moyenneClasse = service.moyenneClasse();
        double meilleure = service.meilleureMoyenne();
        int nbEchecs = service.getEtudiantsEnEchec().size();

        lblMoyenneClasse.setText("Moyenne classe : " + String.format("%.2f", moyenneClasse));
        lblMeilleureMoyenne.setText("Meilleure moyenne : " + String.format("%.2f", meilleure));
        lblNbEchecs.setText("Étudiants en échec : " + nbEchecs);

        Map<Integer, Double> moyennes = service.calculerMoyennesEtudiants();
        StringBuilder sb = new StringBuilder();
        for (var entry : moyennes.entrySet()) {
            sb.append("Etudiant ID ").append(entry.getKey()).append(" : ").append(String.format("%.2f", entry.getValue())).append("\n");
        }
        txtMoyennes.setText(sb.toString());
    }
}