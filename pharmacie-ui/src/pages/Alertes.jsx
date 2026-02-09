import { useEffect, useState } from "react";
import { ArrowLeft, AlertCircle, ShieldAlert } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function Alertes() {
  const [alertes, setAlertes] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const API_BASE = "https://pharmasys-1.onrender.com";

fetch(`${API_BASE}/api/alertes`)

      .then(res => res.json())
      .then(setAlertes)
      .catch(err => console.error("Erreur alertes:", err));
  }, []);

  return (
    <div className="min-h-screen bg-slate-50 p-8">
      <button onClick={() => navigate(-1)} className="flex items-center gap-2 text-slate-500 mb-6 hover:text-indigo-600">
        <ArrowLeft size={20} /> Retour
      </button>

      <div className="max-w-4xl mx-auto">
        <div className="flex items-center gap-4 mb-8">
          <div className="bg-red-100 p-3 rounded-2xl text-red-600">
            <ShieldAlert size={32} />
          </div>
          <div>
            <h1 className="text-3xl font-bold text-slate-800">Alertes de Stock</h1>
            <p className="text-slate-500">Médicaments nécessitant un réapprovisionnement immédiat</p>
          </div>
        </div>

        {alertes.length === 0 ? (
          <div className="bg-white p-10 rounded-3xl text-center shadow-sm border">
            <p className="text-slate-400">Aucune alerte de stock pour le moment. ✨</p>
          </div>
        ) : (
          <div className="grid gap-4">
            {alertes.map(a => (
              <div key={a.id} className="bg-white border-l-8 border-red-500 p-6 rounded-2xl shadow-sm flex justify-between items-center transition-transform hover:scale-[1.01]">
                <div className="flex items-center gap-4">
                  <AlertCircle className="text-red-500" />
                  <div>
                    <h3 className="font-bold text-slate-800 text-lg">{a.nom}</h3>
                    <p className="text-sm text-slate-500">Identifiant produit: #{a.id}</p>
                  </div>
                </div>
                <div className="text-right">
                  <span className="block text-2xl font-black text-red-600">{a.stock}</span>
                  <span className="text-[10px] uppercase font-bold text-slate-400 tracking-tighter">Unités restantes</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}