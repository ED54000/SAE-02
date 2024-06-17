package JDBC;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class RequetesSql {

    public JSONObject getRestaurants() {
        JSONObject jsonObject = new JSONObject();
        try {
            Connection connection = ConnectionDb.getConnection();

            String sql = "SELECT NOM, NUMERO, ADRESSE, LATITUDE, LONGITUDE, NBPLACES FROM RESTAU";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                String adr = "SELECT ADRESSE FROM ADRESSE WHERE ID = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(adr);
                preparedStatement1.setInt(1, set.getInt(3));
                ResultSet set1 = preparedStatement1.executeQuery();
                set1.next();
                Restaurant restaurant = new Restaurant(set.getString(1), set.getInt(2), set1.getString(1), set.getDouble(4), set.getDouble(5), set.getInt(6));
                jsonObject.append("data",restaurant.toJson());

            }
        } catch (SQLException e) {
            System.out.println("erreur lors la connexion : " + e.getMessage());
        }
        return jsonObject;
    }

    public void addRestaurant(String nom, int numero, int adresse, double latitude, double longitude, int nbPlaces) {
        try {
            Connection connection = ConnectionDb.getConnection();
            String sql = "INSERT INTO RESTAU (NOM,NUMERO,ADRESSE,LATITUDE, LONGITUDE,NBPLACES) " +
                         "VALUES ('"+ nom +"', "+ numero +", "+ adresse +", "+ latitude +", "+ longitude +", "+ nbPlaces +")";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            System.out.println("Restaurant ajouté avec succès");
        } catch (SQLException e) {
            System.out.println("erreur lors l'ajout d'un restaurant : " + e.getMessage());
        }
    }

    public void addReserv(int idrestaurant, String nom, String prenom, int nbconvives,String tel) {
        try {
            Connection connection = ConnectionDb.getConnection();
            String sql = "INSERT INTO RESERV (IDRESTAURANT,NOM,PRENOM,NBCONVIVES,COORD_TEL) " +
                         "VALUES ("+ idrestaurant +", '"+ nom +"', '"+ prenom +"', "+ nbconvives +", '"+ tel +"')";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            System.out.println("Reservation ajouté avec succès");
        } catch (SQLException e) {
            System.out.println("erreur lors l'ajout de la reservation : " + e.getMessage());
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
