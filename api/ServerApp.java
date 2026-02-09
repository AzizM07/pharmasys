package api;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServerApp {

    public static void main(String[] args) throws Exception {

        Server server = new Server(8080);

        ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // --- AUTH ---
        context.addServlet(new ServletHolder(new LoginServlet()), "/api/login");

        // --- DASHBOARD (Statistiques globales) ---
        context.addServlet(new ServletHolder(new DashboardServlet()), "/api/dashboard");

        // --- CLIENTS ---
        context.addServlet(new ServletHolder(new ClientsServlet()), "/api/clients");

        // --- MEDICAMENTS ---
        context.addServlet(new ServletHolder(new MedicamentServlet()), "/api/medicaments/*");

        // --- STOCK ---
        context.addServlet(new ServletHolder(new StockHistoriqueServlet()), "/api/stock/historique");
        
        // NOUVEAU : Route pour les alertes de stock critique
        context.addServlet(new ServletHolder(new AlertesServlet()), "/api/alertes");

        // --- VENTE ---
        context.addServlet(new ServletHolder(new VenteServlet()), "/api/vente");
        
        // NOUVEAU : Route pour les rapports détaillés et CA
        context.addServlet(new ServletHolder(new RapportServlet()), "/api/rapport");

        // --- COMMANDE ---
        context.addServlet(new ServletHolder(new CommandeServlet()), "/api/commandes");

        server.setHandler(context);
        
        // Optionnel : Gestion du CORS au niveau du serveur si tu ne veux pas le mettre dans chaque Servlet
        // Mais pour l'instant, tes Servlets le gèrent individuellement avec setHeader.

        server.start();

        System.out.println(" Serveur PharmaSys lancé sur http://localhost:8080");
        System.out.println(" Rapport API: http://localhost:8080/api/rapport");
        System.out.println(" Alertes API: http://localhost:8080/api/alertes");
        
        server.join();
    }
}