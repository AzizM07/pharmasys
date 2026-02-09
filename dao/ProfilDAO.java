package dao;

import java.sql.*;

public class ProfilDAO {

    public static class Profil {
        public String nom;
        public String prenom;
        public String login;

        public Profil(String nom, String prenom, String login) {
            this.nom = nom;
            this.prenom = prenom;
            this.login = login;
        }
    }

    public Profil getProfil(String role, String login) {

        String table = role.equalsIgnoreCase("PHARMACIEN")
                ? "Pharmacien"
                : "Gestionnaire";

        String sql =
            "SELECT nom, prenom, login FROM " + table +
            " WHERE TRIM(UPPER(login)) = TRIM(UPPER(?))";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Profil(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("login")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
