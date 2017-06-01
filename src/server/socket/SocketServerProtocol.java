package server.socket;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public class SocketServerProtocol {

    //region Proprieta
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final GiocatoreSocket giocatore;

    private final HashMap<Object, RequestHandler> listaEventHandler;
    private static final Object WRITE_TO_CLIENT_MUTEX = new Object();
    //endregion

    /**
     * Costruttore del protocollo di comunicazione del socket server
     */
    public SocketServerProtocol(ObjectInputStream input, ObjectOutputStream output, GiocatoreSocket giocatore)
    {
        this.inputStream = input;
        this.outputStream = output;
        this.giocatore = giocatore;
        this.listaEventHandler = new HashMap<>();
        this.PopolaListaEventHandler();
    }
    /**
     * Specifica tutti i possibili eventi ricevibili dal client e ad ognuno associa un metodo per gestirlo
     */
    private void PopolaListaEventHandler()
    {
        this.listaEventHandler.put(ProtocolEvents.LOGIN, this::Login);
        this.listaEventHandler.put(ProtocolEvents.INIZIA_PARTITA, this::IniziaPartita);
    }

    //region Handler eventi
    private void Login()
    {
        try {
            String nomeUtente = (String) this.inputStream.readObject();
            this.LoginGiocatore(nomeUtente);

            //Mi assicuro che venga tirato su l'handler lato client
            Thread.sleep(500);
            this.giocatore.VerificaInizioPartita();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //endregion


    //region Metodi per gestire i messaggi ricevuti Client
    /**
     * Effettua il login del giocatore e comunica l'esito dell'operazione al client
     */
    private void LoginGiocatore(String nome) throws IOException {
        int codiceRisposta;

        try {
            this.giocatore.Login(nome);
            codiceRisposta = ProtocolEvents.OK;
        } catch (Exception e) {
            codiceRisposta = ProtocolEvents.USER_ESISTENTE;
        }

        outputStream.writeObject(codiceRisposta);
        outputStream.flush();
    }

    /**
     * Inizia la partita
     */
    private void IniziaPartita()
    {
        this.giocatore.IniziaPartita();
    }
    //endregion


    //region Messaggi dal Server al Client
    /**
     * Comunica l'eccezione al client
     */
    public void ComunicaEccezione(String exceptionMessage)
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try{
                outputStream.writeObject(ProtocolEvents.TIRATA_ECCEZIONE);
                outputStream.writeObject(exceptionMessage);
                outputStream.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Comunica ai client l'avvenuto inizio della partita
     */
    public void PartitaIniziata()
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.PARTITA_INIZIATA);
                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Comunica ai client l'inizio di un nuovo turno
     */
    public void IniziaTurno(int[] esitoDadi, HashMap<Integer, String> mappaCarte)
    {
        synchronized (WRITE_TO_CLIENT_MUTEX) {
            try {
                this.outputStream.writeObject(ProtocolEvents.INIZIO_TURNO);
                this.outputStream.writeObject(esitoDadi);
                this.outputStream.writeObject(mappaCarte);

                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //endregion


    /**
     * Dato uno specifico evento fa partire il metodo destinato a gestirlo
     * @param object evento lanciato lato client
     */
    public void HandleRequest(Object object)
    {
        RequestHandler handler = listaEventHandler.get(object);
        if(handler != null)
            handler.Handle();
    }

    @FunctionalInterface
    private interface RequestHandler{
        //Tutte le volte che il server scrive un messaggio al client
        //viene invocato questo metodo, a seconda del tipo di richiesta fatta
        //verrà implementato un handler diverso, seguenda la listaEventHandler
        void Handle();
    }
}
