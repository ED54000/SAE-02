package JavaHTTP;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIJavaHTTP {
    public static void main(String[] args) {
        int port = 1099;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        try {
            // Création de l'objet serveur
            JavaHTTP service = new JavaHTTP();
            // Exportation de l'objet serveur
            InterfaceJavaHTTP stub = (InterfaceJavaHTTP) UnicastRemoteObject.exportObject(service, 0);

            // Création du registre RMI
            Registry registry = LocateRegistry.createRegistry(port); // Port par défaut pour RMI

            // Enregistrement de l'objet serveur dans le registre
            registry.rebind("http", stub);

            System.out.println("Serveur RMI de requete http prêt.");
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage du serveur RMI : " + e.toString());
            e.printStackTrace();
        }
    }
}
