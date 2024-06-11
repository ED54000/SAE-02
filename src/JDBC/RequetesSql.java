package JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RequetesSql {

        public void test() {
            try {
                Statement statement = ConnectionDb.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery
                        ("SELECT nom " +
                                "FROM Restaurant " +
                                "WHERE id = 1");
                while (resultSet.next()) {
                    System.out.println("nom en id 0 = " + resultSet.getString(1));
                }
            } catch (SQLException e) {
                System.out.println("erreur lors la connexion : " + e.getMessage());
            }
        }

//    public boolean exempleFonctionSelect(String email, String password) {
//        try {
//            Statement statement = ConnectionDb.getConnection().createStatement();
//            ResultSet resultSet = statement.executeQuery
//                    ("SELECT count(numserv) " +
//                            "FROM SERVEUR " +
//                            "WHERE email = '" + email + "' AND passwd = '" + password + "'");
//            if (resultSet.next()) {
//                //...
//            }
//        } catch (SQLException e) {
//            System.out.println("erreur lors la connexion : " + e.getMessage());
//        }
//        return false;
//    }

//    public void exempleFonctionInsert(int numTab, String date, int nbPersonnes) {
//        try {
//            Statement statement = ConnectionDb.getConnection().createStatement();
//            statement.executeUpdate("INSERT INTO reservation (numres, numtab, datres, nbpers) " +
//                    "VALUES (" + getNbReservation() + ", " +
//                    numTab + ", " +
//                    "TO_DATE('" + date + "', 'dd/mm/yyyy hh24:mi'), " +
//                    nbPersonnes + ")");
//            System.out.println("Réservation effectuée avec succès\n");
//        } catch (SQLException e) {
//            System.out.println("erreur lors de la réservation d'une table : " + e.getMessage());
//        }
//    }

}
