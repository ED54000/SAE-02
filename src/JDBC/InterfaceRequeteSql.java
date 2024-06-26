package JDBC;

import org.json.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface InterfaceRequeteSql extends Remote {
    String getRestaurants() throws RemoteException;

    boolean addRestaurant(Map<String, String> map) throws RemoteException;

    boolean addReserv(Map<String, String> map) throws RemoteException;
}
