package com.gestionnotes.controller;

import com.gestionnotes.model.Promotion;
import com.gestionnotes.service.PromotionService;

import java.util.List;

public class PromotionController {

    private final PromotionService service;

    public PromotionController(PromotionService service) {
        this.service = service;
    }

    public List<Promotion> getAll() {
        return service.getAll();
    }

    public void ajouter(Promotion promotion) {
        service.ajouterPromotion(promotion);
    }

    public void supprimer(Promotion promotion) {
        service.supprimerPromotion(promotion.getId());
    }
}