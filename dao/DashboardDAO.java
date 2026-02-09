package dao;

import java.sql.*;
import java.util.*;

public class DashboardDAO {

    /* ================= KPIs ================= */

    public int getTotalVentes() throws Exception {
        String sql = "SELECT COALESCE(SUM(quantite), 0) FROM Vente";
        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);
        }
    }

    public double getChiffreAffaires() throws Exception {
        String sql = """
            SELECT COALESCE(SUM(v.quantite * m.prix_unitaire), 0)
            FROM Vente v
            JOIN Medicament m ON v.id_medicament = m.id_medicament
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getDouble(1);
        }
    }

    public int getTotalStock() throws Exception {
        String sql = "SELECT COALESCE(SUM(stock), 0) FROM Medicament";
        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);
        }
    }

    public int getAlertesStock() throws Exception {
        //  seuil fixé à 10 (modifiable)
        String sql = "SELECT COUNT(*) FROM Medicament WHERE stock < 10";
        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);
        }
    }

    public int getTotalMedicaments() throws Exception {
        String sql = "SELECT COUNT(*) FROM Medicament";
        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);
        }
    }

    public int getTotalClients() throws Exception {
        String sql = "SELECT COUNT(*) FROM Client";
        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);
        }
    }

    /* ================= CHARTS ================= */

    public List<Map<String, Object>> ventesParJour() throws Exception {

        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT DATE(date) AS jour, SUM(quantite) AS total
            FROM Vente
            GROUP BY DATE(date)
            ORDER BY jour
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("name", rs.getString("jour"));
                row.put("value", rs.getInt("total"));
                list.add(row);
            }
        }
        return list;
    }

    public List<Map<String, Object>> ventesParMedicament() throws Exception {

        List<Map<String, Object>> list = new ArrayList<>();

        String sql = """
            SELECT m.nom AS name, SUM(v.quantite) AS value
            FROM Vente v
            JOIN Medicament m ON v.id_medicament = m.id_medicament
            GROUP BY m.nom
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("name", rs.getString("name"));
                row.put("value", rs.getInt("value"));
                list.add(row);
            }
        }
        return list;
    }

    public Map<String, Object> getDashboardStats() throws Exception {

        Map<String, Object> data = new HashMap<>();

        data.put("ventes", getTotalVentes());
        data.put("revenus", getChiffreAffaires() + " DT");
        data.put("stock", getTotalStock());
        data.put("alertes", getAlertesStock());
        data.put("totalMedicaments", getTotalMedicaments());
        data.put("totalClients", getTotalClients());
        data.put("evolutionVentes", ventesParJour());
        data.put("ventesParMedicament", ventesParMedicament());

        return data;
    }
}
