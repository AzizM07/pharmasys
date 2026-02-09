package dao;

import java.sql.*;
import java.util.Vector;

public class StockHistoriqueDAO {

    public void ajouterMouvement(int idMed, int quantite) {

        String sql = """
            INSERT INTO StockHistorique (id_medicament, quantite, date)
            VALUES (?, ?, NOW())
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idMed);
            ps.setInt(2, quantite);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<Vector<Object>> getHistorique() {

        Vector<Vector<Object>> data = new Vector<>();

        String sql = """
            SELECT h.id_medicament,
                   m.nom AS medicament,
                   h.quantite,
                   h.date
            FROM StockHistorique h
            JOIN Medicament m
              ON h.id_medicament = m.id_medicament
            ORDER BY h.date DESC
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_medicament"));
                row.add(rs.getString("medicament"));
                row.add(rs.getInt("quantite"));
                row.add(rs.getTimestamp("date"));

                data.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}
