  # 🚀 TradeShift Frontend  
A modern, high-performance React + Vite + TailwindCSS frontend for **multi-asset portfolio tracking**, **real-time market data**, and **simulated trading**.

This project connects to the TradeShift Spring Boot backend and uses WebSockets, JWT authentication, and Recharts for visual analytics.

---

## 📌 Features

### ✔ Authentication
- Secure login using JWT
- Persistent session using localStorage
- Protected routes using React Router

### ✔ Portfolio Management
- Fetch portfolio positions from SQL-backed API
- Real-time price updates injected into portfolio values
- P/L calculations updated live

### ✔ Charts & Visualization
- Portfolio value history displayed via Recharts
- Smooth, responsive charts

### ✔ Market Research (MongoDB)
- Search stocks
- Fetch company details
- View analytics/info for selected symbols

### ✔ Trading Simulator
- Simulated buy/sell orders
- Updates portfolio + history instantly

### ✔ Real-time WebSocket Feed
- Auto-updating price ticker
- Push-based market feed

---

## 🏗️ Tech Stack

| Category | Technologies |
|---------|--------------|
| Frontend | **React, Vite, TailwindCSS, Recharts** |
| Auth | **JWT, React Context** |
| Realtime | **WebSocket API** |
| State | React Hooks (Context, Reducers) |
| Build Tools | Vite |
| Language | JavaScript (ES2022) |

---

## 📁 Project Structure

