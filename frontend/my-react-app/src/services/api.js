import axios from 'axios'

// Vite env var: VITE_API_BASE_URL, fallback to Spring Boot default
const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/customers'

const api = axios.create({
  baseURL: API_BASE,
  headers: { 'Content-Type': 'application/json' },
})

export default api
