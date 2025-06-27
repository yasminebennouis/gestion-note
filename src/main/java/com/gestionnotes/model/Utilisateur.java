package com.gestionnotes.model;

public class Utilisateur {
    private int id;
    private String username;
    private String password;
    private String nomComplet;
    private String role;

    public Utilisateur() {}

    public Utilisateur(String username, String password, String nomComplet, String role) {
        this.username = username;
        this.password = password;
        this.nomComplet = nomComplet;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return nomComplet + " (" + role + ")";
    }
}

