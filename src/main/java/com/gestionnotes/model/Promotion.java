package com.gestionnotes.model;

public class Promotion {
    private int id;
    private String nom;
    private String annee;
    private String specialite;
    private boolean active;

    public Promotion() {}

    public Promotion(String nom, String annee, String specialite) {
        this.nom = nom;
        this.annee = annee;
        this.specialite = specialite;
        this.active = true;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAnnee() { return annee; }
    public void setAnnee(String annee) { this.annee = annee; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return nom + " (" + annee + " - " + specialite + ")";
    }
}
