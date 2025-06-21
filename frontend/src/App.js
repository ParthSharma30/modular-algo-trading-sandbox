import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';

const DashboardPage = React.lazy(() => import('./pages/DashboardPage'));

function App() {
  return (
    <Router>
      <React.Suspense fallback={<div>Loading...</div>}>
        <Routes>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="*" element={<DashboardPage />} />
        </Routes>
      </React.Suspense>
    </Router>
  );
}

export default App; 