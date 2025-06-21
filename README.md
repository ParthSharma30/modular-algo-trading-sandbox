# Modular Algorithmic Trading Sandbox

A modular, cloud-ready algorithmic trading platform for educational and experimental use. This system allows users to build, test, and simulate custom trading strategies on synthetic or historical financial data in a containerized and scalable environment.

---

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Frontend│    │  Spring Boot    │    │   PostgreSQL    │
│   (Port 3000)   │◄──►│   Backend       │◄──►│   Database      │
│                 │    │   (Port 8080)   │    │   (Port 5432)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │   Market Data   │
                       │   Engine        │
                       └─────────────────┘
```

---

## 📖 Project Guide: How Everything Works

### 1. User Journey

1. **User Registration & Login**
   - Users register and log in via the React frontend.
   - Authentication is handled by the backend using JWT tokens.

2. **Strategy Management**
   - Users can create, upload, or select trading strategies (JSON format) via the dashboard.
   - Strategies are validated and stored in the backend.

3. **Simulation & Backtesting**
   - Users choose between backtesting (historical data) or live simulation (synthetic/streamed data).
   - The backend executes the strategy logic, simulates order placement, and tracks portfolio performance.

4. **Visualization**
   - Results (P&L, trades, charts) are streamed to the frontend in real time via WebSocket.
   - Users can analyze performance and iterate on strategies.

### 2. Component Roles

- **Frontend (React + TypeScript)**: User interface for authentication, strategy management, simulation controls, and real-time visualization.
- **Backend (Spring Boot)**: Handles authentication, strategy execution, order management, portfolio tracking, and exposes REST/WebSocket APIs.
- **Database (PostgreSQL)**: Stores user data, strategies, portfolios, and trade history.
- **Market Data Engine**: Simulates or fetches market data for use in backtesting and live simulation.

### 3. System Flow Diagram

```
graph TD;
  "User" --> "Frontend (React)";
  "Frontend (React)" -->|REST/JWT| "Backend (Spring Boot)";
  "Backend (Spring Boot)" -->|JPA| "Database (PostgreSQL)";
  "Backend (Spring Boot)" -->|WebSocket| "Frontend (React)";
  "Backend (Spring Boot)" --> "Market Data Engine";
```

---

## 🚀 Features

- **Market Data Engine**: Simulates or fetches price data (equities, FX, crypto)
- **Strategy Executor**: Accepts user-defined strategies and executes logic
- **Order Management System (OMS)**: Simulates order placement, fills, cancellations
- **Portfolio Tracker**: Tracks P&L, positions, and exposure over time
- **Visualization Dashboard**: Real-time charts, trades, and portfolio metrics
- **Backtesting & Live Simulation**: Run strategies on historical or synthetic data
- **JWT Authentication & Security**: Secure endpoints and sandboxed strategy execution

---

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.x (Java 17)
- **Frontend**: React.js 18 (TypeScript)
- **Database**: PostgreSQL 15
- **Containerization**: Docker & Docker Compose
- **Real-time**: WebSocket
- **Charts**: Chart.js
- **API Docs**: Swagger/OpenAPI

---

## 📁 Project Structure

```
modular-algo-trading-sandbox/
├── backend/                 # Spring Boot application
│   ├── src/main/java/
│   │   └── com/trading/
│   │       ├── controller/  # REST controllers
│   │       ├── service/     # Business logic
│   │       ├── model/       # Entity classes
│   │       ├── dto/         # Data transfer objects
│   │       ├── config/      # Configuration classes
│   │       └── util/        # Utility classes
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                # React application
│   ├── src/
│   │   ├── components/      # React components
│   │   ├── pages/           # Page components
│   │   ├── services/        # API services
│   │   ├── types/           # TypeScript types
│   │   └── utils/           # Utility functions
│   ├── Dockerfile
│   └── package.json
├── docker-compose.yml       # Multi-container setup
└── docs/                    # Documentation
    ├── api/                 # API documentation
    └── strategies/          # Sample strategies
```

---

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 17 (for backend development)
- Node.js 18+ (for frontend development)

### Running with Docker Compose

```bash
# Clone and navigate to the project
cd modular-algo-trading-sandbox

# Start all services
docker-compose up -d
```

- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- API Docs: http://localhost:8080/swagger-ui.html
- Database: localhost:5432

### Local Development

**Backend (Spring Boot):**
```bash
cd backend
./mvnw spring-boot:run
```

**Frontend (React):**
```bash
cd frontend
npm install
npm start
```

---

## 🔧 Configuration

Create a `.env` file in the root directory:

```env
# Database
POSTGRES_DB=trading_sandbox
POSTGRES_USER=trading_user
POSTGRES_PASSWORD=secure_password

# JWT
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000

# Market Data
ALPHA_VANTAGE_API_KEY=your_api_key
```

---

## 📊 Sample Strategy Format

```json
{
  "name": "Moving Average Crossover",
  "symbol": "AAPL",
  "parameters": {
    "shortPeriod": 10,
    "longPeriod": 30,
    "positionSize": 100
  },
  "rules": [
    {
      "condition": "short_ma > long_ma",
      "action": "BUY",
      "quantity": "positionSize"
    },
    {
      "condition": "short_ma < long_ma",
      "action": "SELL",
      "quantity": "ALL"
    }
  ]
}
```

---

## 📈 API Endpoints (Summary)

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/refresh` - Refresh JWT token
- `GET /api/strategies` - List all strategies
- `POST /api/strategies` - Create new strategy
- `GET /api/strategies/{id}` - Get strategy details
- `PUT /api/strategies/{id}` - Update strategy
- `DELETE /api/strategies/{id}` - Delete strategy
- `GET /api/portfolios` - List user portfolios
- `POST /api/portfolios` - Create new portfolio
- `GET /api/portfolios/{id}` - Get portfolio details
- `GET /api/portfolios/{id}/trades` - Get portfolio trades
- `GET /api/market-data/{symbol}` - Get current market data
- `GET /api/market-data/{symbol}/history` - Get historical data
- `POST /api/market-data/simulate` - Start simulation

---

## 🔒 Security

- JWT-based authentication
- Strategy sandboxing prevents malicious code execution
- Input validation and sanitization
- Rate limiting on API endpoints

---

## 🚀 Deployment

### AWS EC2 Example

```bash
# Install Docker on EC2
sudo yum update -y
sudo yum install -y docker
sudo service docker start
sudo usermod -a -G docker ec2-user

# Deploy with Docker Compose
cd modular-algo-trading-sandbox
docker-compose -f docker-compose.prod.yml up -d
```

### ECS Deployment

Use the provided ECS task definitions and service configurations in the `docs/deployment/` directory.

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

---

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 🆘 Support

- Create an issue in the repository
- Check the documentation in the `docs/` directory
- Review the API documentation at `/swagger-ui.html` 