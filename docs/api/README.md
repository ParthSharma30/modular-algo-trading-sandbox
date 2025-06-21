# Trading Sandbox API Documentation

## Overview

The Trading Sandbox API provides a comprehensive set of endpoints for managing algorithmic trading strategies, portfolios, and market data in a simulated environment.

## Base URL

- **Development**: `http://localhost:8080/api`
- **Production**: `https://your-domain.com/api`

## Authentication

The API uses JWT (JSON Web Token) authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Endpoints

### Authentication

#### POST /auth/login
Authenticate user and receive JWT token.

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "token": "string",
  "refreshToken": "string",
  "expiresAt": "2024-01-01T12:00:00Z",
  "user": {
    "id": 1,
    "username": "string",
    "email": "string",
    "firstName": "string",
    "lastName": "string",
    "isActive": true,
    "createdAt": "2024-01-01T12:00:00Z",
    "updatedAt": "2024-01-01T12:00:00Z"
  }
}
```

#### POST /auth/register
Register a new user.

**Request Body:**
```json
{
  "username": "string",
  "email": "string",
  "firstName": "string",
  "lastName": "string"
}
```

**Query Parameters:**
- `password` (string, required): User password

#### POST /auth/refresh
Refresh JWT token.

**Query Parameters:**
- `refreshToken` (string, required): Refresh token

### Strategies

#### GET /strategies
Get all strategies for the current user.

**Response:**
```json
[
  {
    "id": 1,
    "name": "string",
    "description": "string",
    "strategyType": "string",
    "parameters": {},
    "rules": {},
    "isActive": true,
    "userId": 1,
    "createdAt": "2024-01-01T12:00:00Z",
    "updatedAt": "2024-01-01T12:00:00Z"
  }
]
```

#### GET /strategies/{id}
Get a specific strategy by ID.

#### POST /strategies
Create a new strategy.

**Request Body:**
```json
{
  "name": "string",
  "description": "string",
  "strategyType": "string",
  "parameters": {},
  "rules": {}
}
```

#### PUT /strategies/{id}
Update an existing strategy.

#### DELETE /strategies/{id}
Delete a strategy.

#### POST /strategies/{id}/execute
Execute a strategy.

**Query Parameters:**
- `portfolioId` (number, required): Portfolio ID to execute strategy on

#### POST /strategies/{id}/backtest
Run backtesting on a strategy.

**Query Parameters:**
- `startDate` (string, required): Start date (ISO format)
- `endDate` (string, required): End date (ISO format)

### Portfolios

#### GET /portfolios
Get all portfolios for the current user.

**Response:**
```json
[
  {
    "id": 1,
    "name": "string",
    "initialCapital": 100000.00,
    "currentCapital": 105000.00,
    "totalPnl": 5000.00,
    "status": "ACTIVE",
    "userId": 1,
    "createdAt": "2024-01-01T12:00:00Z",
    "updatedAt": "2024-01-01T12:00:00Z"
  }
]
```

#### GET /portfolios/{id}
Get a specific portfolio by ID.

#### POST /portfolios
Create a new portfolio.

**Request Body:**
```json
{
  "name": "string",
  "initialCapital": 100000.00
}
```

#### PUT /portfolios/{id}
Update an existing portfolio.

#### DELETE /portfolios/{id}
Delete a portfolio.

#### GET /portfolios/{id}/value
Get current portfolio value.

### Market Data

#### GET /market-data
Get all market data records.

#### GET /market-data/{symbol}
Get market data for a specific symbol.

#### GET /market-data/{symbol}/latest
Get the latest market data for a symbol.

**Response:**
```json
{
  "id": 1,
  "symbol": "AAPL",
  "timestamp": "2024-01-01T12:00:00Z",
  "openPrice": 150.00,
  "highPrice": 152.00,
  "lowPrice": 149.00,
  "closePrice": 151.00,
  "volume": 1000000,
  "createdAt": "2024-01-01T12:00:00Z"
}
```

#### GET /market-data/{symbol}/history
Get historical market data.

**Query Parameters:**
- `startDate` (string, required): Start date (ISO format)
- `endDate` (string, required): End date (ISO format)

#### POST /market-data/{symbol}/simulate
Start market data simulation.

**Query Parameters:**
- `speedMultiplier` (number, optional): Simulation speed multiplier (default: 1.0)

#### POST /market-data/{symbol}/generate
Generate simulated market data.

## WebSocket Endpoints

### /ws
WebSocket endpoint for real-time updates.

**Message Types:**
- `MARKET_DATA_UPDATE`: Real-time market data updates
- `TRADE_EXECUTED`: Trade execution notifications
- `PORTFOLIO_UPDATE`: Portfolio value updates
- `STRATEGY_SIGNAL`: Strategy signal notifications

## Error Responses

All endpoints return standard HTTP status codes. Error responses include:

```json
{
  "message": "Error description",
  "status": 400,
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## Rate Limiting

- **Authentication endpoints**: 10 requests per minute
- **API endpoints**: 100 requests per minute per user
- **WebSocket connections**: 5 concurrent connections per user

## SDKs and Libraries

### JavaScript/TypeScript
```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

### Python
```python
import requests

headers = {'Authorization': f'Bearer {token}'}
response = requests.get('http://localhost:8080/api/strategies', headers=headers)
```

## Examples

### Creating a Strategy
```bash
curl -X POST http://localhost:8080/api/strategies \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Moving Average Crossover",
    "description": "Simple trend following strategy",
    "strategyType": "TREND_FOLLOWING",
    "parameters": {
      "shortPeriod": 10,
      "longPeriod": 30
    },
    "rules": [
      {
        "condition": "short_ma > long_ma",
        "action": "BUY",
        "quantity": 100
      }
    ]
  }'
```

### Executing a Strategy
```bash
curl -X POST "http://localhost:8080/api/strategies/1/execute?portfolioId=1" \
  -H "Authorization: Bearer <token>"
```

### Getting Market Data
```bash
curl -X GET "http://localhost:8080/api/market-data/AAPL/latest" \
  -H "Authorization: Bearer <token>"
``` 