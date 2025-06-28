package com.gestionnotes.controller;

import com.gestionnotes.model.Module;
import com.gestionnotes.service.ModuleService;

import java.util.List;

public class ModuleController {

    private final ModuleService service;

    public ModuleController(ModuleService service) {
        this.service = service;
    }

    public List<Module> getAll() {
        return service.getAll();
    }

    public void ajouter(Module module) {
        service.ajouterModule(module);
    }

    public void supprimer(Module module) {
        service.supprimerModule(module.getId());
    }
}