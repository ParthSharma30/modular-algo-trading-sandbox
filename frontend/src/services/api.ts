import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { 
  User, 
  LoginRequest, 
  LoginResponse, 
  Strategy, 
  Portfolio, 
  Trade, 
  MarketData,
  ApiResponse 
} from '../types';

// @ts-ignore: process.env is injected at build time by Create React App or Docker
declare const process: any;
const api = axios.create({
  baseURL: (typeof process !== 'undefined' && process.env.REACT_APP_API_URL) ? process.env.REACT_APP_API_URL : 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

class ApiService {
  private api: AxiosInstance;

  constructor() {
    this.api = api;
  }

  // Authentication
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response: AxiosResponse<LoginResponse> = await this.api.post('/auth/login', credentials);
    return response.data;
  }

  async register(userData: Partial<User>, password: string): Promise<User> {
    const response: AxiosResponse<User> = await this.api.post('/auth/register', userData, {
      params: { password }
    });
    return response.data;
  }

  async refreshToken(refreshToken: string): Promise<{ token: string }> {
    const response: AxiosResponse<{ token: string }> = await this.api.post('/auth/refresh', null, {
      params: { refreshToken }
    });
    return response.data;
  }

  // Strategies
  async getStrategies(): Promise<Strategy[]> {
    const response: AxiosResponse<Strategy[]> = await this.api.get('/strategies');
    return response.data;
  }

  async getStrategy(id: number): Promise<Strategy> {
    const response: AxiosResponse<Strategy> = await this.api.get(`/strategies/${id}`);
    return response.data;
  }

  async createStrategy(strategy: Partial<Strategy>): Promise<Strategy> {
    const response: AxiosResponse<Strategy> = await this.api.post('/strategies', strategy);
    return response.data;
  }

  async updateStrategy(id: number, strategy: Partial<Strategy>): Promise<Strategy> {
    const response: AxiosResponse<Strategy> = await this.api.put(`/strategies/${id}`, strategy);
    return response.data;
  }

  async deleteStrategy(id: number): Promise<void> {
    await this.api.delete(`/strategies/${id}`);
  }

  async executeStrategy(id: number, portfolioId: number): Promise<void> {
    await this.api.post(`/strategies/${id}/execute`, null, {
      params: { portfolioId }
    });
  }

  async backtestStrategy(id: number, startDate: string, endDate: string): Promise<void> {
    await this.api.post(`/strategies/${id}/backtest`, null, {
      params: { startDate, endDate }
    });
  }

  // Portfolios
  async getPortfolios(): Promise<Portfolio[]> {
    const response: AxiosResponse<Portfolio[]> = await this.api.get('/portfolios');
    return response.data;
  }

  async getPortfolio(id: number): Promise<Portfolio> {
    const response: AxiosResponse<Portfolio> = await this.api.get(`/portfolios/${id}`);
    return response.data;
  }

  async createPortfolio(portfolio: Partial<Portfolio>): Promise<Portfolio> {
    const response: AxiosResponse<Portfolio> = await this.api.post('/portfolios', portfolio);
    return response.data;
  }

  async updatePortfolio(id: number, portfolio: Partial<Portfolio>): Promise<Portfolio> {
    const response: AxiosResponse<Portfolio> = await this.api.put(`/portfolios/${id}`, portfolio);
    return response.data;
  }

  async deletePortfolio(id: number): Promise<void> {
    await this.api.delete(`/portfolios/${id}`);
  }

  async getPortfolioValue(id: number): Promise<number> {
    const response: AxiosResponse<number> = await this.api.get(`/portfolios/${id}/value`);
    return response.data;
  }

  // Market Data
  async getMarketData(): Promise<MarketData[]> {
    const response: AxiosResponse<MarketData[]> = await this.api.get('/market-data');
    return response.data;
  }

  async getMarketDataBySymbol(symbol: string): Promise<MarketData[]> {
    const response: AxiosResponse<MarketData[]> = await this.api.get(`/market-data/${symbol}`);
    return response.data;
  }

  async getLatestMarketData(symbol: string): Promise<MarketData> {
    const response: AxiosResponse<MarketData> = await this.api.get(`/market-data/${symbol}/latest`);
    return response.data;
  }

  async getHistoricalData(symbol: string, startDate: string, endDate: string): Promise<MarketData[]> {
    const response: AxiosResponse<MarketData[]> = await this.api.get(`/market-data/${symbol}/history`, {
      params: { startDate, endDate }
    });
    return response.data;
  }

  async startSimulation(symbol: string, speedMultiplier: number = 1.0): Promise<void> {
    await this.api.post(`/market-data/${symbol}/simulate`, null, {
      params: { speedMultiplier }
    });
  }

  async generateSimulatedData(symbol: string): Promise<MarketData> {
    const response: AxiosResponse<MarketData> = await this.api.post(`/market-data/${symbol}/generate`);
    return response.data;
  }
}

export const apiService = new ApiService();
export default apiService; 