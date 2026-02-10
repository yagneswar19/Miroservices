import api from './axiosInstance';

// Claim points (POST)
// Endpoint: POST /api/users/claim/user/{userId}
export const claimPoints = (userId, claimRequest) =>
  api.post(`/users/claim/user/${userId}`, claimRequest);

// Redeem offer (POST)
// Endpoint mapping: POST /api/users/redeem/offer/{offerId}/user/{userId}
// But controller uses RedeemRequest { offerId, store } in body — include offerId in body.
export const redeemOffer = (userId, offerId, store) => {
  const body = { offerId, store };
  return api.post(`/users/redeem/offer/${offerId}/user/${userId}`, body);
};

// Get transactions for user
export const getTransactions = (userId) =>
  api.get(`/users/transactions/user/${userId}`);

// Get redemptions for user
export const getRedemptions = (userId) =>
  api.get(`/users/redemptions/user/${userId}`);

// Get offers by tier (note controller uses 'teir' in path)
export const getOffersByTier = (tier) =>
  api.get(`/users/offers/teir/${tier}`);