package api;

import dao.ClientDAO;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClientsServlet extends HttpServlet {

    private final ClientDAO dao = new ClientDAO();
    private final Gson gson = new Gson();

    @Override
public void init() {
    System.out.println("🔥 ClientsServlet INIT OK");
}

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ CORS
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<Map<String, Object>> clients = dao.getAllClients();
            resp.getWriter().print(gson.toJson(clients));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(
                gson.toJson(Map.of("error", true, "message", e.getMessage()))
            );
            e.printStackTrace();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
