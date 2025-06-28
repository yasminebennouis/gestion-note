package com.gestionnotes.controller;

import com.gestionnotes.model.Etudiant;
import com.gestionnotes.service.EtudiantService;

import java.util.List;

public class EtudiantController {

    private final EtudiantService service;

    public EtudiantController(EtudiantService service) {
        this.service = service;
    }

    public List<Etudiant> getAll() {
        return service.getAll();
    }

    public void ajouter(Etudiant etudiant) {
        service.ajouterEtudiant(etudiant);
    }

    public void supprimer(Etudiant etudiant) {
        service.supprimerEtudiant(etudiant.getId());
    }
}