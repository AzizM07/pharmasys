import { useEffect, useState } from "react";
import { Users, Search, UserPlus, Mail, Phone, MapPin, ArrowLeft, Star } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function Clients() {
  const navigate = useNavigate();
  const [clients, setClients] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8080/api/clients")
      .then((res) => res.json())
      .then((data) => {
        setClients(data);
        setLoading(false);
      })
      .catch((err) => console.error("Erreur fetch clients:", err));
  }, []);

  // Filtrage dynamique des clients par nom, email ou téléphone
  const filteredClients = clients.filter(
    (c) =>
      c.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
      c.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      c.telephone?.includes(searchTerm)
  );

  return (
    <div className="min-h-screen bg-[#f5f7fb] p-4 md:p-8">
      {/* HEADER & ACTIONS */}
      <div className="max-w-7xl mx-auto flex flex-col md:flex-row justify-between items-center mb-10 gap-4">
        <div>
          <button 
            onClick={() => navigate(-1)} 
            className="flex items-center gap-2 text-slate-500 hover:text-indigo-600 mb-2 transition"
          >
            <ArrowLeft size={18} /> Retour
          </button>
          <h1 className="text-3xl font-black text-slate-800 flex items-center gap-3">
            <Users className="text-indigo-600" size={32} /> Gestion Clients
          </h1>
        </div>

        <div className="flex gap-4 w-full md:w-auto">
          <div className="relative flex-1 md:w-80">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <input
              type="text"
              placeholder="Rechercher un client..."
              className="w-full pl-10 pr-4 py-3 rounded-2xl bg-white border-none shadow-sm focus:ring-2 focus:ring-indigo-500 outline-none transition"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <button className="bg-indigo-600 text-white p-3 rounded-2xl shadow-lg shadow-indigo-200 hover:bg-indigo-700 transition">
            <UserPlus size={24} />
          </button>
        </div>
      </div>

      {/* STATS RAPIDES */}
      <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-100">
          <p className="text-slate-400 text-xs font-bold uppercase tracking-widest">Total Clients</p>
          <h2 className="text-3xl font-black text-slate-800">{clients.length}</h2>
        </div>
        <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-100">
          <p className="text-slate-400 text-xs font-bold uppercase tracking-widest">Nouveaux ce mois</p>
          <h2 className="text-3xl font-black text-indigo-600">+12</h2>
        </div>
        <div className="bg-white p-6 rounded-3xl shadow-sm border border-slate-100">
          <p className="text-slate-400 text-xs font-bold uppercase tracking-widest">Taux de fidélité</p>
          <h2 className="text-3xl font-black text-emerald-500">84%</h2>
        </div>
      </div>

      {/* TABLEAU DES CLIENTS */}
      <div className="max-w-7xl mx-auto bg-white rounded-[32px] shadow-xl shadow-slate-200/50 overflow-hidden border border-slate-100">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50/50 text-slate-400 text-[11px] font-black uppercase tracking-widest">
                <th className="p-6">Profil</th>
                <th className="p-6">Contact</th>
                <th className="p-6">Localisation</th>
                <th className="p-6">Statut</th>
                <th className="p-6 text-center">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-50">
              {loading ? (
                <tr><td colSpan="5" className="p-20 text-center text-slate-400">Chargement des données...</td></tr>
              ) : filteredClients.map((c) => (
                <tr key={c.id} className="hover:bg-indigo-50/30 transition-colors group">
                  <td className="p-6">
                    <div className="flex items-center gap-4">
                      <div className="w-12 h-12 rounded-2xl bg-indigo-100 text-indigo-600 flex items-center justify-center font-bold text-lg shadow-sm">
                        {c.nom.charAt(0)}
                      </div>
                      <div>
                        <p className="font-bold text-slate-700 group-hover:text-indigo-600 transition">{c.nom} {c.prenom}</p>
                        <p className="text-xs text-slate-400 font-mono">ID: #{c.id}</p>
                      </div>
                    </div>
                  </td>
                  <td className="p-6 space-y-1">
                    <div className="flex items-center gap-2 text-sm text-slate-600">
                      <Mail size={14} className="text-slate-400" /> {c.email || "Non renseigné"}
                    </div>
                    <div className="flex items-center gap-2 text-sm text-slate-600">
                      <Phone size={14} className="text-slate-400" /> {c.telephone || "---"}
                    </div>
                  </td>
                  <td className="p-6 text-sm text-slate-500">
                    <div className="flex items-center gap-2">
                      <MapPin size={14} /> {c.adresse || "Tunis, Tunisie"}
                    </div>
                  </td>
                  <td className="p-6">
                    <span className={`px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-tighter flex items-center gap-1 w-fit ${
                      c.id % 3 === 0 ? "bg-emerald-100 text-emerald-600" : "bg-amber-100 text-amber-600"
                    }`}>
                      <Star size={10} fill="currentColor" />
                      {c.id % 3 === 0 ? "Client VIP" : "Régulier"}
                    </span>
                  </td>
                  <td className="p-6 text-center">
                    <button className="px-4 py-2 text-xs font-bold text-indigo-600 border border-indigo-100 rounded-xl hover:bg-indigo-600 hover:text-white transition shadow-sm">
                      Détails
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}