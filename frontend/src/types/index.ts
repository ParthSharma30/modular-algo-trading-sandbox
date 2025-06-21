// User types
export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

// Authentication types
export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  expiresAt: string;
  user: User;
}

// Strategy types
export interface Strategy {
  id: number;
  name: string;
  description?: string;
  strategyType: string;
  parameters?: Record<string, any>;
  rules?: Record<string, any>;
  isActive: boolean;
  userId: number;
  createdAt: string;
  updatedAt: string;
}

// Portfolio types
export interface Portfolio {
  id: number;
  name: string;
  initialCapital: number;
  currentCapital: number;
  totalPnl: number;
  status: string;
  userId: number;
  createdAt: string;
  updatedAt: string;
}

// Trade types
export interface Trade {
  id: number;
  portfolioId: number;
  strategyId?: number;
  symbol: string;
  side: 'BUY' | 'SELL';
  quantity: number;
  price: number;
  totalAmount: number;
  status: string;
  executedAt?: string;
  createdAt: string;
}

// Position types
export interface Position {
  id: number;
  portfolioId: number;
  symbol: string;
  quantity: number;
  averagePrice: number;
  currentPrice?: number;
  unrealizedPnl?: number;
  createdAt: string;
  updatedAt: string;
}

// Market data types
export interface MarketData {
  id: number;
  symbol: string;
  timestamp: string;
  openPrice?: number;
  highPrice?: number;
  lowPrice?: number;
  closePrice?: number;
  volume?: number;
  createdAt: string;
}

// API Response types
export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface PaginatedResponse<T> {
  data: T[];
  total: number;
  page: number;
  size: number;
  totalPages: number;
}

// Chart types
export interface ChartData {
  labels: string[];
  datasets: {
    label: string;
    data: number[];
    borderColor?: string;
    backgroundColor?: string;
    fill?: boolean;
  }[];
}

// Form types
export interface StrategyFormData {
  name: string;
  description: string;
  strategyType: string;
  parameters: Record<string, any>;
  rules: Record<string, any>;
}

export interface PortfolioFormData {
  name: string;
  initialCapital: number;
}

// WebSocket types
export interface WebSocketMessage {
  type: string;
  data: any;
  timestamp: string;
}

// Dashboard types
export interface DashboardStats {
  totalPortfolios: number;
  totalStrategies: number;
  totalTrades: number;
  totalPnl: number;
}

// Error types
export interface ApiError {
  message: string;
  status: number;
  timestamp: string;
} 