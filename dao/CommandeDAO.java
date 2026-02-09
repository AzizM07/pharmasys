package dao;

import java.sql.*;
import java.util.*;

public class CommandeDAO {


    public void ajouterCommande(int idGestionnaire, int idMedicament, int quantite) throws Exception {
        try (Connection cn = DBConnection.getConnection()) {

        
            PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO Commande (id_gestionnaire, id_medicament, quantite, date) VALUES (?, ?, ?, NOW())"
            );
            ps.setInt(1, idGestionnaire);
            ps.setInt(2, idMedicament);
            ps.setInt(3, quantite);
            ps.executeUpdate();

        
            ps = cn.prepareStatement(
                "UPDATE Medicament SET stock = stock + ? WHERE id_medicament=?"
            );
            ps.setInt(1, quantite);
            ps.setInt(2, idMedicament);
            ps.executeUpdate();

         
            new StockHistoriqueDAO().ajouterMouvement(idMedicament, quantite);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public List<Map<String, Object>> getAllCommandesAPI() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT c.id_commande, c.id_medicament, c.quantite, c.date, m.nom " +
                     "FROM Commande c JOIN Medicament m ON c.id_medicament = m.id_medicament " +
                     "ORDER BY c.date DESC";

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id_commande"));
                row.put("medicamentId", rs.getInt("id_medicament"));
                row.put("medicamentNom", rs.getString("nom"));
                row.put("quantite", rs.getInt("quantite"));
                row.put("date", rs.getTimestamp("date"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
