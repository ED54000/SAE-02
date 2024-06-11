package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDb {

    private static Connection connectionDb;

    private static String username,password;

    public static void setUsername(String name) {
        ConnectionDb.username = name;
    }

    public static void setPassword(String pass){
        ConnectionDb.password = pass;
    }

    public static Connection getConnection(){
        if (connectionDb == null){
            try{
                // charger le driver JDBC
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Driver loaded");
                // établir une connexion
                String url= "jdbc:mysql://webetu.iutnc.univ-lorraine.fr/phpmya/index.php?route=/table/sql&db=duchene55u";
                connectionDb = DriverManager.getConnection(url,ConnectionDb.username,ConnectionDb.password);
                System.out.println("Database connected");
            }catch (Exception e){
                System.out.println("erreur lors la connexion à la base de données : "+e.getMessage());
            }
        }
        return connectionDb;
    }
}
