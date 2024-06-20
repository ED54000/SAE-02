import JDBC.ConnectionDb;
import JDBC.InterfaceRequeteSql;
import JDBC.RequetesSql;
import JavaHTTP.InterfaceJavaHTTP;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ProxyServer {
    public static void main(String[] args) throws IOException {
        InterfaceJavaHTTP service1;
        InterfaceRequeteSql service2;
        try {
            // connexion aux deux clients RMI
            String host1 = "localhost";
            int port1 = 1099;
            String host2 = "localhost";
            int port2 = 1098;

            if (args.length > 0) {
                host1 = args[0];
                if (args.length > 1) {
                    port1 = Integer.parseInt(args[1]);
                    if (args.length > 2) {
                        host2 = args[2];
                        if (args.length > 3) {
                            port2 = Integer.parseInt(args[3]);
                        }
                    }
                }
            }

            // Création des registres RMI
            Registry registry1 = LocateRegistry.getRegistry(host1, port1);
            Registry registry2 = LocateRegistry.getRegistry(host2, port2);

            // Récupération des objets distants
            service1 = (InterfaceJavaHTTP) registry1.lookup("http");
            service2 = (InterfaceRequeteSql) registry2.lookup("sql");


        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion aux serveurs RMI : " + e.toString());
            e.printStackTrace();
            return;
        }


        // Crée et démarre un serveur HTTP sur le port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        // Crée les contexts
        server.createContext("/trafic", new Handler("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json", service1));
        server.createContext("/etudeSup", new Handler("https://data.enseignementsup-recherche.gouv.fr/api/explore/v2.1/catalog/datasets/fr-esr-implantations_etablissements_d_enseignement_superieur_publics/records?limit=20&refine=etablissement_uai%3A%220542493S%22&refine=localisation%3A%22Alsace%20-%20Champagne-Ardenne%20-%20Lorraine%3ENancy-Metz%3EMeurthe-et-Moselle%3ENancy%22", service1));
        server.createContext("/restaurants", new HandlerSQL(0, service2));
        server.createContext("/reserver", new HandlerSQL(1, service2));
        server.createContext("/ajouterRestaurant", new HandlerSQL(2, service2));
        server.setExecutor(null); // Crée un exécuteur par défaut
        server.start();
        System.out.println("Server started on port 8000");
    }

    // Gestionnaire pour traiter les requêtes HTTP
    static class Handler implements HttpHandler {

        public String externalApiUrl;
        public InterfaceJavaHTTP service1;

        public Handler(String externalApiUrl, InterfaceJavaHTTP service1) {
            this.externalApiUrl = externalApiUrl;
            this.service1 = service1;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Ajoute les en-têtes CORS
            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                // Répond aux requêtes OPTIONS pour CORS
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
            } else if ("GET".equals(exchange.getRequestMethod())) {
                System.out.println("Envoie des données HTTP");
                // Traitement des requêtes GET : récupérer les données de l'API externe
                sendResponse(exchange, service1.handleGetRequest(externalApiUrl));
            } else {
                // Méthode non autorisée
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
                exchange.close();
            }
        }

        private void sendResponse(HttpExchange exchange, String response) throws IOException {
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "application/json");

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    static class HandlerSQL implements HttpHandler {
        int num;
        InterfaceRequeteSql service2;

        public HandlerSQL(int num, InterfaceRequeteSql service2) {
            this.num = num;
            this.service2 = service2;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Ajoute les en-têtes CORS
            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                // Répond aux requêtes OPTIONS pour CORS
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
            } else if ("GET".equals(exchange.getRequestMethod())) {
                System.out.println("Envoi des données SQL " + num);
                // Traitement des requêtes GET : récupérer les données de l'API externe
                // Récupère les paramètres de la requête
                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = queryToMap(query);

                String reponse;
                switch (num) {
                    case 0:
                        reponse = service2.getRestaurants();
                        break;
                    case 1:
                        reponse = service2.addReserv(params) ? "Reservation ajoutée avec succès" : "Erreur lors de l'ajout de la reservation";
                        break;
                    case 2:
                        reponse = service2.addRestaurant(params) ? "Restaurant ajouté avec succès" : "Erreur lors de l'ajout du restaurant";
                        break;
                    default:
                        reponse = "Erreur";
                        break;
                }
                // envoie des parametres et appel de la fonction dans requetesSQL

                sendResponse(exchange, reponse);
            } else {
                // Méthode non autorisée
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
                exchange.close();
            }
        }

        private Map<String, String> queryToMap(String query) throws UnsupportedEncodingException {
            Map<String, String> result = new HashMap<>();
            if (query != null) {
                String[] params = query.split("&");
                for (String param : params) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = pair.length > 1 ? URLDecoder.decode(pair[1], "UTF-8") : "";
                    result.put(key, value);
                }
            }
            return result;
        }

        private void sendResponse(HttpExchange exchange, String response) throws IOException {
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "application/json");

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }
}
