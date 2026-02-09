import { useEffect, useState } from "react";
import { UserPlus, Users, ShoppingBag, ArrowLeft, Package, Trash2, History, Search } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function Ventes() {
  const navigate = useNavigate();
  
  // États pour les données
  const [medicaments, setMedicaments] = useState([]);
  const [clients, setClients] = useState([]);
  const [historique, setHistorique] = useState([]);
  
  // États pour l'interface
  const [nouveauClient, setNouveauClient] = useState(false);
  const [searchTerm, setSearchTerm] = useState(""); // Pour la barre de recherche
  const [form, setForm] = useState({ 
    idClient: "", 
    nom: "", 
    prenom: "", 
    email: "", 
    adresse: "", 
    idMedicament: "", 
    quantite: 1 
  });

  // Chargement initial des données
  const loadData = async () => {
    try {
      const [resMed, resCli, resVen] = await Promise.all([
        fetch("http://localhost:8080/api/medicaments"),
        fetch("http://localhost:8080/api/clients"),
        fetch("http://localhost:8080/api/vente")
      ]);
      setMedicaments(await resMed.json());
      setClients(await resCli.json());
      setHistorique(await resVen.json());
    } catch (err) {
      console.error("Erreur de chargement des données:", err);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  // Enregistrement d'une vente
  const handleSubmit = async (e) => {
    e.preventDefault();
    const pharmacienId = localStorage.getItem("userId");
    if (!pharmacienId) return alert("Session expirée. Reconnectez-vous.");

    const payload = {
      idPharmacien: parseInt(pharmacienId),
      idMedicament: parseInt(form.idMedicament),
      quantite: parseInt(form.quantite),
      idClient: !nouveauClient ? parseInt(form.idClient) : null,
      clientInfo: nouveauClient ? { 
        nom: form.nom, 
        prenom: form.prenom, 
        email: form.email, 
        adresse: form.adresse 
      } : null
    };

    try {
      const res = await fetch("http://localhost:8080/api/vente", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      
      if (data.success) {
        alert("Vente enregistrée avec succès !");
        // Reset du formulaire partiel
        setForm({ ...form, idMedicament: "", quantite: 1 });
        loadData(); // Rafraîchir l'historique et les stocks
      } else {
        alert(data.message || "Erreur lors de la vente");
      }
    } catch (err) {
      alert("Erreur serveur.");
    }
  };

  // Annulation d'une vente (Suppression + Retour de stock)
  const handleAnnuler = async (id) => {
    if (!confirm("⚠️ Voulez-vous annuler cette vente ? Le stock sera automatiquement restauré.")) return;
    
    try {
      const res = await fetch(`http://localhost:8080/api/vente?id=${id}`, { 
        method: "DELETE" 
      });
      const data = await res.json();
      
      if (data.success) {
        loadData(); // Recharger pour voir le stock augmenter
      } else {
        alert("Erreur lors de l'annulation.");
      }
    } catch (err) {
      alert("Impossible de contacter le serveur.");
    }
  };

  // Logique de recherche filtrée
  const historiqueFiltre = historique.filter(v => 
    v.client.toLowerCase().includes(searchTerm.toLowerCase()) || 
    v.medicament.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-slate-50 p-4 md:p-8 space-y-10">
      {/* Header & Retour */}
      <div className="max-w-5xl mx-auto flex items-center justify-between">
        <button 
          onClick={() => navigate(-1)} 
          className="flex items-center gap-2 text-slate-500 hover:text-indigo-600 font-medium transition"
        >
          <ArrowLeft size={20} /> Retour au tableau de bord
        </button>
      </div>

      {/* SECTION FORMULAIRE */}
      <div className="max-w-3xl mx-auto bg-white rounded-3xl shadow-xl overflow-hidden border border-slate-100">
        <div className="bg-indigo-600 p-5 text-white flex items-center gap-4">
          <div className="bg-white/20 p-2 rounded-lg">
            <ShoppingBag size={24} />
          </div>
          <h1 className="text-xl font-bold">Nouvelle Vente</h1>
        </div>

        <form onSubmit={handleSubmit} className="p-8 space-y-6">
          {/* Bloc Client */}
          <div className="space-y-4">
            <div className="flex justify-between items-center border-b border-slate-100 pb-2">
              <h2 className="text-sm font-black text-slate-400 uppercase tracking-wider">Information Client</h2>
              <button 
                type="button" 
                onClick={() => setNouveauClient(!nouveauClient)} 
                className="text-xs font-bold text-indigo-600 hover:underline transition"
              >
                {nouveauClient ? "SÉLECTIONNER CLIENT EXISTANT" : "+ CRÉER NOUVEAU CLIENT"}
              </button>
            </div>

            {nouveauClient ? (
              <div className="grid grid-cols-2 gap-4 animate-in fade-in duration-300">
                <input 
                  placeholder="Nom" 
                  className="p-3 border rounded-xl outline-indigo-500 bg-slate-50" 
                  onChange={e => setForm({...form, nom: e.target.value})} 
                  required 
                />
                <input 
                  placeholder="Prénom" 
                  className="p-3 border rounded-xl outline-indigo-500 bg-slate-50" 
                  onChange={e => setForm({...form, prenom: e.target.value})} 
                  required 
                />
              </div>
            ) : (
              <select 
                className="w-full p-3 border rounded-xl bg-slate-50 outline-indigo-500" 
                value={form.idClient} 
                onChange={e => setForm({...form, idClient: e.target.value})} 
                required
              >
                <option value="">-- Sélectionner un client --</option>
                {clients.map(c => <option key={c.id} value={c.id}>{c.nom} {c.prenom}</option>)}
              </select>
            )}
          </div>

          {/* Bloc Produit */}
          <div className="space-y-4 pt-4 border-t border-slate-100">
            <h2 className="text-sm font-black text-slate-400 uppercase tracking-wider">Détails de la commande</h2>
            <div className="grid grid-cols-4 gap-4">
              <select 
                className="col-span-3 p-3 border rounded-xl bg-slate-50 outline-indigo-500" 
                value={form.idMedicament} 
                onChange={e => setForm({...form, idMedicament: e.target.value})} 
                required
              >
                <option value="">-- Choisir le médicament --</option>
                {medicaments.map(m => (
                  <option key={m.id} value={m.id} disabled={m.stock <= 0}>
                    {m.nom} — (En stock: {m.stock})
                  </option>
                ))}
              </select>
              <input 
                type="number" 
                min="1" 
                placeholder="Qté"
                className="p-3 border rounded-xl outline-indigo-500 bg-slate-50 text-center font-bold" 
                value={form.quantite} 
                onChange={e => setForm({...form, quantite: e.target.value})} 
                required 
              />
            </div>
          </div>

          <button className="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-4 rounded-2xl font-bold shadow-lg shadow-indigo-100 transition-all active:scale-[0.98]">
            Finaliser la transaction
          </button>
        </form>
      </div>

      {/* SECTION HISTORIQUE AVEC BARRE DE RECHERCHE */}
      <div className="max-w-5xl mx-auto bg-white rounded-3xl shadow-xl overflow-hidden border border-slate-100">
        <div className="bg-slate-800 p-5 text-white flex flex-col md:flex-row justify-between items-center gap-4">
          <div className="flex items-center gap-3">
            <History size={24} className="text-indigo-400" />
            <h2 className="font-bold text-lg">Historique des Ventes</h2>
          </div>

          {/* Barre de recherche dynamique */}
          <div className="relative w-full md:w-80">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <input 
              type="text"
              placeholder="Chercher un client ou un produit..."
              className="w-full bg-slate-700/50 border border-slate-600 p-2.5 pl-10 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 transition"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead className="bg-slate-50 text-slate-500 text-[11px] font-black uppercase tracking-widest border-b">
              <tr>
                <th className="p-5">Date / Heure</th>
                <th className="p-5">Client</th>
                <th className="p-5">Médicament</th>
                <th className="p-5">Quantité</th>
                <th className="p-5 text-center">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {historiqueFiltre.length > 0 ? (
                historiqueFiltre.map(v => (
                  <tr key={v.id} className="hover:bg-slate-50 transition">
                    <td className="p-5 text-xs text-slate-400 font-mono">{v.date}</td>
                    <td className="p-5 font-bold text-slate-700">{v.client}</td>
                    <td className="p-5 text-slate-600">{v.medicament}</td>
                    <td className="p-5">
                      <span className="bg-indigo-50 text-indigo-700 px-3 py-1 rounded-full text-xs font-black">
                        {v.quantite}
                      </span>
                    </td>
                    <td className="p-5 text-center">
                      <button 
                        onClick={() => handleAnnuler(v.id)} 
                        className="text-red-400 hover:text-red-600 hover:bg-red-50 p-2 rounded-full transition"
                        title="Annuler la vente"
                      >
                        <Trash2 size={18} />
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="p-20 text-center text-slate-400 italic">
                    {searchTerm ? "Aucun résultat trouvé pour cette recherche." : "Aucune vente enregistrée dans l'historique."}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}