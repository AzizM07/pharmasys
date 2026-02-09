import { useEffect, useState } from "react"
import { Search, AlertCircle, History, Package, TrendingDown, Filter } from "lucide-react"

export default function Stock() {
  const [stock, setStock] = useState([])
  const [critique, setCritique] = useState([])
  const [historique, setHistorique] = useState([])
  const [searchTerm, setSearchTerm] = useState("")

  useEffect(() => {
    // Parallélisation des appels API pour plus de rapidité
    Promise.all([
      fetch("http://localhost:8080/api/medicaments").then(r => r.json()),
      fetch("http://localhost:8080/api/medicaments?critique=true&seuil=10").then(r => r.json()),
      fetch("http://localhost:8080/api/stock/historique").then(r => r.json())
    ]).then(([dataStock, dataCritique, dataHistorique]) => {
      setStock(dataStock)
      setCritique(dataCritique)
      setHistorique(dataHistorique)
    }).catch(err => console.error("Erreur chargement données:", err))
  }, [])

  // Filtrage dynamique pour la barre de recherche
  const filteredStock = stock.filter(m => 
    m.nom.toLowerCase().includes(searchTerm.toLowerCase())
  )

  return (
    <div className="flex min-h-screen bg-[#f8fafc]">
      <main className="flex-1 p-8 space-y-8">
        
        {/* HEADER & RECHERCHE */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold text-slate-800">Inventaire Pharmacie</h1>
            <p className="text-slate-500">Gérez vos produits et surveillez les ruptures de stock.</p>
          </div>
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 w-5 h-5" />
            <input 
              type="text"
              placeholder="Rechercher un médicament..."
              className="pl-10 pr-4 py-2 border border-slate-200 rounded-xl w-full md:w-80 focus:ring-2 focus:ring-indigo-500 outline-none transition-all shadow-sm"
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        {/* SECTION CRITIQUE (Alertes Rapides) */}
        {critique.length > 0 && (
          <section className="bg-red-50 border border-red-100 rounded-2xl p-6">
            <div className="flex items-center gap-2 mb-4 text-red-700">
              <AlertCircle className="w-6 h-6" />
              <h2 className="text-lg font-bold">Alertes de Stock Critique ({critique.length})</h2>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {critique.map(c => (
                <div key={c.id_medicament} className="bg-white p-4 rounded-xl shadow-sm border-l-4 border-red-500 flex justify-between items-center">
                  <div>
                    <p className="font-bold text-slate-800">{c.nom}</p>
                    <p className="text-xs text-slate-500">Seuil atteint</p>
                  </div>
                  <span className="text-2xl font-black text-red-600">{c.stock}</span>
                </div>
              ))}
            </div>
          </section>
        )}

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* TABLE PRINCIPALE (2/3 de l'écran) */}
          <section className="lg:col-span-2 space-y-4">
            <div className="flex items-center gap-2">
              <Package className="w-5 h-5 text-indigo-600" />
              <h2 className="text-xl font-bold text-slate-800">Stock actuel</h2>
            </div>
            <div className="bg-white shadow-xl shadow-slate-200/50 rounded-2xl overflow-hidden border border-slate-100">
              <table className="w-full text-left">
                <thead className="bg-slate-50 border-b border-slate-100">
                  <tr>
                    <th className="p-4 text-sm font-semibold text-slate-600">Produit</th>
                    <th className="p-4 text-sm font-semibold text-slate-600 text-center">Stock</th>
                    <th className="p-4 text-sm font-semibold text-slate-600 text-center">Prix Unit.</th>
                    <th className="p-4 text-sm font-semibold text-slate-600">Statut</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                  {filteredStock.map(m => (
                    <tr key={m.id_medicament} className="hover:bg-slate-50/50 transition-colors">
                      <td className="p-4">
                        <div className="font-bold text-slate-700">{m.nom}</div>
                        <div className="text-xs text-slate-400">{m.dosage}</div>
                      </td>
                      <td className="p-4 text-center">
                        <span className={`font-mono font-bold ${m.stock < 10 ? 'text-red-500' : 'text-slate-700'}`}>
                          {m.stock}
                        </span>
                      </td>
                      <td className="p-4 text-center font-medium text-slate-600">{m.prix_unitaire} DT</td>
                      <td className="p-4">
                        {m.stock === 0 ? (
                          <span className="bg-slate-100 text-slate-600 px-3 py-1 rounded-full text-xs font-bold uppercase">Rupture</span>
                        ) : m.stock < 10 ? (
                          <span className="bg-orange-100 text-orange-600 px-3 py-1 rounded-full text-xs font-bold uppercase">Bas</span>
                        ) : (
                          <span className="bg-green-100 text-green-600 px-3 py-1 rounded-full text-xs font-bold uppercase">Normal</span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>

          {/* HISTORIQUE (1/3 de l'écran) */}
          <section className="space-y-4">
            <div className="flex items-center gap-2">
              <History className="w-5 h-5 text-slate-600" />
              <h2 className="text-xl font-bold text-slate-800">Derniers mouvements</h2>
            </div>
            <div className="bg-white shadow-xl shadow-slate-200/50 rounded-2xl p-4 border border-slate-100 h-fit">
              <div className="space-y-6">
                {historique.map((h, i) => (
                  <div key={i} className="flex gap-4 relative">
                    {i !== historique.length - 1 && (
                      <div className="absolute left-[17px] top-8 w-0.5 h-10 bg-slate-100"></div>
                    )}
                    <div className={`w-9 h-9 rounded-full flex items-center justify-center shrink-0 ${
                      h.quantite > 0 ? 'bg-green-100 text-green-600' : 'bg-blue-100 text-blue-600'
                    }`}>
                      {h.quantite > 0 ? <TrendingDown className="w-4 h-4 rotate-180" /> : <TrendingDown className="w-4 h-4" />}
                    </div>
                    <div>
                      <p className="text-sm font-bold text-slate-700">{h.medicament}</p>
                      <p className="text-xs text-slate-500">
                        {h.quantite > 0 ? `Ajout de ${h.quantite}` : `Vente de ${Math.abs(h.quantite)}`} unités
                      </p>
                      <p className="text-[10px] text-slate-400 mt-1 italic">
                        {new Date(h.date).toLocaleDateString()} à {new Date(h.date).toLocaleTimeString()}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </section>

        </div>
      </main>
    </div>
  )
}