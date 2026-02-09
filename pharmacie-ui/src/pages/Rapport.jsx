import { useEffect, useState } from "react";
import { ArrowLeft, TrendingUp, Package, DollarSign, FileText } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function Rapport() {
  const navigate = useNavigate();
  const [data, setData] = useState({
    totalVentes: 0,
    totalQuantite: 0,
    chiffreAffaires: 0,
    ventesParMedicament: []
  });

  useEffect(() => {
    fetch("http://localhost:8080/api/rapport")
      .then(res => res.json())
      .then(setData)
      .catch(err => console.error("Erreur rapport:", err));
  }, []);

  return (
    <div className="min-h-screen bg-slate-50 p-8">
      <button onClick={() => navigate(-1)} className="flex items-center gap-2 text-slate-500 mb-6 hover:text-indigo-600">
        <ArrowLeft size={20} /> Retour
      </button>

      <div className="max-w-6xl mx-auto">
        <h1 className="text-3xl font-bold text-slate-800 mb-8 flex items-center gap-3">
          <FileText className="text-indigo-600" /> Rapports d'Activité
        </h1>

        {/* KPI CARDS */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
          <Card title="Ventes Totales" value={data.totalVentes} icon={<TrendingUp className="text-blue-500" />} color="bg-blue-50" />
          <Card title="Unités Vendues" value={data.totalQuantite} icon={<Package className="text-purple-500" />} color="bg-purple-50" />
          <Card title="Chiffre d'Affaires" value={`${data.chiffreAffaires} DT`} icon={<DollarSign className="text-green-500" />} color="bg-green-50" />
        </div>

        {/* TABLEAU */}
        <div className="bg-white rounded-3xl shadow-sm border border-slate-100 overflow-hidden">
          <div className="p-6 border-b bg-slate-50/50">
            <h2 className="font-bold text-slate-700">Détails par Médicament</h2>
          </div>
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="text-slate-400 text-[11px] uppercase tracking-widest bg-slate-50">
                <th className="p-5">Médicament</th>
                <th className="p-5 text-center">Quantité cumulée</th>
                <th className="p-5 text-right">Revenu total</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {data.ventesParMedicament.map((v, i) => (
                <tr key={i} className="hover:bg-slate-50/50 transition">
                  <td className="p-5 font-bold text-slate-700">{v.nom}</td>
                  <td className="p-5 text-center font-semibold text-indigo-600">{v.total_qte}</td>
                  <td className="p-5 text-right font-black text-slate-800">{v.total_prix} DT</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

function Card({ title, value, icon, color }) {
  return (
    <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-100 flex items-center gap-5">
      <div className={`${color} p-4 rounded-2xl`}>{icon}</div>
      <div>
        <p className="text-sm font-medium text-slate-400 uppercase tracking-wide">{title}</p>
        <h2 className="text-2xl font-black text-slate-800">{value}</h2>
      </div>
    </div>
  );
}