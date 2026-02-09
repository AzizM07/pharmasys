import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  LayoutDashboard, Users, Package, AlertTriangle, 
  BarChart3, LogOut, ShoppingCart, Activity
} from "lucide-react";
import {
  AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer
} from "recharts";

export default function DashboardGestionnaire() {
  const navigate = useNavigate();
  const [data, setData] = useState({
    ventes: 0, revenus: "0 DT", stock: 0, alertes: 0,
    totalClients: 0, totalMedicaments: 0, evolutionVentes: []
  });

  useEffect(() => {
    fetch("http://localhost:8080/api/dashboard")
      .then(res => res.json())
      .then(setData)
      .catch(err => console.error("Erreur Dashboard:", err));
  }, []);

  return (
    <div className="flex min-h-screen bg-[#f5f7fb]">
      {/* SIDEBAR */}
      <aside className="w-64 bg-[#0f172a] text-slate-400 p-6 flex flex-col fixed h-full">
        <h2 className="text-2xl font-bold mb-10 text-white flex items-center gap-2">
          <div className="bg-indigo-600 p-1.5 rounded-lg"><Activity size={20}/></div>
          PharmaSys
        </h2>

        <div className="space-y-2 flex-1">
          <NavItem icon={<LayoutDashboard size={20}/>} label="Tableau de bord" active to="/dashboardGestionnaire" />
          <NavItem icon={<Users size={20}/>} label="Clients" to="/clients" />
          <NavItem icon={<Package size={20}/>} label="Médicaments" to="/medicaments" />
          <NavItem icon={<ShoppingCart size={20}/>} label="Ventes" to="/ventes" />
          <NavItem icon={<AlertTriangle size={20}/>} label="Alertes" to="/alertes" />
          <NavItem icon={<BarChart3 size={20}/>} label="Rapport" to="/rapport" />
        </div>

        <button onClick={() => navigate("/")} className="mt-auto flex items-center gap-3 p-3 rounded-xl hover:bg-red-500/10 hover:text-red-400 transition">
          <LogOut size={20}/> Déconnexion
        </button>
      </aside>

      {/* CONTENT */}
      <main className="flex-1 ml-64 p-10">
        <header className="mb-10">
          <h1 className="text-3xl font-black text-slate-800">Espace Gestionnaire</h1>
          <p className="text-slate-500">Bienvenue. Voici l'état actuel de votre pharmacie.</p>
        </header>

        {/* KPI GRID */}
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6 mb-10">
          <Kpi title="Revenus Mensuels" value={data.revenus} color="text-green-600" />
          <Kpi title="Total Ventes" value={data.ventes} color="text-indigo-600" />
          <div onClick={() => navigate("/alertes")} className="cursor-pointer">
             <Kpi title="Alertes Stock" value={data.alertes} color="text-red-500" sub="Cliquer pour voir" />
          </div>
          <Kpi title="Clients Actifs" value={data.totalClients} color="text-blue-600" />
          <Kpi title="Catalogue" value={`${data.totalMedicaments} produits`} color="text-slate-700" />
          <Kpi title="Stock Global" value={data.stock} color="text-orange-500" />
        </div>

        {/* GRAPH */}
        <div className="bg-white rounded-3xl shadow-sm border border-slate-100 p-8">
          <h3 className="font-bold text-slate-700 mb-6">Évolution des revenus (7 derniers jours)</h3>
          <div className="h-[350px]">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={data.evolutionVentes}>
                <defs>
                  <linearGradient id="colorVal" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#6366f1" stopOpacity={0.1}/>
                    <stop offset="95%" stopColor="#6366f1" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <XAxis dataKey="name" axisLine={false} tickLine={false} tick={{fill: '#94a3b8', fontSize: 12}} dy={10} />
                <YAxis hide />
                <Tooltip contentStyle={{borderRadius: '15px', border: 'none', boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1)'}} />
                <Area type="monotone" dataKey="value" stroke="#6366f1" fill="url(#colorVal)" strokeWidth={4} />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>
      </main>
    </div>
  );
}

function NavItem({ icon, label, to, active }) {
  const navigate = useNavigate();
  return (
    <div
      onClick={() => navigate(to)}
      className={`flex items-center gap-3 p-3.5 rounded-xl cursor-pointer transition-all ${
        active ? "bg-indigo-600 text-white shadow-lg shadow-indigo-200" : "hover:bg-slate-800 hover:text-white"
      }`}
    >
      {icon} <span className="font-medium text-sm">{label}</span>
    </div>
  );
}

function Kpi({ title, value, color, sub }) {
  return (
    <div className="bg-white rounded-3xl p-6 shadow-sm border border-slate-100 hover:shadow-md transition">
      <p className="text-slate-400 text-[11px] font-black uppercase tracking-widest mb-1">{title}</p>
      <h2 className={`text-3xl font-black ${color}`}>{value}</h2>
      {sub && <p className="text-[10px] text-slate-400 mt-2 font-bold uppercase">{sub}</p>}
    </div>
  );
}