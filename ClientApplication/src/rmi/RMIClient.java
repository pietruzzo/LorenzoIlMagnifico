package rmi;

import Domain.Tabellone;
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
import java.util.HashMap;

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
     * Effettua il login di un GiocatoreGraphic (aggiungendolo al tabellone)
     */
    @Override
    public void Login(String nome) throws IOException {
        idGiocatore = server.Login(nome, this);
    }

    /**
     * La partita inizia automaticamente se è stato raggiunto il numero massimo di giocatori
     */
    @Override
    public void VerificaInizioAutomatico() throws IOException {
        server.VerificaInizioAutomatico(idGiocatore);
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
    public void PartitaIniziata(Tabellone tabellone) {
        this.getMainGame().PartitaIniziata(tabellone);
    }

    /**
     * Gestisce l'evento di inizio turno
     * @param esitoDadi valore dei dadi da mantenere per tutto il turno
     * @param mappaCarte posizione delle carte sulle torri
     * @throws RemoteException
     */
    @Override
    public void IniziaTurno(int[] esitoDadi, HashMap<Integer, String> mappaCarte) {
        this.getMainGame().IniziaTurno(esitoDadi, mappaCarte);
    }

    /**
     * Comunica l'inzio di una nuova mossa
     * @param idGiocatore id del giocatore che deve effettuare la mossa
     */
    @Override
    public void IniziaMossa(int idGiocatore) {
        this.getMainGame().IniziaMossa(idGiocatore);
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
