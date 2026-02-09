package dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    public Map<String, Object> login(String login, String pwd) {
        try (Connection cn = DBConnection.getConnection()) {
            // Test PHARMACIEN
            String sql1 = "SELECT id_pharmacien FROM Pharmacien WHERE login=? AND pwd=?";
            PreparedStatement ps = cn.prepareStatement(sql1);
            ps.setString(1, login);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Map.of("id", rs.getInt("id_pharmacien"), "role", "PHARMACIEN");
            }

            // Test GESTIONNAIRE
            String sql2 = "SELECT id_gestionnaire FROM Gestionnaire WHERE login=? AND pwd=?";
            ps = cn.prepareStatement(sql2);
            ps.setString(1, login);
            ps.setString(2, pwd);
            rs = ps.executeQuery();
            if (rs.next()) {
                return Map.of("id", rs.getInt("id_gestionnaire"), "role", "GESTIONNAIRE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}