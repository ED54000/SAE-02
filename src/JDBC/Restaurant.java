package JDBC;

import org.json.JSONObject;

public class Restaurant {
    private int id;
    private String nom;
    private int numero;
    private String adresse;
    private double latitude;
    private double longitude;
    private int nbPlaces;

    public Restaurant(int id, String nom, int numero, String adresse, double latitude, double longitude, int nbPlaces) {
        this.id = id;
        this.nom = nom;
        this.numero = numero;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nbPlaces = nbPlaces;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("nom", this.nom);
        jsonObject.put("numero", this.numero);
        jsonObject.put("adresse", this.adresse);
        jsonObject.put("latitude", this.latitude);
        jsonObject.put("longitude", this.longitude);
        jsonObject.put("nbPlaces", this.nbPlaces);
        return jsonObject;
    }

    public static Restaurant fromJson(JSONObject jsonObject) {
        int id = jsonObject.getInt("id");
        String nom = jsonObject.getString("nom");
        int numero = jsonObject.getInt("numero");
        String adresse = jsonObject.getString("adresse");
        double latitude = jsonObject.getDouble("latitude");
        double longitude = jsonObject.getDouble("longitude");
        int nbPlaces = jsonObject.getInt("nbPlaces");
        return new Restaurant(id, nom, numero, adresse, latitude, longitude, nbPlaces);
    }


}
