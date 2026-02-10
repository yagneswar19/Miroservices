# Rewards360 Frontend (my-react-app)

This is the Vite + React frontend for the CustomerMS Rewards demo. It communicates with the Spring Boot backend at `/api/customers` via axios.

## What changed in the backend
- Added `CorsConfig.java` at `CustomerMS/src/main/java/com/customer/CustomerMS/config/CorsConfig.java` to allow cross-origin requests from dev servers (http://localhost:5173 and http://localhost:3000).
- Fixed a controller mapping bug: `/offers/{my-tier}` -> `/offers/{tier}` in `CustomerContoller.java`.

## How the frontend is structured
- `src/api.js` — axios instance. Reads `VITE_API_BASE_URL` from env or defaults to `http://localhost:8080/api/customers`.
- `src/services/userService.js` — all API calls (profile, transactions, offers, redemptions, claim, redeem, addCustomer).
- `src/context/UserContext.jsx` — central data provider for pages.
- `src/App.jsx`, `src/main.jsx` — app wiring and routes.
- `src/pages/user/*` — Dashboard, Offers, Profile, Redemptions, Transactions pages.
- `src/Components/Header.jsx` — top navigation.
- `src/styles/*` — CSS used by pages.

## Run locally (Windows PowerShell)
1. Start backend (from repository root `CustomerMS`):

```powershell
cd 'C:\Users\2460623\Downloads\CustomerMS (1)\CustomerMS'
.\mvnw spring-boot:run
```

Backend default: http://localhost:8080

2. Start frontend (from `frontend/my-react-app`):

```powershell
cd 'C:\Users\2460623\Downloads\CustomerMS (1)\frontend\my-react-app'
npm install
# optional: copy .env.example to .env and adjust VITE_API_BASE_URL
copy .env.example .env
npm run dev
```

Frontend default: http://localhost:5173

## API examples (axios)
The project already includes a centralized axios instance (`src/api.js`). Example calls (the `userService` already implements these):

- Get profile: `GET /profile/{id}`
- Get transactions: `GET /{customerId}/transactions`
- Get offers: `GET /offers`
- Get redemptions: `GET /{customerId}/redemptions`
- Add customer: `POST /addCustomer` (body: CustomerProfile)
- Claim points: `POST /claim/{customerId}` (body: ClaimRequest)
- Redeem offer: `POST /redeem/{customerId}` (body: RedeemRequest)

## Notes & next steps
- CORS configuration used is permissive for development. Lock origins in production.
- If you want a dev proxy (avoid absolute host), update `vite.config.js` with a proxy for `/api`.
- If you want, I can:
  - create a zip of the frontend directory for download, or
  - paste the full frontend source files here, or
  - run the backend and frontend and perform an end-to-end test now and share outputs.

If you'd like the entire frontend source printed here, tell me and I will paste each file. Otherwise, follow the steps above to run the app.
