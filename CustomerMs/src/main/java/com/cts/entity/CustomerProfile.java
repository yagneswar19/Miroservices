package com.cts.entity;

import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="CustomerProfile")
public class CustomerProfile {
    
      @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loyaltyTier;
    private int pointsBalance;
    private LocalDate nextExpiry;
    private String preferences; // CSV of categories
    private String communication; // Email/SMS/WhatsApp
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLoyaltyTier() {
        return loyaltyTier;
    }
    public void setLoyaltyTier(String loyaltyTier) {
        this.loyaltyTier = loyaltyTier;
    }
    public int getPointsBalance() {
        return pointsBalance;
    }
    public void setPointsBalance(int pointsBalance) {
        this.pointsBalance = pointsBalance;
    }
    public LocalDate getNextExpiry() {
        return nextExpiry;
    }
    public void setNextExpiry(LocalDate nextExpiry) {
        this.nextExpiry = nextExpiry;
    }
    public String getPreferences() {
        return preferences;
    }
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
    public String getCommunication() {
        return communication;
    }
    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public CustomerProfile() {
    }
    public CustomerProfile(Long id, String loyaltyTier, int pointsBalance, LocalDate nextExpiry, String preferences,
            String communication, User user) {
        this.id = id;
        this.loyaltyTier = loyaltyTier;
        this.pointsBalance = pointsBalance;
        this.nextExpiry = nextExpiry;
        this.preferences = preferences;
        this.communication = communication;
        this.user = user;
    }
}
