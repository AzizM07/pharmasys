package api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.VenteDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class VenteServlet extends HttpServlet {
    private final VenteDAO dao = new VenteDAO();
    private final Gson gson = new Gson();

    private void setCors(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS"); // Ajout de DELETE
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        resp.setContentType("application/json;charset=UTF-8");
        try {
            resp.getWriter().print(gson.toJson(dao.getAllVentesAPI()));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().print(gson.toJson(Map.of("success", false, "message", e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCors(resp);
        resp.setContentType("application/json;charset=UTF-8");

        try {
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = gson.fromJson(req.getReader(), mapType);

            if (data == null || data.get("idPharmacien") == null || data.get("idMedicament") == null) {
                resp.setStatus(400);
                resp.getWriter().print(gson.toJson(Map.of("success", false, "message", "Données manquantes")));
                return;
            }

            int idPharmacien = ((Number) data.get("idPharmacien")).intValue();
            int idMedicament = ((Number) data.get("idMedicament")).intValue();
            int quantite = ((Number) data.get("quantite")).intValue();
            Integer idClient = null;

            if (data.get("clientInfo") != null) {
                String clientJson = gson.toJson(data.get("clientInfo"));
                Type stringMapType = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> c = gson.fromJson(clientJson, stringMapType);
                idClient = dao.creerClient(c.get("nom"), c.get("prenom"), c.get("email"), c.get("adresse"));
            } else if (data.get("idClient") != null) {
                idClient = ((Number) data.get("idClient")).intValue();
            }

            boolean success = dao.enregistrerVente(idPharmacien, idClient, idMedicament, quantite);
            resp.getWriter().print(gson.toJson(Map.of("success", success, "message", success ? "Vente effectuée" : "Stock insuffisant")));

        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().print(gson.toJson(Map.of("success", false, "message", e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        resp.setContentType("application/json;charset=UTF-8");
        try {
            // On récupère l'ID depuis le paramètre de l'URL ?id=...
            String idStr = req.getParameter("id");
            if (idStr != null) {
                int idVente = Integer.parseInt(idStr);
                boolean success = dao.annulerVente(idVente);
                resp.getWriter().print(gson.toJson(Map.of("success", success)));
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().print(gson.toJson(Map.of("success", false, "message", e.getMessage())));
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setCors(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}