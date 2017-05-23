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
    public void Login(String nome) throws Exception {
        idGiocatore = server.Login(nome, this);
    }

    /**
     * Comunica al server di iniziare la partita
     */
    @Override
    public void IniziaPartita() throws Exception {
        server.IniziaPartita(idGiocatore);
    }

    /**
     * Gestisce gli eventi relativi all'inizio di una partita
     */
    @Override
    public void PartitaIniziata() {
        System.out.println("Il server rmi mi ha detto che la partita è iniziata");
        //TODO: disabilita il bottone per iniziare la partita
    }
}
