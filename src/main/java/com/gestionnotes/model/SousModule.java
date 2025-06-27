package com.gestionnotes.model;

public class SousModule {
    private int id;
    private String nom;
    private double coefficient;
    private int moduleId;
    private int enseignantId;

    public SousModule() {}

    public SousModule(String nom, double coefficient, int moduleId, int enseignantId) {
        this.nom = nom;
        this.coefficient = coefficient;
        this.moduleId = moduleId;
        this.enseignantId = enseignantId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getCoefficient() { return coefficient; }
    public void setCoefficient(double coefficient) { this.coefficient = coefficient; }

    public int getModuleId() { return moduleId; }
    public void setModuleId(int moduleId) { this.moduleId = moduleId; }

    public int getEnseignantId() { return enseignantId; }
    public void setEnseignantId(int enseignantId) { this.enseignantId = enseignantId; }
}
