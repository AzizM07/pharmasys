package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import dao.StockHistoriqueDAO;
import model.Medicament;

public class MedicamentDAO {
    public List<Medicament> getAllMedicamentsAPI() {

    List<Medicament> list = new ArrayList<>();

    String sql = """
        SELECT id_medicament, nom, dosage, stock, prix_unitaire
        FROM Medicament
    """;

    try (Connection cn = DBConnection.getConnection();
         Statement st = cn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            list.add(new Medicament(
                rs.getInt("id_medicament"),
                rs.getString("nom"),
                rs.getString("dosage"),
                rs.getInt("stock"),
                rs.getDouble("prix_unitaire")
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}


   
    public Vector<Vector<Object>> getStockCritique(int seuil) {

        Vector<Vector<Object>> data = new Vector<>();

        String sql = """
            SELECT id_medicament, nom, dosage, stock, prix_unitaire
            FROM Medicament
            WHERE stock <= ?
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, seuil);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_medicament"));
                row.add(rs.getString("nom"));
                row.add(rs.getString("dosage"));
                row.add(rs.getInt("stock"));
                row.add(rs.getDouble("prix_unitaire"));
                data.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    
    public Vector<Vector<Object>> getAllMedicaments() {

        Vector<Vector<Object>> data = new Vector<>();

        String sql = """
            SELECT id_medicament, nom, dosage, stock, prix_unitaire
            FROM Medicament
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_medicament"));
                row.add(rs.getString("nom"));
                row.add(rs.getString("dosage"));
                row.add(rs.getInt("stock"));
                row.add(rs.getDouble("prix_unitaire"));
                data.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    
    public Vector<Vector<Object>> getMedicamentsForVente() {

        Vector<Vector<Object>> data = new Vector<>();

        String sql = """
            SELECT id_medicament, nom, stock
            FROM Medicament
            WHERE stock > 0
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_medicament"));
                row.add(rs.getString("nom"));
                row.add(rs.getInt("stock"));
                data.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    
    public void ajouterMedicament(String nom, String dosage, int stock, double prix) {

        String sql = """
            INSERT INTO Medicament(nom, dosage, stock, prix_unitaire)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                 sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nom);
            ps.setString(2, dosage);
            ps.setInt(3, stock);
            ps.setDouble(4, prix);
            ps.executeUpdate();

          
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int idMed = keys.getInt(1);

             
                StockHistoriqueDAO histDao = new StockHistoriqueDAO();
                histDao.ajouterMouvement(idMed, stock);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void updateStock(int idMed, int nouveauStock) {

        try (Connection cn = DBConnection.getConnection()) {

            
            PreparedStatement ps = cn.prepareStatement(
                "SELECT stock FROM Medicament WHERE id_medicament=?"
            );
            ps.setInt(1, idMed);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return;

            int ancienStock = rs.getInt("stock");

          
            ps = cn.prepareStatement(
                "UPDATE Medicament SET stock=? WHERE id_medicament=?"
            );
            ps.setInt(1, nouveauStock);
            ps.setInt(2, idMed);
            ps.executeUpdate();

           
            int diff = nouveauStock - ancienStock;

            StockHistoriqueDAO histDao = new StockHistoriqueDAO();
            histDao.ajouterMouvement(idMed, diff);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
