
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class ClientVelib {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();

        // URL de l'API externe
        String externalApiUrl = "https://transport.data.gouv.fr/gbfs/nancy/station_information.json";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(externalApiUrl))
                .build();

        // Envoi d'une requête à l'API externe
        // Envoi de la réponse de l'API externe au serveur local
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ClientVelib::sendToLocalServer)
                .join();
    }

    private static void sendToLocalServer(String response) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8000/api"))
                .POST(HttpRequest.BodyPublishers.ofString(response))
                .build();

        // Envoi d'une requête POST avec les données de l'API externe au serveur local
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
    }
}

