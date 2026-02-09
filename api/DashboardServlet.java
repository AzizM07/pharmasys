package api;

import dao.DashboardDAO;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Map;

public class DashboardServlet extends HttpServlet {

    private final DashboardDAO dao = new DashboardDAO();
    private final Gson gson = new Gson();

    @Override
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
            Map<String, Object> data = dao.getDashboardStats();
            resp.getWriter().print(gson.toJson(data));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(Map.of("error", true, "message", e.getMessage())));
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
