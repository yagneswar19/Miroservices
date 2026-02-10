package com.example.Offers.controller;  

import com.example.Offers.model.Promotion;
import com.example.Offers.model.Analytics;
import com.example.Offers.repository.AnalyticsRepository;
import com.example.Offers.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@CrossOrigin(origins = "*")
public class PromotionController {

    @Autowired
    private PromotionService service;

    @Autowired
    private AnalyticsRepository analyticsRepo;

    /**
     * Saves a new promotion and automatically triggers
     * the creation of an associated analytics record.
     */
    @PostMapping("/promotions")
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        Promotion savedPromotion = service.savePromotionWithAnalytics(promotion);
        return ResponseEntity.ok(savedPromotion);
    }

    /**
     * Retrieves all promotion records.
     */
    @GetMapping("/promotions")
    public List<Promotion> getAllPromotions() {
        return service.getAllPromotions();
    }
     @GetMapping("/api/promotions/{id}")
    public ResponseEntity<Promotion> getOfferById(@PathVariable("id") Long id) {
        Promotion promotion = service.getPromotionById(id);
        if (promotion != null) {
            return ResponseEntity.ok(promotion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all data from the analytics table.
     */
    @GetMapping("/analytics")
    public List<Analytics> getAnalyticsRecords() {
        return analyticsRepo.findAll();
    }
}