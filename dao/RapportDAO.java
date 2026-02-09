package dao;

import java.sql.*;
import java.util.Vector;

public class RapportDAO {


    public int getTotalVentes() {

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT COUNT(*) FROM Vente")) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalQuantiteVendue() {

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT SUM(quantite) FROM Vente")) {

            if (rs.next()) return rs.getInt(1); // null => 0 auto

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getChiffreAffaires() {

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("""
                SELECT SUM(v.quantite * m.prix_unitaire)
                FROM Vente v
                JOIN Medicament m
                  ON v.id_medicament = m.id_medicament
             """)) {

            if (rs.next()) return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Vector<Vector<Object>> ventesParMedicament() {

        Vector<Vector<Object>> data = new Vector<>();

        String sql = """
            SELECT m.nom,
                   SUM(v.quantite) AS total_qte,
                   SUM(v.quantite * m.prix_unitaire) AS total_prix
            FROM Vente v
            JOIN Medicament m
              ON v.id_medicament = m.id_medicament
            GROUP BY m.nom
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("nom"));
                row.add(rs.getInt("total_qte"));
                row.add(rs.getDouble("total_prix"));
                data.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}
