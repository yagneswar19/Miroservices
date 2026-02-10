import axios from 'axios';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_BASE_URL || '/api', // use proxy in dev
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: false // set true if you use cookies
});

export default api;