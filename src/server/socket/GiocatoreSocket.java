package server.socket;

import Domain.Tabellone;
import Exceptions.DomainException;
import server.GiocatoreRemoto;
import server.Server;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public class GiocatoreSocket extends GiocatoreRemoto implements Runnable {

    //region Proprieta
    private final transient Socket socket;
    private final transient Server server;
    private final transient ObjectInputStream inputStream;
    private final transient ObjectOutputStream outputStream;
    private final transient SocketServerProtocol protocol;
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
    public void Login(String nome) throws DomainException {
        this.server.AggiungiGiocatore(nome,this);
    }

    /**
     * Se ci sono 4 giocatori la partita inizia in automatico
     */
    public void VerificaInizioPartita() {
        try {
            if(this.getPartita() != null)
                this.getPartita().VerificaInizioAutomatico();
        } catch (DomainException e) {
            protocol.ComunicaEccezione(e.getMessage());
        }
    }

    /**
     *  Inizia la partita
     */
    public void IniziaPartita(){
        try {
            this.getPartita().IniziaPartita();
        } catch (DomainException e) {
            protocol.ComunicaEccezione(e.getMessage());
        }
    }

    /**
     * Comunica al client l'inzio della partita
     */
    public void PartitaIniziata(Tabellone tabellone)
    {
        this.protocol.PartitaIniziata(tabellone);
    }

    /**
     *  Comunica al client l'inzio di un nuovo turno
     */
    public void IniziaTurno(int[] esitoDadi, HashMap<Integer, String> mappaCarte)
    {
        this.protocol.IniziaTurno(esitoDadi, mappaCarte);
    }

    /**
     * Comunica al client l'inzio di una nuova mossa
     * @param idGiocatore id del giocatore che deve effettuare la mossa
     */
    public void IniziaMossa(int idGiocatore)
    {
        this.protocol.IniziaMossa(idGiocatore);
    }
}
