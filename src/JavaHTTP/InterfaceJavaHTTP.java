package JavaHTTP;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceJavaHTTP extends Remote {
    String handleGetRequest(String externalApiUrl) throws RemoteException;
}