import { useEffect, useState } from "react";

export default function Commandes() {
  const [stock, setStock] = useState([]);
  const [form, setForm] = useState({
    idMedicament: "",
    quantite: 1
  });

  // Charger la liste des médicaments au démarrage
  const fetchStock = () => {
    fetch("http://localhost:8080/api/medicaments")
      .then((r) => r.json())
      .then((data) => {
        console.log("Médicaments chargés:", data);
        setStock(data);
      })
      .catch((err) => console.error("Erreur chargement stock:", err));
  };

  useEffect(() => {
    fetchStock();
  }, []);

  const handleAddStock = async (e) => {
    e.preventDefault();

    // 1. Récupération de l'ID du gestionnaire stocké lors du login
    const userId = localStorage.getItem("userId");

    // 2. Vérifications de sécurité côté client
    if (!userId) {
      alert("Erreur : session expirée. Veuillez vous reconnecter.");
      return;
    }

    if (!form.idMedicament) {
      alert("Veuillez sélectionner un médicament");
      return;
    }

    // 3. Préparation des données (Les clés doivent matcher CommandeRequest en Java)
    const payload = {
      idGestionnaire: parseInt(userId),
      idMedicament: parseInt(form.idMedicament),
      quantite: parseInt(form.quantite)
    };

    console.log("Envoi au serveur:", payload);

    try {
      const res = await fetch("http://localhost:8080/api/commandes", {
        method: "POST",
        headers: { 
          "Content-Type": "application/json" 
        },
        body: JSON.stringify(payload)
      });

      const data = await res.json();

      if (data.success) {
        alert("Stock mis à jour avec succès !");
        // Réinitialiser le formulaire
        setForm({ idMedicament: "", quantite: 1 });
        // Rafraîchir l'affichage du stock
        fetchStock(); 
      } else {
        alert(data.message || "Erreur lors de l'ajout");
      }
    } catch (err) {
      console.error("Erreur réseau :", err);
      alert("Erreur réseau ou serveur inaccessible");
    }
  };

  return (
    <div className="p-8 bg-slate-50 min-h-screen">
      <h1 className="text-3xl font-bold mb-8 text-slate-800">Gestion du Stock</h1>

      {/* Formulaire d'ajout */}
      <div className="bg-white p-6 shadow-md rounded-xl w-full max-w-md mb-10">
        <h2 className="text-lg font-semibold mb-4 border-b pb-2">Passer une commande</h2>
        <form onSubmit={handleAddStock}>
          <div className="mb-4">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Sélectionner le médicament
            </label>
            <select
              className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
              value={form.idMedicament}
              onChange={(e) => setForm({ ...form, idMedicament: e.target.value })}
            >
              <option value="">-- Choisir un produit --</option>
              {stock.map((m) => (
                <option key={m.id_medicament || m.id} value={m.id_medicament || m.id}>
                  {m.nom} (En stock: {m.stock})
                </option>
              ))}
            </select>
          </div>

          <div className="mb-6">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Quantité à commander
            </label>
            <input
              type="number"
              min="1"
              className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
              value={form.quantite}
              onChange={(e) => setForm({ ...form, quantite: e.target.value })}
            />
          </div>

          <button 
            type="submit"
            className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded-lg transition-colors shadow"
          >
            Valider la commande
          </button>
        </form>
      </div>

      {/* Tableau récapitulatif */}
      <div className="bg-white shadow-md rounded-xl overflow-hidden">
        <table className="w-full text-left border-collapse">
          <thead className="bg-slate-100 text-slate-600 uppercase text-xs font-bold">
            <tr>
              <th className="p-4">Médicament</th>
              <th className="p-4 text-center">Quantité actuelle</th>
              <th className="p-4">Statut</th>
            </tr>
          </thead>
          <tbody>
            {stock.map((s) => (
              <tr key={s.id_medicament || s.id} className="border-t hover:bg-slate-50 transition">
                <td className="p-4 font-medium">{s.nom}</td>
                <td className="p-4 text-center font-mono">{s.stock}</td>
                <td className="p-4">
                  {s.stock < 10 ? (
                    <span className="px-2 py-1 bg-red-100 text-red-700 rounded text-xs font-bold">Stock Faible</span>
                  ) : (
                    <span className="px-2 py-1 bg-green-100 text-green-700 rounded text-xs font-bold">OK</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}