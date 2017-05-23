package server.rmi;

import rmi.IRMIClient;
import server.AbstractServer;
import server.GiocatoreRemoto;
import server.Server;

import java.awt.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Portatile on 19/05/2017.
 */
public class RMIServer extends AbstractServer implements IRMIServer {

    /**
     * Costruttore
     */
    public RMIServer(Server server)
    {
        super(server);
    }

    /**
     * Avvia il server rmi
     */
    @Override
    public void StartServer(int porta) throws Exception {
        Registry registro = creaCaricaRegistro(porta);
        try {
            registro.bind("IRMIServer", this);
            UnicastRemoteObject.exportObject(this, porta);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Se non è ancora stato creato, crea il registro.
     * Altrimenti prova a ritornare il registro creato in precedenza.
     */
    private Registry creaCaricaRegistro(int porta) throws Exception {
        try {
            return LocateRegistry.createRegistry(porta);
        } catch (RemoteException e) {
            //Il registro è già stato creato
        }
        try {
            return LocateRegistry.getRegistry(porta);
        } catch (RemoteException e) {
            throw new Exception("Impossibile avviare il registro rmi");
        }
    }


    /**
     * Ottiene il giocatore dato l'id
     */
    private GiocatoreRemoto GetGiocatoreById(short idGiocatore)
    {
        return getServer().GetGiocatoreById(idGiocatore);
    }

    /**
     * Effettua il login del giocatore
     * Salva anche il riferimento per chiamare i metodi lato client attraverso rmi
     */
    public short Login(String nome, IRMIClient rmiClient) throws Exception {
        short idGiocatore = getServer().AggiungiGiocatore(nome, new GiocatoreRMI(rmiClient));
        GetGiocatoreById(idGiocatore).getPartita().VerificaInizioAutomatico();
        return idGiocatore;
    }

    /**
     * Inizia una nuova partita
     */
    public void IniziaPartita(short idGiocatore) throws Exception {
        GetGiocatoreById(idGiocatore).getPartita().IniziaPartita();
    }
}
