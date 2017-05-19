package RMI;

import Network.AbstractClient;
import Server.RMI.IRMIServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Portatile on 18/05/2017.
 */
public class RMIClient extends AbstractClient implements IRMIClient {

    private IRMIServer server;

    /**
     * Costruttore del Client RMI
     */
    public RMIClient(String indirizzoIp, int porta)
    {
        super(indirizzoIp, porta);
    }

    /**
     * Stabilisce il collegamento con il registro RMI
     */
    @Override
    public void ConnessioneServer() {
        Registry registry = null;
        //TODO: gestione eccezioni
        try {
            registry = LocateRegistry.getRegistry(getIndirizzoIp(), getPorta());
            server = (IRMIServer) registry.lookup("IRMIServer");
            UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inizializza RMI
     */
    @Override
    public void InizializzaSocketProtocol() {
    }


}
