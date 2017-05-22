package server;

import Domain.Tabellone;
import server.rmi.RMIServer;
import server.socket.SocketServer;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public class Server {

    //region Proprieta

    private SocketServer socketServer;
    private RMIServer rmiServer;
    private Tabellone tabellone;
    private HashMap<String, GiocatoreRemoto> listaGiocatori;
    private boolean accettaNuoviGiocatori;

    //Usato per gestire un login alla volta
    private static final Object MUTEX_GIOCATORI = new Object();
    //endregion

    /**
     * Costruttore del server
     */
    private Server()
    {
        this.socketServer = new SocketServer(this);
        this.rmiServer = new RMIServer(this);
        this.accettaNuoviGiocatori = true;
        this.listaGiocatori = new HashMap<>();
    }

    /**
     * Metodo main eseguito all'avvio
     */
    public static void main(String[] args) {

        Server server = new Server();

        //Mette il server in running
        try {
            //TODO:gestione eccezione, deve tirare un eccezione gestita
            server.StartServer(1337, 1338);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mette in ascolto entrambi i mezzi. Instaurerà una connessione solo tramite il canale scelto dal client.
     */
    private void StartServer(int portaSocket, int portaRMI) throws Exception {
        socketServer.StartServer(portaSocket);
        rmiServer.StartServer(portaRMI);
        this.tabellone = new Tabellone();
    }

    /**
     * Aggiunge un giocatore alla partita
     * @return l'id del giocatore appena inserito
     */
     public short AggiungiGiocatore(String nome, Color colore, GiocatoreRemoto giocatore) throws Exception {
        synchronized (MUTEX_GIOCATORI)
        {
            if(accettaNuoviGiocatori) {
                //Verifica la presenza di un altro giocatore con lo stesso username
                if (!listaGiocatori.containsKey(nome)) {
                    this.listaGiocatori.put(nome, giocatore);
                    short idGiocatore = this.tabellone.AggiungiGiocatore(nome, colore, giocatore);
                    System.out.println(String.format("Aggiunto il giocatore {0} con id {1}", nome, idGiocatore));
                    return idGiocatore;
                } else
                    throw new Exception("Esiste già un giocatore con lo stesso username.");
            }
            else
                throw new Exception("La partita è già iniziata.");
        }
    }

    /**
     * Ritorna il giocatore remoto dato il suo id
     * @param idGiocatore
     * @return
     */
    public GiocatoreRemoto GetGiocatoreById(short idGiocatore)
    {
        String nome = this.tabellone.GetNomeGiocatoreById(idGiocatore);
        return this.listaGiocatori.get(nome);
    }

    /**
     * Inizia la partita e avvia il primo turno
     */
    public void IniziaPartita()
    {
        this.accettaNuoviGiocatori = false;
        //Comunica l'inzio della partita agli altri giocatori

        for (GiocatoreRemoto giocatore : this.listaGiocatori.values()) {
            giocatore.PartitaIniziata();
        }

    }
}
