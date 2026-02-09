import { useEffect, useState } from "react";
import { Package, Plus, Search, AlertCircle, ArrowLeft, Activity, Coins, Database } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function Medicaments() {
  const navigate = useNavigate();
  const [meds, setMeds] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [form, setForm] = useState({ nom: "", dosage: "", stock: "", prix: "" });

  const loadMeds = () => {
    fetch("http://localhost:8080/api/medicaments")
      .then((res) => res.json())
      .then(setMeds)
      .catch((err) => console.error("Erreur:", err));
  };

  useEffect(() => { loadMeds(); }, []);

  const handleAjouter = async (e) => {
    e.preventDefault();
    const res = await fetch("http://localhost:8080/api/medicaments", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nom: form.nom,
        dosage: form.dosage,
        stock: parseInt(form.stock),
        prix: parseFloat(form.prix),
      }),
    });

    if (res.ok) {
      setForm({ nom: "", dosage: "", stock: "", prix: "" });
      loadMeds();
    } else {
      alert("Erreur lors de l'ajout");
    }
  };

  const filteredMeds = meds.filter((m) =>
    m.nom.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-[#f8fafc] p-6 md:p-10">
      <div className="max-w-7xl mx-auto space-y-8">
        
        <button onClick={() => navigate(-1)} className="flex items-center gap-2 text-slate-500 hover:text-indigo-600 font-bold transition">
          <ArrowLeft size={20} /> Retour Dashboard
        </button>

        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
          
          {/* COLONNE GAUCHE : FORMULAIRE */}
          <div className="lg:col-span-4 bg-white p-8 rounded-[2.5rem] shadow-xl shadow-slate-200/50 border border-white h-fit">
            <div className="flex items-center gap-3 mb-8 text-indigo-600">
              <Plus className="bg-indigo-50 p-1.5 rounded-lg" size={32} />
              <h2 className="text-xl font-black text-slate-800 tracking-tight">Nouveau Produit</h2>
            </div>

            <form onSubmit={handleAjouter} className="space-y-5">
              <div className="space-y-2">
                <label className="text-[10px] font-black uppercase text-slate-400 tracking-widest ml-1">Nom du Médicament</label>
                <input 
                  className="w-full p-4 bg-slate-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-indigo-500 transition"
                  placeholder="Ex: Doliprane"
                  value={form.nom}
                  onChange={e => setForm({...form, nom: e.target.value})}
                  required
                />
              </div>

              <div className="space-y-2">
                <label className="text-[10px] font-black uppercase text-slate-400 tracking-widest ml-1">Dosage (mg/ml)</label>
                <div className="relative">
                  <Activity className="absolute left-4 top-4 text-slate-300" size={18}/>
                  <input 
                    className="w-full p-4 pl-12 bg-slate-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-indigo-500 transition"
                    placeholder="Ex: 1000 mg"
                    value={form.dosage}
                    onChange={e => setForm({...form, dosage: e.target.value})}
                    required
                  />
                </div>
              </div>
              
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <label className="text-[10px] font-black uppercase text-slate-400 tracking-widest ml-1">Prix (DT)</label>
                  <div className="relative">
                    <Coins className="absolute left-3 top-4 text-slate-300" size={18}/>
                    <input 
                      type="number" step="0.01"
                      className="w-full p-4 pl-10 bg-slate-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-indigo-500 transition"
                      placeholder="0.00"
                      value={form.prix}
                      onChange={e => setForm({...form, prix: e.target.value})}
                      required
                    />
                  </div>
                </div>
                <div className="space-y-2">
                  <label className="text-[10px] font-black uppercase text-slate-400 tracking-widest ml-1">Stock</label>
                  <div className="relative">
                    <Database className="absolute left-3 top-4 text-slate-300" size={18}/>
                    <input 
                      type="number"
                      className="w-full p-4 pl-10 bg-slate-50 border-none rounded-2xl outline-none focus:ring-2 focus:ring-indigo-500 transition"
                      placeholder="Qté"
                      value={form.stock}
                      onChange={e => setForm({...form, stock: e.target.value})}
                      required
                    />
                  </div>
                </div>
              </div>

              <button className="w-full bg-slate-900 text-white py-5 rounded-[1.5rem] font-bold shadow-2xl hover:bg-indigo-600 transition-all duration-300 active:scale-95 mt-4">
                Ajouter au Stock
              </button>
            </form>
          </div>

          {/* COLONNE DROITE : LISTE */}
          <div className="lg:col-span-8 space-y-6">
            <div className="relative">
              <Search className="absolute left-6 top-1/2 -translate-y-1/2 text-slate-400" size={20} />
              <input 
                className="w-full p-6 pl-16 bg-white rounded-[2rem] shadow-sm border-none outline-none focus:ring-2 focus:ring-indigo-500 transition"
                placeholder="Rechercher une référence..."
                onChange={e => setSearchTerm(e.target.value)}
              />
            </div>

            <div className="bg-white rounded-[2.5rem] shadow-xl shadow-slate-200/50 border border-white overflow-hidden">
              <table className="w-full text-left border-collapse">
                <thead className="bg-slate-50/50 text-slate-400 text-[10px] font-black uppercase tracking-[0.2em]">
                  <tr>
                    <th className="p-6">Désignation</th>
                    <th className="p-6">Dosage</th>
                    <th className="p-6">Prix Unitaire</th>
                    <th className="p-6">Stock</th>
                    <th className="p-6 text-center">Statut</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                  {filteredMeds.map((m) => (
                    <tr key={m.id} className="hover:bg-indigo-50/30 transition-colors group">
                      <td className="p-6">
                        <p className="font-bold text-slate-800 group-hover:text-indigo-600 transition">{m.nom}</p>
                        <p className="text-[10px] text-slate-400 font-mono">REF-{m.id}</p>
                      </td>
                      <td className="p-6 text-slate-600 font-medium">{m.dosage}</td>
                      <td className="p-6 font-black text-slate-900">{m.prix} DT</td>
                      <td className="p-6">
                        <span className={`text-lg font-black ${m.stock <= 10 ? 'text-red-500' : 'text-slate-800'}`}>
                          {m.stock}
                        </span>
                      </td>
                      <td className="p-6">
                        <span className={`flex items-center justify-center gap-1.5 px-3 py-1.5 rounded-full text-[10px] font-black uppercase tracking-tighter ${
                          m.stock <= 10 ? 'bg-red-100 text-red-600' : 'bg-emerald-100 text-emerald-600'
                        }`}>
                          {m.stock <= 10 ? <AlertCircle size={12}/> : null}
                          {m.stock <= 10 ? "Critique" : "Disponible"}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {filteredMeds.length === 0 && (
                <div className="p-20 text-center text-slate-400 font-medium">
                  Aucun médicament trouvé...
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}