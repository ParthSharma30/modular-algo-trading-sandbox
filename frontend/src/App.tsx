import React from 'react';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Cloud Algo Trading Sandbox</h1>
        <p>Welcome to the Algorithmic Trading Platform</p>
        <p>Backend API is running at: <a href="http://localhost:8080/api" target="_blank" rel="noopener noreferrer">http://localhost:8080/api</a></p>
        <p>Swagger UI: <a href="http://localhost:8080/swagger-ui.html" target="_blank" rel="noopener noreferrer">http://localhost:8080/swagger-ui.html</a></p>
      </header>
    </div>
  );
}

export default App; 