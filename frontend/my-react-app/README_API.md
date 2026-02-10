API integration notes

- Base API URL used by the frontend axios instance: VITE_API_BASE_URL env var (Vite). If not set, defaults to http://localhost:8080/api/customers

- Files added:
  - `src/services/api.js` — central axios instance (uses VITE_API_BASE_URL)
  - `src/services/customerService.js` — thin wrappers for backend endpoints (GET profile, transactions, offers, redemptions; POST claim/redeem; addCustomer)
  - `src/context/UserContext.jsx` — React context provider that fetches profile/transactions/offers/redemptions and exposes `claimPoints` and `redeemOffer` functions used by the UI

- How to run locally:
  1. Start the backend Spring Boot app (default port 8080). If backend runs on a different port or host, set `VITE_API_BASE_URL` in a `.env` file at the project root or in your environment. Example for Vite (create `.env` in `my-react-app`):

     VITE_API_BASE_URL=http://localhost:8080/api/customers

  2. From `frontend/my-react-app` run:

     npm install
     npm run dev

- Notes on shape expectations:
  - Claim request body: { activityCode: string, points: number, note: string }
  - Redeem request body: { offerId: number, store: string }
  - GET endpoints return raw JSON arrays or objects as the Spring controllers return (no ApiResponse wrapper used in controllers above)
