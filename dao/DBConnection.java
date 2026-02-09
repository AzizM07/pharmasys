package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/pharmacie_db",
                "root",
                "0000"
            );
        } catch (Exception e) {
            System.out.println(" Erreur de connexion à MySQL");
            e.printStackTrace();
            return null;
        }
    }
}
