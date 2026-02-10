import React from 'react'
import { NavLink } from 'react-router-dom'
import '../styles/Header.css'

const NavItem = ({ to, children }) => (
  <NavLink to={to} className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}>
    {children}
  </NavLink>
)

export default function Header() {
  return (
    <header className="app-header">
      <div className="app-brand">
        <a href="/" className="brand-link">Rewards360</a>
      </div>

      <nav className="nav">
        <NavItem to="/">Dashboard</NavItem>
        <NavItem to="/user/offers">Offers</NavItem>
        <NavItem to="/user/profile">Profile</NavItem>
        <NavItem to="/user/redemptions">Redemptions</NavItem>
        <NavItem to="/user/transactions">Transactions</NavItem>
      </nav>
    </header>
  )
}
