package api;

import com.google.gson.Gson;
import dao.CommandeDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet gérant les commandes et les mises à jour de stock.
 * Reçoit l'ID du gestionnaire dynamiquement depuis le frontend.
 */
public class CommandeServlet extends HttpServlet {

    private final CommandeDAO dao = new CommandeDAO();
    private final Gson gson = new Gson();

    // Classe interne pour mapper le JSON envoyé par React
    private static class CommandeRequest {
        Integer idGestionnaire; 
        Integer idMedicament;
        Integer quantite;
    }

    /**
     * Configuration des entêtes CORS pour autoriser le frontend Vite/React
     */
    private void setCors(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCors(resp);
        resp.setContentType("application/json;charset=UTF-8");
        
        try {
            // Renvoie la liste de toutes les commandes passées
            resp.getWriter().print(gson.toJson(dao.getAllCommandesAPI()));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().print(gson.toJson(Map.of("error", true, "message", e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCors(resp);
        resp.setContentType("application/json;charset=UTF-8");

        try {
            // Désérialisation du JSON en objet Java
            CommandeRequest data = gson.fromJson(req.getReader(), CommandeRequest.class);

            // Validation de sécurité
            if (data == null || data.idGestionnaire == null || data.idMedicament == null || data.quantite == null) {
                resp.setStatus(400);
                resp.getWriter().print(gson.toJson(Map.of(
                    "error", true, 
                    "message", "Données incomplètes : idGestionnaire, idMedicament et quantite sont requis"
                )));
                return;
            }

            if (data.quantite <= 0) {
                resp.setStatus(400);
                resp.getWriter().print(gson.toJson(Map.of("error", true, "message", "La quantité doit être positive")));
                return;
            }

            // Debug console pour vérifier l'ID reçu
            System.out.println("Insertion commande : Gestionnaire ID = " + data.idGestionnaire);

            // Appel au DAO avec l'ID dynamique envoyé par le frontend
            dao.ajouterCommande(data.idGestionnaire, data.idMedicament, data.quantite);

            // Réponse de succès
            resp.getWriter().print(gson.toJson(Map.of("success", true)));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print(gson.toJson(Map.of("error", true, "message", "Erreur SQL ou serveur : " + e.getMessage())));
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Nécessaire pour le "pre-flight request" de CORS
        setCors(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}