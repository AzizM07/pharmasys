import { BrowserRouter, Routes, Route } from "react-router-dom";
import Gestionnaire from "./pages/DashboardGestionnaire";
import Pharmacien from "./pages/DashboardPharmacien";
import Login from "./pages/Login";
import Stock from "./pages/Stock";
import Ventes from "./pages/Ventes";
import Rapport from "./pages/Rapport";
import Clients from "./pages/Clients";
import Medicaments from "./pages/Medicaments";
import Alertes from "./pages/Alertes";
import Commandes from "./pages/commande";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
    <Route path="/clients" element={<Clients />} />
    <Route path="/commandes" element={<Commandes />} />
    <Route path="/medicaments" element={<Medicaments />} />
    <Route path="/alertes" element={<Alertes />} />
        <Route path="/stock" element={<Stock />} />
        <Route path="/ventes" element={<Ventes />} />
        <Route path="/rapport" element={<Rapport />} />
        <Route path="/dashboardPharmacien" element={<Pharmacien />} />
        <Route path="/dashboardGestionnaire" element={<Gestionnaire />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
