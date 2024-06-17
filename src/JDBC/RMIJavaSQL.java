package JavaHTTP;
    
import JDBC.InterfaceRequeteSql;
import JDBC.RequetesSql;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;

public class RMIJavaSQL {

    public static void main(String[] args) {
        ConnectionDb.setUsername(args[0]);
        ConnectionDb.setPassword(args[1]);
        Connection connection = ConnectionDb.getConnection();

        int port = 1098;
        if(args.length > 2){
            port = Integer.parseInt(args[2]);
        }
        try {
            // Création de l'objet serveur
            RequetesSql service = new RequetesSql(connection);

            // Exportation de l'objet serveur
            InterfaceRequeteSql stub = (InterfaceRequeteSql) UnicastRemoteObject.exportObject(service, 0);

            // Création du registre RMI
            Registry registry = LocateRegistry.createRegistry(port); // Port par défaut pour RMI

            // Enregistrement de l'objet serveur dans le registre
            registry.rebind("sql", stub);

            System.out.println("Serveur RMI de requete sql prêt.");
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage du serveur RMI : " + e.toString());
            e.printStackTrace();
        }
    }
}
