package api;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServerApp {

    public static void main(String[] args) throws Exception {

        // ✅ Utiliser le port fourni par Render ou fallback sur 8080
        String portEnv = System.getenv("PORT");
        int port = portEnv != null ? Integer.parseInt(portEnv) : 8080;

        Server server = new Server(port);

        ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // --- AUTH ---
        context.addServlet(new ServletHolder(new LoginServlet()), "/api/login");

        // --- DASHBOARD ---
        context.addServlet(new ServletHolder(new DashboardServlet()), "/api/dashboard");

        // --- CLIENTS ---
        context.addServlet(new ServletHolder(new ClientsServlet()), "/api/clients");

        // --- MEDICAMENTS ---
        context.addServlet(new ServletHolder(new MedicamentServlet()), "/api/medicaments/*");

        // --- STOCK ---
        context.addServlet(new ServletHolder(new StockHistoriqueServlet()), "/api/stock/historique");

        // --- ALERTES ---
        context.addServlet(new ServletHolder(new AlertesServlet()), "/api/alertes");

        // --- VENTE ---
        context.addServlet(new ServletHolder(new VenteServlet()), "/api/vente");

        // --- RAPPORT ---
        context.addServlet(new ServletHolder(new RapportServlet()), "/api/rapport");

        // --- COMMANDE ---
        context.addServlet(new ServletHolder(new CommandeServlet()), "/api/commandes");

        server.setHandler(context);

        server.start();

        // 🔹 Affichage des URLs publiques (Render utilisera HTTPS automatiquement)
        System.out.println(" Serveur PharmaSys lancé sur le port " + port);
        System.out.println(" Rapport API: /api/rapport");
        System.out.println(" Alertes API: /api/alertes");

        server.join();
    }
}
