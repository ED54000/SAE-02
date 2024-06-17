package JDBC;
    
import JDBC.InterfaceRequeteSql;
import JDBC.RequetesSql;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIJavaSQL {

    public static void main(String[] args) {
        int port = 1098;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        try {
            // Création de l'objet serveur
            RequetesSql service = new RequetesSql();

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
