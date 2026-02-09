import { useState } from "react";
import { useNavigate } from "react-router-dom";

// 🔹 On centralise l'URL de l'API
const API_BASE = "https://pharmasys-1.onrender.com";

export default function Login() {
  const [login, setLogin] = useState("");
  const [pwd, setPwd] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    fetch(`${API_BASE}/api/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ login, pwd }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Erreur réseau");
        return res.json();
      })
      .then((json) => {
        if (!json.role) {
          setError("Login ou mot de passe incorrect");
          return;
        }

        // Sauvegarde de l'utilisateur dans le navigateur
        localStorage.setItem("userId", json.id);
        localStorage.setItem("userRole", json.role);

        // Redirection selon le rôle
        if (json.role === "PHARMACIEN") {
          navigate("/dashboardPharmacien");
        } else {
          navigate("/dashboardGestionnaire");
        }
      })
      .catch((err) => {
        console.error("Erreur de connexion:", err);
        setError("Erreur serveur : impossible de joindre l'API");
      });
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-8 rounded-xl shadow-lg w-96 border border-slate-200"
      >
        <div className="flex justify-center mb-6">
          <div className="bg-indigo-600 p-3 rounded-full text-white">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth={1.5}
              stroke="currentColor"
              className="w-8 h-8"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M16.5 10.5V6.75a4.5 4.5 0 10-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H6.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z"
              />
            </svg>
          </div>
        </div>

        <h2 className="text-2xl font-bold mb-6 text-center text-slate-800">
          PharmaSys - Connexion
        </h2>

        {error && (
          <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm mb-4 border border-red-100">
            {error}
          </div>
        )}

        <div className="mb-4">
          <label className="block text-sm font-medium text-slate-600 mb-1">
            Nom d'utilisateur
          </label>
          <input
            className="w-full p-2.5 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition"
            placeholder="Ex: admin_pharma"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
            required
          />
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-slate-600 mb-1">
            Mot de passe
          </label>
          <input
            type="password"
            className="w-full p-2.5 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition"
            placeholder="••••••••"
            value={pwd}
            onChange={(e) => setPwd(e.target.value)}
            required
          />
        </div>

        <button
          type="submit"
          className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2.5 rounded-lg shadow transition-all active:scale-95"
        >
          Se connecter
        </button>

        <p className="mt-6 text-center text-xs text-slate-400">
          Système de gestion de pharmacie v1.0
        </p>
      </form>
    </div>
  );
}
