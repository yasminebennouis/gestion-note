package com.gestionnotes.model;

import java.time.LocalDate;

public class Note {
    private int id;
    private double valeur;
    private int etudiantId;
    private int sousModuleId;
    private LocalDate dateSaisie;

    // Informations supplémentaires pour affichage
    private String nomEtudiant;
    private String nomSousModule;

    public Note() {}

    public Note(double valeur, int etudiantId, int sousModuleId, LocalDate dateSaisie) {
        this.valeur = valeur;
        this.etudiantId = etudiantId;
        this.sousModuleId = sousModuleId;
        this.dateSaisie = dateSaisie;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getValeur() { return valeur; }
    public void setValeur(double valeur) { this.valeur = valeur; }

    public int getEtudiantId() { return etudiantId; }
    public void setEtudiantId(int etudiantId) { this.etudiantId = etudiantId; }

    public int getSousModuleId() { return sousModuleId; }
    public void setSousModuleId(int sousModuleId) { this.sousModuleId = sousModuleId; }

    public LocalDate getDateSaisie() { return dateSaisie; }
    public void setDateSaisie(LocalDate dateSaisie) { this.dateSaisie = dateSaisie; }

    public String getNomEtudiant() { return nomEtudiant; }
    public void setNomEtudiant(String nomEtudiant) { this.nomEtudiant = nomEtudiant; }

    public String getNomSousModule() { return nomSousModule; }
    public void setNomSousModule(String nomSousModule) { this.nomSousModule = nomSousModule; }

    @Override
    public String toString() {
        return "Note{" + "valeur=" + valeur + ", etudiantId=" + etudiantId + ", sousModuleId=" + sousModuleId + '}';
    }
}
