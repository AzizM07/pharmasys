package api;

import com.google.gson.Gson;
import dao.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/api/alertes")
public class AlertesServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setContentType("application/json;charset=UTF-8");

        List<Map<String, Object>> alertes = new ArrayList<>();
        String sql = "SELECT id_medicament, nom, stock FROM Medicament WHERE stock < 10";

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> a = new HashMap<>();
                a.put("id", rs.getInt("id_medicament"));
                a.put("nom", rs.getString("nom"));
                a.put("stock", rs.getInt("stock"));
                alertes.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.getWriter().print(gson.toJson(alertes));
    }
}