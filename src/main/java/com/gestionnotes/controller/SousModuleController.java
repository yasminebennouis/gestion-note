package com.gestionnotes.controller;

import com.gestionnotes.model.SousModule;
import com.gestionnotes.service.SousModuleService;

import java.util.List;

public class SousModuleController {

    private final SousModuleService service;

    public SousModuleController(SousModuleService service) {
        this.service = service;
    }

    public List<SousModule> getAll() {
        return service.getTous();
    }

    public void ajouter(SousModule sousModule) {
        service.ajouter(sousModule);
    }

    public void supprimer(SousModule sousModule) {
        service.supprimer(sousModule.getId());
    }
}
