package com.gestionnotes.model;

public class Module {
    private int id;
    private String nom;
    private double coefficient;
    private int promotionId;
    private int enseignantId;
    private String nomPromotion;
    private String nomEnseignant;

    public Module() {}

    public Module(String nom, double coefficient, int promotionId, int enseignantId) {
        this.nom = nom;
        this.coefficient = coefficient;
        this.promotionId = promotionId;
        this.enseignantId = enseignantId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getCoefficient() { return coefficient; }
    public void setCoefficient(double coefficient) { this.coefficient = coefficient; }

    public int getPromotionId() { return promotionId; }
    public void setPromotionId(int promotionId) { this.promotionId = promotionId; }

    public int getEnseignantId() { return enseignantId; }
    public void setEnseignantId(int enseignantId) { this.enseignantId = enseignantId; }

    public String getNomPromotion() { return nomPromotion; }
    public void setNomPromotion(String nomPromotion) { this.nomPromotion = nomPromotion; }

    public String getNomEnseignant() { return nomEnseignant; }
    public void setNomEnseignant(String nomEnseignant) { this.nomEnseignant = nomEnseignant; }

    @Override
    public String toString() {
        return nom;
    }
}