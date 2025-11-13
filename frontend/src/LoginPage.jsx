import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext.jsx';

export default function LoginPage() {
  const { login } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [err, setErr] = useState(null);
  const nav = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setErr(null);
    try {
      await login(username, password);
      nav('/');
    } catch (e) {
      setErr(e.message);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50">
      <div className="max-w-md mx-auto mt-6 p-6 bg-white shadow-md rounded-2xl">
        <h2 className="text-2xl font-semibold mb-4">Sign in to TradeShift</h2>
        <form onSubmit={submit} className="space-y-4">
          <input value={username} onChange={e=>setUsername(e.target.value)} placeholder="Username" className="w-full p-2 border rounded" />
          <input value={password} onChange={e=>setPassword(e.target.value)} type="password" placeholder="Password" className="w-full p-2 border rounded" />
          {err && <div className="text-red-600">{err}</div>}
          <button className="w-full py-2 rounded bg-indigo-600 text-white">Sign in</button>
        </form>
      </div>
    </div>
  );
}
