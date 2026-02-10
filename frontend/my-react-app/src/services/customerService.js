import api from './api'

export const getProfile = async (id) => {
  const res = await api.get(`/profile/${id}`)
  return res.data
}

export const getTransactions = async (customerId) => {
  const res = await api.get(`/${customerId}/transactions`)
  return res.data
}

export const getOffers = async () => {
  const res = await api.get('/offers')
  return res.data
}

export const getOffersForTier = async (tier) => {
  const res = await api.get(`/offers/${tier}`)
  return res.data
}

export const getRedemptions = async (customerId) => {
  const res = await api.get(`/${customerId}/redemptions`)
  return res.data
}

export const addCustomer = async (customer) => {
  const res = await api.post('/addCustomer', customer)
  return res.data
}

export const claimPoints = async (customerId, { activityCode, points, note }) => {
  const res = await api.post(`/claim/${customerId}`, { activityCode, points, note })
  return res.data
}

export const redeemOffer = async (customerId, { offerId, store }) => {
  const res = await api.post(`/redeem/${customerId}`, { offerId, store })
  return res.data
}
