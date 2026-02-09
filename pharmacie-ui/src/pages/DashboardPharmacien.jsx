import { useEffect, useState } from "react"
import { NavLink, useNavigate } from "react-router-dom" // ✅ Import NavLink
import {
  LayoutDashboard,
  ShoppingCart,
  Package,
  AlertTriangle,
  LogOut,
  Activity
} from "lucide-react"
import { AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts"

export default function DashboardPharmacien() {
  const navigate = useNavigate();
  const [data, setData] = useState({
    ventes: 0,
    revenus: "0 DT",
    stock: 0,
    alertes: 0,
    evolutionVentes: []
  })

  useEffect(() => {
    fetch("http://localhost:8080/api/dashboard")
      .then(res => res.json())
      .then(json => {
        setData({
          ventes: json.ventes,
          revenus: json.revenus,
          stock: json.stock,
          alertes: json.alertes,
          evolutionVentes: json.evolutionVentes || []
        })
      })
      .catch(err => console.error("Erreur API dashboard:", err))
  }, [])

  const handleLogout = () => {
    localStorage.removeItem("userId");
    localStorage.removeItem("userRole");
    navigate("/");
  };

  return (
    <div className="flex min-h-screen bg-[#f5f7fb]">
      {/* SIDEBAR */}
      <aside className="w-64 bg-[#0f172a] text-white p-6 flex flex-col">
        <h2 className="text-2xl font-bold mb-10 text-indigo-400">
          Pharma<span className="text-white">Sys</span>
        </h2>
        
        <nav className="space-y-2">
          {/* ✅ Utilisation de NavLink pour la navigation */}
          <Nav to="/dashboardPharmacien" icon={<LayoutDashboard size={20}/>} label="Dashboard" />
          <Nav to="/ventes" icon={<ShoppingCart size={20}/>} label="Ventes" />
          <Nav to="/stock" icon={<Package size={20}/>} label="Stock" />
        </nav>

        <div className="mt-auto">
          {/* ✅ Bouton de déconnexion fonctionnel */}
          <button 
            onClick={handleLogout}
            className="flex items-center gap-3 p-2 w-full rounded-lg hover:bg-red-500/20 text-red-400 transition-colors"
          >
            <LogOut size={20}/><span>Déconnexion</span>
          </button>
        </div>
      </aside>

      {/* CONTENT */}
      <main className="flex-1 p-8">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-800">Dashboard Pharmacien</h1>
          <div className="flex items-center gap-2 bg-white px-4 py-2 rounded-lg shadow-sm">
             <span className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></span>
             <span className="text-gray-500 text-sm font-medium">Live System Status</span>
          </div>
        </div>

        {/* KPI CARDS */}
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6 mb-10">
          <Kpi title="Ventes" value={data.ventes} subtitle="Nombre total" color="from-indigo-500 to-purple-500" />
          <Kpi title="Revenus" value={data.revenus} subtitle="Ce mois" color="from-green-400 to-emerald-500" />
          <Kpi title="Stock" value={data.stock} subtitle="Produits" color="from-blue-400 to-cyan-500" />
          <Kpi title="Alertes" value={data.alertes} subtitle="Stock critique" color="from-red-400 to-pink-500" />
        </div>

        {/* CHART + ACTIVITY */}
        <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
          <div className="bg-white rounded-2xl shadow-lg p-6 xl:col-span-2">
            <h3 className="font-semibold text-gray-700 mb-4 font-bold uppercase text-xs tracking-wider">Évolution des ventes</h3>
            <div className="h-[280px]">
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={data.evolutionVentes}>
                  <defs>
                    <linearGradient id="colorVentes" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#6366f1" stopOpacity={0.8}/>
                      <stop offset="95%" stopColor="#6366f1" stopOpacity={0}/>
                    </linearGradient>
                  </defs>
                  <XAxis dataKey="name" stroke="#94a3b8" fontSize={12} />
                  <YAxis stroke="#94a3b8" fontSize={12} />
                  <Tooltip />
                  <Area type="monotone" dataKey="value" stroke="#6366f1" strokeWidth={3} fill="url(#colorVentes)" />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          </div>

          <div className="bg-white rounded-2xl shadow-lg p-6">
            <h3 className="font-semibold text-gray-700 mb-6 font-bold uppercase text-xs tracking-wider">Activité récente</h3>
            <div className="space-y-4">
              <ActivityItem text="Vente enregistrée – Paracétamol" time="12:45" />
              <ActivityItem text="Stock critique – Insuline" time="10:20" />
              <ActivityItem text="Commande validée" time="Hier" />
              <ActivityItem text="Vente annulée" time="Hier" />
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}

// ✅ Composant Nav mis à jour avec NavLink
function Nav({ icon, label, to }) {
  return (
    <NavLink 
      to={to} 
      className={({ isActive }) => 
        `flex items-center gap-3 p-3 rounded-lg transition-all duration-200 ${
          isActive 
            ? "bg-indigo-600 text-white shadow-lg shadow-indigo-500/30" 
            : "text-slate-400 hover:bg-slate-800 hover:text-white"
        }`
      }
    >
      {icon}
      <span className="font-medium">{label}</span>
    </NavLink>
  )
}

function Kpi({ title, value, subtitle, color }) {
  return (
    <div className="bg-white rounded-2xl shadow-lg p-6 relative overflow-hidden group hover:scale-[1.02] transition-transform">
      <div className={`absolute top-0 right-0 w-32 h-32 bg-gradient-to-br ${color} opacity-10 rounded-full -mr-10 -mt-10 group-hover:opacity-20 transition-opacity`} />
      <p className="text-gray-500 text-sm font-medium">{title}</p>
      <h2 className="text-3xl font-bold text-gray-800 my-1">{value}</h2>
      <span className="text-xs text-slate-400 bg-slate-100 px-2 py-0.5 rounded-full">{subtitle}</span>
    </div>
  )
}

function ActivityItem({ text, time }) {
  return (
    <div className="flex items-start gap-3 text-sm text-gray-600 group">
      <div className="mt-1">
        <Activity size={14} className="text-indigo-500" />
      </div>
      <div>
        <p className="font-medium text-gray-800">{text}</p>
        <p className="text-[10px] text-gray-400">{time}</p>
      </div>
    </div>
  )
}