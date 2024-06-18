package JDBC;

import org.json.JSONObject;

import java.sql.*;
import java.util.Map;

public class RequetesSql implements InterfaceRequeteSql{



    public String getRestaurants() {
        JSONObject jsonObject = new JSONObject();
        try {
            Connection connection = ConnectionDb.getConnection();
            String sql = "SELECT ID, NOM, NUMERO, ADRESSE, LATITUDE, LONGITUDE, NBPLACES FROM RESTAU";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                String adr = "SELECT ADRESSE FROM ADRESSE WHERE ID = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(adr);
                preparedStatement1.setInt(1, set.getInt(4));
                ResultSet set1 = preparedStatement1.executeQuery();
                set1.next();
                Restaurant restaurant = new Restaurant(set.getInt(1),set.getString(2), set.getInt(3), set1.getString(1), set.getDouble(5), set.getDouble(6), set.getInt(7));
                set1.close();
                jsonObject.append("data",restaurant.toJson());
            }
            set.close();

        } catch (SQLException e) {
            System.out.println("erreur lors la connexion : " + e.getMessage());
        }
        return jsonObject.toString();

    }

    public int addAdresse(String adresse) {
        try {
            Connection connection = ConnectionDb.getConnection();
            String sql = "SELECT * FROM ADRESSE WHERE ADRESSE LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, adresse);
            ResultSet set = preparedStatement.executeQuery();
            if(set.next()){
                System.out.println("L'adresse existe deja");
                return set.getInt(1);
            }else {
                String sql_insert = "INSERT INTO ADRESSE (ADRESSE) " +
                        "VALUES ('"+ adresse +"')";
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql_insert);
                preparedStatement1.execute();
                System.out.println("Nouvelle adresse ajoutee");
                //on reexecute la premiere commande pour recuperer l'id de la nouvelle adresse
                ResultSet set2 = preparedStatement.executeQuery();
                set2.next();
                int res = set2.getInt(1);
                set2.close();
                return res;
            }
        } catch (SQLException e) {
            System.out.println("erreur lors la récupération de l'adresse : " + e.getMessage());
        }
        return -1;
    }

    public boolean addRestaurant(Map<String, String> map) {
        System.out.println(map);
        try {
            Connection connection = ConnectionDb.getConnection();
            int idAdresse = addAdresse(map.get("adresse"));
            System.out.println(idAdresse);
            String sql = "INSERT INTO RESTAU (NOM,NUMERO,ADRESSE,LATITUDE, LONGITUDE,NBPLACES) " +
                         "VALUES (? ,? ,? ,? ,? ,? )";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, map.get("nom"));
            preparedStatement.setString(2, map.get("numero"));
            preparedStatement.setInt(3, idAdresse);
            preparedStatement.setDouble(4, Double.parseDouble(map.get("latitude")));
            preparedStatement.setDouble(5, Double.parseDouble(map.get("longitude")));
            preparedStatement.setInt(6, Integer.parseInt(map.get("nbPlaces")));

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
            int newNbPlaces = getNbPlaces(Integer.parseInt(map.get("idrestaurant"))) - Integer.parseInt(map.get("nbconvives"));
            if(newNbPlaces < 0) {
                System.out.println("Pas assez de places disponibles");
                return false;
            } else {
                Connection connection = ConnectionDb.getConnection();
                String sql_insert = "INSERT INTO RESERV (IDRESTAURANT,NOM,PRENOM,NBCONVIVES,COORD_TEL) " +
                             "VALUES ("+ map.get("idrestaurant") +", '"+ map.get("nom") +"', '"+ map.get("prenom") +"', "+ map.get("nbconvives") +", '"+ map.get("tel") +"')";
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql_insert);
                preparedStatement1.execute();

                String sql_update = "UPDATE RESTAU SET (NBPLACES) = "+ newNbPlaces +" WHERE ID = "+ map.get("idrestaurant");
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql_update);
                preparedStatement2.execute();
                System.out.println("Reservation ajouté avec succès");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("erreur lors l'ajout de la reservation : " + e.getMessage());
        }
        return false;
    }

    public int getNbPlaces(int id) {
        try {
            Connection connection = ConnectionDb.getConnection();
            String sql = "SELECT NBPLACES FROM RESTAU WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet set = preparedStatement.executeQuery();
            set.next();
            return set.getInt(1);
        } catch (SQLException e) {
            System.out.println("erreur lors la connexion : " + e.getMessage());
        }
        return -1;
    }
}

