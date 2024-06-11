import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ProxyServer {
    public static void main(String[] args) throws IOException {

        /*//connexion à la base de données
        ConnectionDb.setUsername(args[0]);
        ConnectionDb.setPassword(args[1]);

        Connection connection = JDBC.ConnectionDb.getConnection();

        //on cree un objet requetesSql pour pouvoir utiliser les methodes de cette classe
        RequetesSql requetesSql = new RequetesSql();
        boolean test = requetesSql.test();*/

        // Crée et démarre un serveur HTTP sur le port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        // Crée les contexts
        server.createContext("/infoVelibs", new Handler("https://transport.data.gouv.fr/gbfs/nancy/station_information.json"));
        server.createContext("/statusVelibs", new Handler("https://transport.data.gouv.fr/gbfs/nancy/station_status.json"));
        server.setExecutor(null); // Crée un exécuteur par défaut
        server.start();
        System.out.println("Server started on port 8000");
    }

    // Gestionnaire pour traiter les requêtes HTTP
    static class Handler implements HttpHandler {

        public String externalApiUrl;

        public Handler(String externalApiUrl){
            this.externalApiUrl = externalApiUrl;
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
                System.out.println("envoie des données");
                // Traitement des requêtes GET : récupérer les données de l'API externe
                handleGetRequest(exchange);
            } else {
                // Méthode non autorisée
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
                exchange.close();
            }
        }

        private void handleGetRequest(HttpExchange exchange) throws IOException {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(externalApiUrl))
                    .build();

            // Envoi d'une requête à l'API externe et renvoi de la réponse au client
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            System.out.println("donnée reçu");
                            System.out.println(response);
                            sendResponse(exchange, response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
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
