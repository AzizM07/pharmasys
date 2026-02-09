package dao;

import java.sql.*;
import java.util.*;

public class VenteDAO {

    // Enregistre une nouvelle vente avec mise à jour du stock
    public boolean enregistrerVente(int idPharmacien, int idClient, int idMedicament, int quantite) {
        String sqlVente = "INSERT INTO Vente (id_pharmacien, id_client, id_medicament, quantite, date) VALUES (?, ?, ?, ?, NOW())";
        String sqlStock = "UPDATE Medicament SET stock = stock - ? WHERE id_medicament = ? AND stock >= ?";

        try (Connection cn = DBConnection.getConnection()) {
            cn.setAutoCommit(false);
            try (PreparedStatement psStock = cn.prepareStatement(sqlStock);
                 PreparedStatement psVente = cn.prepareStatement(sqlVente)) {
                
                psStock.setInt(1, quantite);
                psStock.setInt(2, idMedicament);
                psStock.setInt(3, quantite);
                
                if (psStock.executeUpdate() > 0) {
                    psVente.setInt(1, idPharmacien);
                    psVente.setInt(2, idClient);
                    psVente.setInt(3, idMedicament);
                    psVente.setInt(4, quantite);
                    psVente.executeUpdate();
                    cn.commit();
                    return true;
                }
            }
            cn.rollback();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Annule une vente et REND le stock au médicament
    public boolean annulerVente(int idVente) {
        String sqlSelect = "SELECT id_medicament, quantite FROM Vente WHERE id_vente = ?";
        String sqlDelete = "DELETE FROM Vente WHERE id_vente = ?";
        String sqlRestore = "UPDATE Medicament SET stock = stock + ? WHERE id_medicament = ?";

        try (Connection cn = DBConnection.getConnection()) {
            cn.setAutoCommit(false);
            int idMed = -1, qte = 0;

            // 1. Trouver les infos de la vente
            try (PreparedStatement ps1 = cn.prepareStatement(sqlSelect)) {
                ps1.setInt(1, idVente);
                ResultSet rs = ps1.executeQuery();
                if (rs.next()) {
                    idMed = rs.getInt("id_medicament");
                    qte = rs.getInt("quantite");
                }
            }

            if (idMed != -1) {
                // 2. Rendre le stock
                try (PreparedStatement ps2 = cn.prepareStatement(sqlRestore)) {
                    ps2.setInt(1, qte);
                    ps2.setInt(2, idMed);
                    ps2.executeUpdate();
                }
                // 3. Supprimer la vente
                try (PreparedStatement ps3 = cn.prepareStatement(sqlDelete)) {
                    ps3.setInt(1, idVente);
                    ps3.executeUpdate();
                }
                cn.commit();
                return true;
            }
            cn.rollback();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // Récupère toutes les ventes avec le nom du médicament et du client
    public List<Map<String, Object>> getAllVentesAPI() throws Exception {
        List<Map<String, Object>> ventes = new ArrayList<>();
        String sql = "SELECT v.id_vente, v.quantite, v.date, m.nom as med_nom, c.nom as cli_nom " +
                     "FROM Vente v " +
                     "JOIN Medicament m ON v.id_medicament = m.id_medicament " +
                     "LEFT JOIN Client c ON v.id_client = c.id_client " +
                     "ORDER BY v.date DESC";
        try (Connection cn = DBConnection.getConnection(); 
             Statement st = cn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> v = new HashMap<>();
                v.put("id", rs.getInt("id_vente"));
                v.put("medicament", rs.getString("med_nom"));
                v.put("client", rs.getString("cli_nom") != null ? rs.getString("cli_nom") : "Anonyme");
                v.put("quantite", rs.getInt("quantite"));
                v.put("date", rs.getTimestamp("date").toString());
                ventes.add(v);
            }
        }
        return ventes;
    }

    public int creerClient(String nom, String prenom, String email, String adresse) throws SQLException {
        String sql = "INSERT INTO Client (nom, prenom, email, adresse) VALUES (?, ?, ?, ?)";
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, email != null ? email : "");
            ps.setString(4, adresse != null ? adresse : "");
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }
}