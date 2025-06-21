-- Trading Sandbox Database Initialization

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Strategies table
CREATE TABLE IF NOT EXISTS strategies (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    strategy_type VARCHAR(50) NOT NULL,
    parameters JSONB,
    rules JSONB,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Portfolios table
CREATE TABLE IF NOT EXISTS portfolios (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    name VARCHAR(100) NOT NULL,
    initial_capital DECIMAL(15,2) NOT NULL,
    current_capital DECIMAL(15,2) NOT NULL,
    total_pnl DECIMAL(15,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trades table
CREATE TABLE IF NOT EXISTS trades (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT REFERENCES portfolios(id),
    strategy_id BIGINT REFERENCES strategies(id),
    symbol VARCHAR(20) NOT NULL,
    side VARCHAR(10) NOT NULL, -- BUY, SELL
    quantity INTEGER NOT NULL,
    price DECIMAL(10,4) NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, FILLED, CANCELLED
    executed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Market data table
CREATE TABLE IF NOT EXISTS market_data (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    open_price DECIMAL(10,4),
    high_price DECIMAL(10,4),
    low_price DECIMAL(10,4),
    close_price DECIMAL(10,4),
    volume BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Positions table
CREATE TABLE IF NOT EXISTS positions (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT REFERENCES portfolios(id),
    symbol VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL,
    average_price DECIMAL(10,4) NOT NULL,
    current_price DECIMAL(10,4),
    unrealized_pnl DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_strategies_user_id ON strategies(user_id);
CREATE INDEX IF NOT EXISTS idx_portfolios_user_id ON portfolios(user_id);
CREATE INDEX IF NOT EXISTS idx_trades_portfolio_id ON trades(portfolio_id);
CREATE INDEX IF NOT EXISTS idx_trades_symbol ON trades(symbol);
CREATE INDEX IF NOT EXISTS idx_market_data_symbol_timestamp ON market_data(symbol, timestamp);
CREATE INDEX IF NOT EXISTS idx_positions_portfolio_symbol ON positions(portfolio_id, symbol);

-- Insert sample data
INSERT INTO users (username, email, password_hash, first_name, last_name) VALUES
('admin', 'admin@tradingsandbox.com', '$2a$10$rAMyM6L6UZ6UZ6UZ6UZ6UO', 'Admin', 'User'),
('trader1', 'trader1@example.com', '$2a$10$rAMyM6L6UZ6UZ6UZ6UZ6UO', 'John', 'Trader'),
('trader2', 'trader2@example.com', '$2a$10$rAMyM6L6UZ6UZ6UZ6UZ6UO', 'Jane', 'Investor')
ON CONFLICT (username) DO NOTHING;

-- Insert demo portfolios for users
INSERT INTO portfolios (user_id, name, initial_capital, current_capital, total_pnl, status)
SELECT id, 'Admin Portfolio', 100000, 100000, 0, 'ACTIVE' FROM users WHERE username = 'admin' ON CONFLICT DO NOTHING;
INSERT INTO portfolios (user_id, name, initial_capital, current_capital, total_pnl, status)
SELECT id, 'Trader1 Growth', 50000, 52000, 2000, 'ACTIVE' FROM users WHERE username = 'trader1' ON CONFLICT DO NOTHING;
INSERT INTO portfolios (user_id, name, initial_capital, current_capital, total_pnl, status)
SELECT id, 'Trader2 Value', 75000, 76000, 1000, 'ACTIVE' FROM users WHERE username = 'trader2' ON CONFLICT DO NOTHING;

-- Insert demo strategies for users
INSERT INTO strategies (user_id, name, description, strategy_type, parameters, rules, is_active)
SELECT id, 'Momentum Strategy', 'A simple momentum-based strategy.', 'MOMENTUM', '{}', '{}', true FROM users WHERE username = 'admin' ON CONFLICT DO NOTHING;
INSERT INTO strategies (user_id, name, description, strategy_type, parameters, rules, is_active)
SELECT id, 'Mean Reversion', 'A mean reversion strategy for equities.', 'MEAN_REVERSION', '{}', '{}', true FROM users WHERE username = 'trader1' ON CONFLICT DO NOTHING;
INSERT INTO strategies (user_id, name, description, strategy_type, parameters, rules, is_active)
SELECT id, 'RSI Crossover', 'RSI-based crossover strategy.', 'RSI', '{}', '{}', true FROM users WHERE username = 'trader2' ON CONFLICT DO NOTHING; 