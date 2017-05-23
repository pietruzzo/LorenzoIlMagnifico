package server;

import Domain.Tabellone;
import server.rmi.RMIServer;
import server.socket.SocketServer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public class Server {

    //region Proprieta

    private SocketServer socketServer;
    private RMIServer rmiServer;
    private ArrayList<GiocatoreRemoto> listaGiocatori;
    private ArrayList<Partita> listaPartite;
    private static short maxIdGiocatore = 0;

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
        this.listaGiocatori = new ArrayList<>();
        this.listaPartite = new ArrayList<>();
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
    }

    /**
     * Aggiunge un giocatore alla partita da iniziare
     * @return l'id del giocatore appena inserito
     */
     public short AggiungiGiocatore(String nome, GiocatoreRemoto giocatore) throws Exception {
        synchronized (MUTEX_GIOCATORI)
        {
            Partita partitaDaIniziare = this.GetPartitaDaIniziare();
            maxIdGiocatore++;
            partitaDaIniziare.AggiungiGiocatore(maxIdGiocatore, nome, giocatore);
            this.listaGiocatori.add(giocatore);
            System.out.println(String.format("Aggiunto il giocatore %s con id %d", nome, maxIdGiocatore));
            return maxIdGiocatore;
        }
    }

    /**
     * Ritorna il giocatore remoto dato il suo id
     * @param idGiocatore
     * @return
     */
    public GiocatoreRemoto GetGiocatoreById(short idGiocatore)
    {
        return this.listaGiocatori.stream().filter(x -> x.getIdGiocatore() == idGiocatore).findFirst().orElse(null);
    }

    /**
     * Ritorna la partita da iniziare
     * @return
     */
    private Partita GetPartitaDaIniziare()
    {
        //Cerca una partita ancora da iniziare
        Partita partitaToStart = this.listaPartite.stream().filter(x -> !x.isIniziata()).findFirst().orElse(null);

        //se non ci sono partite ancora da iniziare ne crea una nuova
        if(partitaToStart == null)
        {
            partitaToStart = new Partita(this);
            this.listaPartite.add(partitaToStart);

            System.out.println("Partita Creata");
        }

        return partitaToStart;
    }

}
