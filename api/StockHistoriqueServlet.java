package api;

import com.google.gson.Gson;
import dao.StockHistoriqueDAO;
import jakarta.servlet.http.*;

import java.io.IOException;

public class StockHistoriqueServlet extends HttpServlet {

    private final StockHistoriqueDAO dao = new StockHistoriqueDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        resp.getWriter().print(gson.toJson(dao.getHistorique()));
    }
}
