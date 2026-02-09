package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.MedicamentDAO;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.stream.Collectors;

public class MedicamentServlet extends HttpServlet {

    private final MedicamentDAO dao = new MedicamentDAO();
    private final Gson gson = new Gson();

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setContentType("application/json;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setAccessControlHeaders(resp);
        String path = req.getPathInfo();

        if (path != null && path.equals("/critique")) {
            String seuilParam = req.getParameter("seuil");
            int seuil = (seuilParam != null) ? Integer.parseInt(seuilParam) : 10;
            resp.getWriter().print(gson.toJson(dao.getStockCritique(seuil)));
        } else {
            resp.getWriter().print(gson.toJson(dao.getAllMedicamentsAPI()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setAccessControlHeaders(resp);
        
        // Lire le JSON du corps de la requête
        String body = req.getReader().lines().collect(Collectors.joining());
        JsonObject json = gson.fromJson(body, JsonObject.class);

        try {
            String nom = json.get("nom").getAsString();
            String dosage = json.get("dosage").getAsString();
            int stock = json.get("stock").getAsInt();
            double prix = json.get("prix").getAsDouble();

            dao.ajouterMedicament(nom, dosage, stock, prix);
            resp.getWriter().print("{\"success\": true, \"message\": \"Médicament ajouté\"}");
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}