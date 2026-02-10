import React, { createContext, useContext, useEffect, useState } from 'react'
import * as customerService from '../services/customerService'

const UserContext = createContext(null)

export const UserProvider = ({ children, initialCustomerId = 1 }) => {
  const [user, setUser] = useState(null)
  const [transactions, setTransactions] = useState([])
  const [offers, setOffers] = useState([])
  const [redemptions, setRedemptions] = useState([])
  const [loading, setLoading] = useState(true)

  const customerId = initialCustomerId

  const loadAll = async () => {
    try {
      setLoading(true)
      const [profile, txs, offersList, redems] = await Promise.all([
        customerService.getProfile(customerId).catch(() => null),
        customerService.getTransactions(customerId).catch(() => []),
        customerService.getOffers().catch(() => []),
        customerService.getRedemptions(customerId).catch(() => []),
      ])

      // Map profile to shape Dashboard expects: user.profile and user.name
      const displayName = profile?.name || profile?.firstName || `Member ${profile?.customerid ?? ''}`
      setUser({ name: displayName, profile: profile || {} })
      setTransactions(txs || [])
      setOffers(offersList || [])
      setRedemptions(redems || [])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadAll()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [customerId])

  const refreshProfileAndTx = async () => {
    const profile = await customerService.getProfile(customerId)
    const txs = await customerService.getTransactions(customerId)
    const displayName = profile?.name || profile?.firstName || `Member ${profile?.customerid ?? ''}`
    setUser({ name: displayName, profile })
    setTransactions(txs || [])
  }

  const claimPoints = async (activityCode, points, note) => {
    // Backend requires ClaimRequest: { activityCode, points, note }
    const result = await customerService.claimPoints(customerId, { activityCode, points, note })
    // Refresh profile and transactions after successful claim
    await refreshProfileAndTx()
    return result
  }

  const redeemOffer = async (offerId, store) => {
    const result = await customerService.redeemOffer(customerId, { offerId, store })
    await refreshProfileAndTx()
    return result
  }

  return (
    <UserContext.Provider value={{ user, transactions, offers, redemptions, loading, claimPoints, redeemOffer }}>
      {children}
    </UserContext.Provider>
  )
}

export const useUser = () => {
  const ctx = useContext(UserContext)
  if (!ctx) throw new Error('useUser must be used within UserProvider')
  return ctx
}
