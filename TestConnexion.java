import dao.DBConnection;
import java.sql.Connection;

public class TestConnexion {

    public static void main(String[] args) {

        Connection cn = DBConnection.getConnection();

        if (cn != null) {
            System.out.println(" Connexion réussie !");
        } else {
            System.out.println(" Connexion échouée");
        }
    }
}
