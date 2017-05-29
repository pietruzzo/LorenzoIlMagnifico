package rmi;

import Exceptions.DomainException;
import lorenzo.MainGame;
import network.AbstractClient;
import server.rmi.IRMIServer;

import java.awt.*;
import java.io.IOException;
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
    public RMIClient(MainGame mainGame, String indirizzoIp, int porta)
    {
        super(mainGame, indirizzoIp, porta);
    }

    /**
     * Stabilisce il collegamento con il registro rmi
     */
    @Override
    public void ConnessioneServer() {
        Registry registry = null;
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

    //region Chiamate al server
    /**
     * Effettua il login di un giocatore (aggiungendolo al tabellone)
     */
    @Override
    public void Login(String nome) throws IOException {
        idGiocatore = server.Login(nome, this);
    }

    /**
     * Comunica al server di iniziare la partita
     */
    @Override
    public void IniziaPartita() {
        try {
            server.IniziaPartita(idGiocatore);
        } catch (IOException e) {
            this.HandleException(e);
        }
    }
    //endregion

    //region Chiamate dal server
    /**
     * Gestisce gli eventi relativi all'inizio di una partita
     */
    @Override
    public void PartitaIniziata() {
        System.out.println("Il server rmi mi ha detto che la partita è iniziata");
        this.getMainGame().PartitaIniziata();
    }
    //endregion

    /**
     * Gestisce le eccezioni lanciate lato server
     */
    private void HandleException(IOException eccezione)
    {
        this.getMainGame().MostraEccezione(eccezione.getMessage());
    }
}
