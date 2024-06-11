import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ProxyServer {
    // Variable pour stocker la réponse de l'API externe
    private static String externalApiResponse = "";

    public static void main(String[] args) throws IOException {
        // Crée et démarre un serveur HTTP sur le port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        // Crée un contexte pour gérer les requêtes sur "/api"
        server.createContext("/api", new MyHandler());
        server.setExecutor(null); // Crée un exécuteur par défaut
        server.start();
        System.out.println("Server started on port 8000");
    }

    // Gestionnaire pour traiter les requêtes HTTP
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if ("POST".equals(t.getRequestMethod())) {
                // Traitement des requêtes POST : lire et stocker les données envoyées par le client Java
                InputStream is = t.getRequestBody();
                externalApiResponse = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                t.sendResponseHeaders(200, 0); // Envoie un code de réponse 200 OK
                t.close();
                System.out.println("reception des donnée");
            } else if ("GET".equals(t.getRequestMethod())) {
                // Traitement des requêtes GET : envoyer les données stockées au client JavaScript
                t.sendResponseHeaders(200, externalApiResponse.length());
                OutputStream os = t.getResponseBody();
                os.write(externalApiResponse.getBytes());
                os.close();
                System.out.println("envoie des données");
            } else {
                // Méthode non autorisée
                t.sendResponseHeaders(405, -1); // 405 Method Not Allowed
                t.close();
            }
        }
    }
}
