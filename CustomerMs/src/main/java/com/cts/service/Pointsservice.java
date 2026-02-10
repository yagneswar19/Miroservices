package com.cts.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.cts.repository.TransactionRepository;
import com.cts.repository.UserRepository;
import com.cts.repository.RedemptionRepository;
import com.cts.entity.User;
import com.cts.entity.Redemption;
import com.cts.entity.Transaction;
import com.cts.entity.CustomerProfile;
import com.cts.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.cts.feign.PromotionFeignClient;
@Service
public class Pointsservice {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PromotionFeignClient promotionFeignClient;
    
    @Autowired
    private RedemptionRepository redemptionRepository;

    /**
     * Redeem an offer for a user
     * Deducts points from user's profile and creates a redemption record
     */
    // @Transactional
    // public Redemption redeemOffer(Long userId, RedeemRequest request) {
    //     // Fetch user
    //     User user = userRepository.findById(userId)
    //         .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
    //     // Fetch offer
    //     Offers offer = offerRepository.findById(request.getOfferId())
    //         .orElseThrow(() -> new RuntimeException("Offer not found with id: " + request.getOfferId()));
        
    //     // Check if offer is active
    //     if (!offer.isActive()) {
    //         throw new RuntimeException("Offer is not active");
    //     }
        
    //     // Get customer profile
    //     CustomerProfile profile = user.getProfile();
    //     if (profile == null) {
    //         throw new RuntimeException("Customer profile not found for user: " + userId);
    //     }
        
    //     // Check if user has enough points
    //     if (profile.getPointsBalance() < offer.getCostPoints()) {
    //         throw new RuntimeException("Insufficient points. Required: " + offer.getCostPoints() 
    //             + ", Available: " + profile.getPointsBalance());
    //     }
        
    //     // Deduct points from profile
    //     profile.setPointsBalance(profile.getPointsBalance() - offer.getCostPoints());
    //     userRepository.save(user);
        
    //     Transaction transaction = new Transaction();
    //     transaction.setExternalId("RED-" + System.currentTimeMillis());
    //     transaction.setType("REDEMPTION");
    //     transaction.setPointsEarned(0);
    //     transaction.setPointsRedeemed(offer.getCostPoints());
    //     transaction.setStore(request.getStore());
    //     transaction.setDate(LocalDate.now());
    //     transaction.setNote(offer.getTitle());
    //     transaction.setUser(user);
        
    //     transactionRepository.save(transaction);

    //     // Create redemption record
    //     Redemption redemption = new Redemption();
    //     redemption.setTransactionId(transaction.getExternalId());
    //     redemption.setConfirmationCode("CONF-" + System.currentTimeMillis());
    //     redemption.setDate(LocalDate.now());
    //     redemption.setCostPoints(offer.getCostPoints());
    //     redemption.setOfferTitle(offer.getTitle());
    //     redemption.setStore(request.getStore());
    //     redemption.setUser(user);

    //     redemptionRepository.save(redemption);
    //     userRepository.save(user);

    //     return redemption;
    // }


    /**
     * Redeem an offer for a user
     * Deducts points from user's profile and creates a redemption record
     * Fetches offer details from Promotion Service via Feign client
     */
    @Transactional
    public Redemption redeemOffer(Long userId, RedeemRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Fetch specific offer details from Promotion Service via Feign
        OfferDto offer = promotionFeignClient.getOfferById(request.getOfferId());
        
        CustomerProfile profile = user.getProfile();
        if (profile.getPointsBalance() < offer.getCostPoints()) {
            throw new RuntimeException("Insufficient points");
        }
        
        // Deduct points
        profile.setPointsBalance(profile.getPointsBalance() - offer.getCostPoints());
        
        // Create Transaction
        Transaction transaction = new Transaction();
        transaction.setExternalId("RED-" + System.currentTimeMillis());
        transaction.setType("REDEMPTION");
        transaction.setPointsRedeemed(offer.getCostPoints());
        transaction.setNote(offer.getTitle());
        transaction.setUser(user);
        transaction.setDate(LocalDate.now());
        transactionRepository.save(transaction);

        // Create Redemption record
        Redemption redemption = new Redemption();
        redemption.setOfferTitle(offer.getTitle());
        redemption.setCostPoints(offer.getCostPoints());
        redemption.setUser(user);
        redemption.setDate(LocalDate.now());
        redemptionRepository.save(redemption);

        userRepository.save(user);
        return redemption;
    }

  private static final List<String> TIER_ORDER = List.of("BRONZE", "SILVER", "GOLD", "PLATINUM");

    public List<OfferDto> getOffersByTier(String userTier) {
        // 1. Get all offers from the other service via Feign
        List<OfferDto> allOffers = promotionFeignClient.getAllOffers();
        
        // 2. Prepare a list to store the results
        List<OfferDto> eligibleOffers = new ArrayList<>();

        // 3. Determine the user's tier rank
        int userTierRank = TIER_ORDER.indexOf(userTier.toUpperCase());
        
        // If the tier is unknown, default to the lowest (Bronze)
        if (userTierRank == -1) {
            userTierRank = 0; 
        }

        // 4. Create a sub-list of all allowed tiers (e.g., ["BRONZE", "SILVER"])
        List<String> allowedTiers = TIER_ORDER.subList(0, userTierRank + 1);

        // 5. Use a standard loop to filter
        for (OfferDto offer : allOffers) {
            if (offer.getTierLevel() != null) {
                String offerTier = offer.getTierLevel().toUpperCase();
                
                // Check if the offer's tier is within the user's allowed list
                if (allowedTiers.contains(offerTier)) {
                    eligibleOffers.add(offer);
                }
            }
        }

        return eligibleOffers;
    }

   /**
     * Claim an offer for a user
     * Only adds points to user's profile and creates a transaction entry
     * Does NOT create a redemption record
     */
    @Transactional
    public ClaimRequest claimOffer(ClaimRequest claimRequest, Long userId) {
        // Fetch user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Get customer profile
        CustomerProfile profile = user.getProfile();
        if (profile == null) {
            throw new RuntimeException("Customer profile not found for user: " + userId);
        }
        
        // Add points to profile
        int previousBalance = profile.getPointsBalance();
        profile.setPointsBalance(previousBalance + claimRequest.getPoints());
        userRepository.save(user);
        
        // Create transaction record only (no redemption entry)
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType("CLAIM");
        transaction.setPointsEarned(claimRequest.getPoints());
        transaction.setPointsRedeemed(0);
        transaction.setStore("Claim");
        transaction.setDate(LocalDate.now());
        transaction.setExpiry(LocalDate.now().plusYears(1)); // Points expire in 1 year
        transaction.setNote("Claimed: " + claimRequest.getNote());
        String transactionId = "CLM-" + System.currentTimeMillis();
        transaction.setExternalId(transactionId);
        transactionRepository.save(transaction);
        
        // Return response DTO
      
      
        return claimRequest;
    }

    /**
     * Get all redemptions for a specific user
     */
    public List<Redemption> getRedemptionsByUserId(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Return redemptions sorted by date descending
        return redemptionRepository.findByUserIdOrderByDateDesc(userId);
    }

    /**
     * Get all transactions for a specific user
     */
    public List<Transaction> getTransactionsByUserId(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Return transactions for the user sorted by date descending
        return transactionRepository.findByUserIdOrderByDateDesc(userId);
    }



    /**
     * Get all offers available for a specific tier
     */
    // public List<Offers> getOffersByTier(String tier) {
    //     return offerRepository.findByTier(tier);
    // }
}   
