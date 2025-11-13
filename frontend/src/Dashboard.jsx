import React, { useEffect, useState, useCallback } from 'react';
import Header from '../components/Header.jsx';
import PriceTicker from '../components/PriceTicker.jsx';
import PortfolioList from '../components/PortfolioList.jsx';
import TransactionsChart from '../components/TransactionsChart.jsx';
import MarketResearch from '../components/MarketResearch.jsx';
import TradeSimulator from '../components/TradeSimulator.jsx';
import useWebSocket from '../hooks/useWebSocket.js';
import { apiFetch } from '../api/apiClient.js';
import { useAuth } from '../auth/AuthContext.jsx';

export default function Dashboard() {
  const { token, logout } = useAuth();
  const [portfolio, setPortfolio] = useState([]);
  const [history, setHistory] = useState([]);
  const [prices, setPrices] = useState({});
  const [selectedSymbol, setSelectedSymbol] = useState(null);
  const [marketDetail, setMarketDetail] = useState(null);

  useEffect(() => {
    if (!token) return;
    (async () => {
      try {
        const [p, h] = await Promise.all([
          apiFetch(token, '/api/portfolio'),
          apiFetch(token, '/api/transactions/history')
        ]);
        setPortfolio(p || []);
        setHistory(h || []);
      } catch (e) {
        console.error('load', e);
      }
    })();
  }, [token]);

  const handleWs = useCallback((msg) => {
    if (msg.type === 'price') {
      setPrices(prev => ({ ...prev, [msg.symbol]: { price: msg.price, change: msg.change, ts: msg.ts } }));
      setPortfolio(prev => prev.map(p => p.symbol === msg.symbol ? { ...p, currentPrice: msg.price } : p));
    }
  }, []);

  useWebSocket(token, handleWs);

  const onSelectSymbol = async (symbol) => {
    setSelectedSymbol(symbol);
    try {
      const detail = await apiFetch(token, `/api/market/${encodeURIComponent(symbol)}`);
      setMarketDetail(detail);
    } catch (e) {
      setMarketDetail(null);
    }
  };

  const onTradeExecuted = async (trade) => {
    try {
      const p = await apiFetch(token, '/api/portfolio');
      const h = await apiFetch(token, '/api/transactions/history');
      setPortfolio(p || []);
      setHistory(h || []);
    } catch (e) {
      console.error('refresh after trade', e);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <Header />
      <main className="p-6 grid grid-cols-12 gap-6">
        <section className="col-span-8 space-y-4">
          <PriceTicker prices={prices} />
          <div className="grid grid-cols-2 gap-4">
            <PortfolioList portfolio={portfolio} />
            <TransactionsChart history={history} />
          </div>
          <div className="grid grid-cols-3 gap-4">
            <div className="col-span-2">
              <MarketResearch onSelectSymbol={onSelectSymbol} />
            </div>
            <div>
              <TradeSimulator onTradeExecuted={onTradeExecuted} />
            </div>
          </div>
        </section>
        <aside className="col-span-4 space-y-4">
          <div className="bg-white p-4 rounded shadow">
            <h3 className="font-semibold mb-3">Selected symbol</h3>
            {selectedSymbol ? (
              <div>
                <div className="text-xl font-bold">{selectedSymbol}</div>
                <div className="text-sm text-gray-500">{marketDetail?.name}</div>
                <div className="mt-3 text-sm">
                  <div>Market cap: {marketDetail?.marketCap ?? '—'}</div>
                  <div>PE ratio: {marketDetail?.peRatio ?? '—'}</div>
                  <div>52w range: {marketDetail?.range52w ?? '—'}</div>
                </div>
                <div className="mt-3">
                  <button onClick={() => navigator.clipboard.writeText(selectedSymbol)} className="px-3 py-2 rounded bg-slate-100">Copy symbol</button>
                </div>
              </div>
            ) : (
              <div className="text-sm text-gray-500">No symbol selected. Use Market research to pick one.</div>
            )}
          </div>

          <div className="bg-white p-4 rounded shadow">
            <h3 className="font-semibold mb-3">Quick stats</h3>
            <div className="text-sm text-gray-500">Total positions: {portfolio.length}</div>
            <div className="text-sm text-gray-500">Last update: {new Date().toLocaleString()}</div>
          </div>
        </aside>
      </main>
    </div>
  );
}
