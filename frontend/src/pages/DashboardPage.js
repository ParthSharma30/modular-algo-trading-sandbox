import React, { useEffect, useState } from 'react';
import api from '../services/api.ts';
import '../App.css';

// Portfolio List Component
function PortfolioList({ portfolios, positions, trades }) {
  return (
    <div className="card">
      <div className="card-title">Your Portfolios</div>
      {portfolios.length === 0 && <p>No portfolios found.</p>}
      {portfolios.map((p) => (
        <div key={p.id} style={{ marginBottom: 24 }}>
          <h4>{p.name} - Capital: {p.currentCapital}</h4>
          <div>
            <strong>Positions:</strong>
            <ul className="styled-list">
              {(positions[p.id] && positions[p.id].length > 0) ? positions[p.id].map(pos => (
                <li key={pos.id}>{pos.symbol}: {pos.quantity} @ {pos.averagePrice} (Unrealized PnL: {pos.unrealizedPnl})</li>
              )) : <li>No positions.</li>}
            </ul>
          </div>
          <div>
            <strong>Trades:</strong>
            <ul className="styled-list">
              {(trades[p.id] && trades[p.id].length > 0) ? trades[p.id].map(trade => (
                <li key={trade.id}>{trade.side} {trade.quantity} {trade.symbol} @ {trade.price} ({trade.status})</li>
              )) : <li>No trades.</li>}
            </ul>
          </div>
        </div>
      ))}
    </div>
  );
}

// Strategy List Component
function StrategyList({ strategies }) {
  return (
    <div className="card">
      <div className="card-title">Your Strategies</div>
      <ul className="styled-list">
        {strategies.length === 0 && <li>No strategies found.</li>}
        {strategies.map((s) => (
          <li key={s.id}>{s.name} ({s.strategyType}) - {s.description}</li>
        ))}
      </ul>
    </div>
  );
}

// Create Portfolio Form
function CreatePortfolioForm({ onCreate }) {
  const [name, setName] = useState('');
  const [initialCapital, setInitialCapital] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const newPortfolio = await api.createPortfolio({ name, initialCapital: parseFloat(initialCapital), currentCapital: parseFloat(initialCapital) });
      onCreate(newPortfolio);
      setName('');
      setInitialCapital('');
    } catch (err) {
      setError('Failed to create portfolio.');
    }
    setLoading(false);
  };

  return (
    <form className="form-section" onSubmit={handleSubmit}>
      <h4>Create Portfolio</h4>
      <label>Name</label>
      <input value={name} onChange={e => setName(e.target.value)} required />
      <label>Initial Capital</label>
      <input type="number" value={initialCapital} onChange={e => setInitialCapital(e.target.value)} required min="0" step="0.01" />
      <button type="submit" disabled={loading}>{loading ? 'Creating...' : 'Create Portfolio'}</button>
      {error && <div style={{ color: 'red', marginTop: 8 }}>{error}</div>}
    </form>
  );
}

// Create Strategy Form
function CreateStrategyForm({ onCreate }) {
  const [name, setName] = useState('');
  const [strategyType, setStrategyType] = useState('MOMENTUM');
  const [description, setDescription] = useState('');
  const [parameters, setParameters] = useState('{}');
  const [rules, setRules] = useState('{}');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    let parsedParameters, parsedRules;
    try {
      parsedParameters = parameters ? JSON.parse(parameters) : {};
    } catch (err) {
      setError('Parameters must be valid JSON.');
      setLoading(false);
      return;
    }
    try {
      parsedRules = rules ? JSON.parse(rules) : {};
    } catch (err) {
      setError('Rules must be valid JSON.');
      setLoading(false);
      return;
    }
    try {
      const newStrategy = await api.createStrategy({ name, strategyType, description, parameters: parsedParameters, rules: parsedRules });
      onCreate(newStrategy);
      setName('');
      setStrategyType('MOMENTUM');
      setDescription('');
      setParameters('{}');
      setRules('{}');
    } catch (err) {
      setError('Failed to create strategy.');
    }
    setLoading(false);
  };

  return (
    <form className="form-section" onSubmit={handleSubmit}>
      <h4>Create Strategy</h4>
      <label>Name</label>
      <input value={name} onChange={e => setName(e.target.value)} required />
      <label>Type</label>
      <select value={strategyType} onChange={e => setStrategyType(e.target.value)}>
        <option value="MOMENTUM">Momentum</option>
        <option value="MEAN_REVERSION">Mean Reversion</option>
        <option value="RSI">RSI</option>
      </select>
      <label>Description</label>
      <textarea value={description} onChange={e => setDescription(e.target.value)} />
      <label>Parameters (JSON)</label>
      <textarea value={parameters} onChange={e => setParameters(e.target.value)} placeholder="{}" />
      <label>Rules (JSON)</label>
      <textarea value={rules} onChange={e => setRules(e.target.value)} placeholder="{}" />
      <button type="submit" disabled={loading}>{loading ? 'Creating...' : 'Create Strategy'}</button>
      {error && <div style={{ color: 'red', marginTop: 8 }}>{error}</div>}
    </form>
  );
}

const DashboardPage = () => {
  const [user] = useState({ username: 'Guest', firstName: 'Guest' });
  const [portfolios, setPortfolios] = useState([]);
  const [strategies, setStrategies] = useState([]);
  const [positions, setPositions] = useState({});
  const [trades, setTrades] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const portRes = await api.getPortfolios();
        setPortfolios(portRes);
        const stratRes = await api.getStrategies();
        setStrategies(stratRes);
        // Fetch positions and trades for each portfolio (optional, can be left empty)
        const posObj = {};
        const tradeObj = {};
        for (const p of portRes) {
          posObj[p.id] = [];
          tradeObj[p.id] = [];
        }
        setPositions(posObj);
        setTrades(tradeObj);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch dashboard data.');
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handlePortfolioCreate = (newPortfolio) => {
    setPortfolios(prev => [...prev, newPortfolio]);
  };
  const handleStrategyCreate = (newStrategy) => {
    setStrategies(prev => [...prev, newStrategy]);
  };

  if (loading) return <div>Loading dashboard...</div>;
  if (error) return <div>{error}</div>;
  if (!user) return <div>Loading user...</div>;

  return (
    <div className="dashboard-container">
      <div className="dashboard-title">Welcome, {user.firstName || user.username}!</div>
      <div className="dashboard-grid">
        <div>
          <PortfolioList portfolios={portfolios} positions={positions} trades={trades} />
          <CreatePortfolioForm onCreate={handlePortfolioCreate} />
        </div>
        <div>
          <StrategyList strategies={strategies} />
          <CreateStrategyForm onCreate={handleStrategyCreate} />
        </div>
      </div>
    </div>
  );
};

export default DashboardPage; 