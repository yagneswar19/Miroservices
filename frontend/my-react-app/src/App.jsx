import React from 'react'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './App.css'
import Header from './Components/Header'
import Dashboard from './pages/user/Dashboard'
import Offers from './pages/user/Offers'
import Profile from './pages/user/Profile'
import Redemptions from './pages/user/Redemptions'
import Transactions from './pages/user/Transactions'
import { UserProvider } from './context/UserContext'

export default function App() {
  return (
    <UserProvider>
      <BrowserRouter>
        <Header />
        <main style={{ padding: 16 }}>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/user/offers" element={<Offers />} />
            <Route path="/user/profile" element={<Profile />} />
            <Route path="/user/redemptions" element={<Redemptions />} />
            <Route path="/user/transactions" element={<Transactions />} />
          </Routes>
        </main>
      </BrowserRouter>
    </UserProvider>
  )
}
