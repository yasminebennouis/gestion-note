package com.gestionnotes.model;

import java.time.LocalDate;

public class Etudiant {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateNaissance;
    private int promotionId;
    private String nomPromotion;
    private boolean archive;

    public Etudiant() {}

    public Etudiant(String nom, String prenom, String email, LocalDate dateNaissance, int promotionId) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.promotionId = promotionId;
        this.archive = false;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public int getPromotionId() { return promotionId; }
    public void setPromotionId(int promotionId) { this.promotionId = promotionId; }

    public String getNomPromotion() { return nomPromotion; }
    public void setNomPromotion(String nomPromotion) { this.nomPromotion = nomPromotion; }

    public boolean isArchive() { return archive; }
    public void setArchive(boolean archive) { this.archive = archive; }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return getNomComplet();
    }
}
