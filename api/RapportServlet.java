package api;

import com.google.gson.Gson;
import dao.RapportDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/api/rapport")
public class RapportServlet extends HttpServlet {
    private final RapportDAO dao = new RapportDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setContentType("application/json;charset=UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("totalVentes", dao.getTotalVentes());
        data.put("totalQuantite", dao.getTotalQuantiteVendue());
        data.put("chiffreAffaires", dao.getChiffreAffaires());

        // Conversion du Vector<Vector> en Liste de Maps pour le JSON
        List<Map<String, Object>> details = new ArrayList<>();
        Vector<Vector<Object>> rows = dao.ventesParMedicament();
        for (Vector<Object> row : rows) {
            Map<String, Object> item = new HashMap<>();
            item.put("nom", row.get(0));
            item.put("total_qte", row.get(1));
            item.put("total_prix", row.get(2));
            details.add(item);
        }
        data.put("ventesParMedicament", details);

        resp.getWriter().print(gson.toJson(data));
    }
}