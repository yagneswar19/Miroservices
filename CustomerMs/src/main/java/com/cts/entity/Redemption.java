package com.cts.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
@Entity
@Table(name="Redemption")
public class Redemption {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String confirmationCode;
    private String transactionId;
    private LocalDate date;
    private int costPoints;
    private String offerTitle;
    private String store;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore 
    private User user;
    // Getters and Setters
    public Long getId() {   
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getConfirmationCode() {
        return confirmationCode;
    }
    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public int getCostPoints() {
        return costPoints;
    }
    public void setCostPoints(int costPoints) {
        this.costPoints = costPoints;
    }
    public String getOfferTitle() {
        return offerTitle;
    }
    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }
    public String getStore() {
        return store;
    }
    public void setStore(String store) {
        this.store = store;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public Redemption() {
    }
    public Redemption(Long id, String confirmationCode, String transactionId, LocalDate date, int costPoints,
            String offerTitle, String store, User user) {
        this.id = id;
        this.confirmationCode = confirmationCode;
        this.transactionId = transactionId;
        this.date = date;
        this.costPoints = costPoints;
        this.offerTitle = offerTitle;
        this.store = store;
        this.user = user;
    }
}
