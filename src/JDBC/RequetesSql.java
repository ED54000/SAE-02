package JDBC;

import org.json.JSONObject;

import java.sql.*;
import java.util.Map;

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

    public boolean addRestaurant(Map<String, String> map) {
        try {
            Connection connection = ConnectionDb.getConnection();
            String sql = "INSERT INTO RESTAU (NOM,NUMERO,ADRESSE,LATITUDE, LONGITUDE,NBPLACES) " +
                         "VALUES ('"+ map.get("nom") +"', "+ map.get("numero") +", "+ map.get("adresse") +", "+ map.get("latitude") +", "+ map.get("longitude") +", "+ map.get("nbPlaces") +")";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            System.out.println("Restaurant ajouté avec succès");
            return true;
        } catch (SQLException e) {
            System.out.println("erreur lors l'ajout d'un restaurant : " + e.getMessage());
        }
        return false;
    }

    public boolean addReserv(Map<String, String> map) {
        try {
            Connection connection = ConnectionDb.getConnection();
            String sql = "INSERT INTO RESERV (IDRESTAURANT,NOM,PRENOM,NBCONVIVES,COORD_TEL) " +
                         "VALUES ("+ map.get("idrestaurant") +", '"+ map.get("nom") +"', '"+ map.get("prenom") +"', "+ map.get("nbconvives") +", '"+ map.get("tel") +"')";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            System.out.println("Reservation ajouté avec succès");
            return true;
        } catch (SQLException e) {
            System.out.println("erreur lors l'ajout de la reservation : " + e.getMessage());
        }
        return false;
    }
}