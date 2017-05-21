package rmi;

import network.AbstractClient;
import server.rmi.IRMIServer;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Portatile on 18/05/2017.
 */
public class RMIClient extends AbstractClient implements IRMIClient {

    //region Proprieta
    private IRMIServer server;
    private short idGiocatore;
    //endregion

    /**
     * Costruttore del Client rmi
     */
    public RMIClient(String indirizzoIp, int porta)
    {
        super(indirizzoIp, porta);
    }

    /**
     * Stabilisce il collegamento con il registro rmi
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
     * Inizializza rmi
     */
    @Override
    public void InizializzaSocketProtocol() {
    }

    /**
     * Effettua il login di un giocatore (aggiungendolo al tabellone)
     * @throws Exception
     */
    @Override
    public void Login(String nome, Color colore) throws Exception {
        idGiocatore = server.Login(nome, colore, this);
    }
}
