package server.socket;

import server.GiocatoreRemoto;
import server.Server;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Portatile on 18/05/2017.
 */
public class GiocatoreSocket extends GiocatoreRemoto implements Runnable {

    //region Proprieta
    private final Socket socket;
    private final Server server;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final SocketServerProtocol protocol;
    //endregion

    /**
     * Costruttore
     */
    protected GiocatoreSocket(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(this.socket.getInputStream());
        protocol = new SocketServerProtocol(inputStream, outputStream, this);
    }

    /**
     * Metodo eseguito dal thread
     */
    @Override
    public void run() {
        //Il server resta sempre in ascolto
        try{
                while(true)
                {
                    Object obcjet = inputStream.readObject();
                    protocol.HandleRequest(obcjet);
                }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Effettua il login del giocatore
     */
    public void Login(String nome) throws Exception {
        this.server.AggiungiGiocatore(nome,this);
    }

    /**
     * Se ci sono 4 giocatori la partita inizia in automatico
     */
    public void VerificaInizioPartita() throws Exception {
        this.getPartita().VerificaInizioAutomatico();
    }

    /**
     *  Inizia la partita
     */
    public void IniziaPartita() throws Exception {
        this.getPartita().IniziaPartita();
    }

    /**
     * Comunica al client l'inzio della partita
     */
    public void PartitaIniziata()
    {
        this.protocol.PartitaIniziata();
    }

}
