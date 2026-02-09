package api;

import com.google.gson.Gson;
import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();

    static class LoginRequest {
        String login;
        String pwd;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            LoginRequest body = gson.fromJson(req.getReader(), LoginRequest.class);
            Map<String, Object> user = userDAO.login(body.login, body.pwd);

            if (user == null) {
                resp.getWriter().print(gson.toJson(Map.of("role", null)));
            } else {
                // Renvoie par ex: {"id": 2, "role": "GESTIONNAIRE"}
                resp.getWriter().print(gson.toJson(user));
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().print(gson.toJson(Map.of("error", "SERVER_ERROR")));
        }
    }
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}