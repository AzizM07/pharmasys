package dao;

import java.sql.*;
import java.util.*;

public class ClientDAO {

    public List<Map<String, Object>> getAllClients() {

        List<Map<String, Object>> clients = new ArrayList<>();

        String sql = """
            SELECT id_client AS id, nom, prenom, email, adresse
            FROM client
        """;

        try (Connection cn = DBConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> c = new HashMap<>();
                c.put("id", rs.getInt("id"));
                c.put("nom", rs.getString("nom"));
                c.put("prenom", rs.getString("prenom"));
                c.put("email", rs.getString("email"));
                c.put("adresse", rs.getString("adresse"));
                clients.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clients;
    }
}
