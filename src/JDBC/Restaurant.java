package JDBC;
import org.json.JSONObject;
public class Restaurant {

    private String nom;
    private int numero;
    private String adresse;
    private double latitude;
    private double longitude;
    private int nbTables;

    public Restaurant(String nom, int numero, String adresse, double latitude, double longitude, int nbTables) {

        this.nom = nom;
        this.numero = numero;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nbTables = nbTables;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nom", this.nom);
        jsonObject.put("numero", this.numero);
        jsonObject.put("adresse", this.adresse);
        jsonObject.put("latitude", this.latitude);
        jsonObject.put("longitude", this.longitude);
        jsonObject.put("nbTables", this.nbTables);
        return jsonObject;
    }

    public static Restaurant fromJson(JSONObject jsonObject) {
        String nom = jsonObject.getString("nom");
        int numero = jsonObject.getInt("numero");
        String adresse = jsonObject.getString("adresse");
        double latitude = jsonObject.getDouble("latitude");
        double longitude = jsonObject.getDouble("longitude");
        int nbTables = jsonObject.getInt("nbTables");
        return new Restaurant(nom, numero, adresse, latitude, longitude, nbTables);
    }



}
