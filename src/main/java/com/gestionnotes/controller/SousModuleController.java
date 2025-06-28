package com.gestionnotes.controller;

import com.gestionnotes.model.SousModule;
import com.gestionnotes.service.SousModuleService;

import java.util.List;

public class SousModuleController {

    private final SousModuleService service;

    public SousModuleController(SousModuleService service) {
        this.service = service;
    }

    public void ajouter(SousModule sm) {
        service.ajouter(sm);
    }

    public void supprimer(int id) {
        service.supprimer(id);
    }

    // ✅ Surcharge pour accepter un objet SousModule directement
    public void supprimer(SousModule sm) {
        if (sm != null) {
            service.supprimer(sm.getId());
        }
    }

    public List<SousModule> getAll() {
        return service.getAll();
    }
}

