package socket;

import server.socket.ProtocolEvents;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by Portatile on 17/05/2017.
 */
public class SocketClientProtocol {

    //region Proprieta
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    private final HashMap<Object, ResponseHandler> listaEventHandler;
    //endregion

    /**
     * Costruttore
     */
    public SocketClientProtocol(ObjectInputStream inputStream, ObjectOutputStream outputStream)
    {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.listaEventHandler = new HashMap<>();
        this.PopolaListaEventHandler();
    }

    /**
     * Specifica tutti i possibili eventi ricevibili dal server e ad ognuno associa un metodo per gestirlo
     */
    private void PopolaListaEventHandler()
    {
        this.listaEventHandler.put(ProtocolEvents.PARTITA_INIZIATA, this::PartitaIniziata);
    }

    /**
     * Gestisce l'inizio di una partita
     */
    private void PartitaIniziata()
    {
        //TODO: disabilita il bottone per iniziare la partita
    }

    /**
     * Comunica al server le informazioni per aggiungere il giocatore alla partita
     */
    public void Login(String nome, Color colore) throws Exception {
        int codiceRisposta = ProtocolEvents.OK;
        try {
            outputStream.writeObject(ProtocolEvents.LOGIN);
            outputStream.writeObject(nome);
            outputStream.writeObject(colore);

            outputStream.flush();
            codiceRisposta = (int)inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Se era già presente un utente con l'username specificato
        if(codiceRisposta == ProtocolEvents.USER_ESISTENTE)
            throw new Exception("Esiste già un utente con questo username");
    }

    /**
     * Comunica al server di iniziare la partita
     */
    public void IniziaPartita()
    {
        try {
            outputStream.writeObject(ProtocolEvents.INIZIA_PARTITA);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dato uno specifico evento fa partire il metodo destinato a gestirlo
     * @param object evento lanciato lato server
     */
    public void HandleResponse(Object object)
    {
        ResponseHandler handler = listaEventHandler.get(object);
        if(handler != null)
            handler.Handle();
    }

    @FunctionalInterface
    private interface ResponseHandler{
        //Tutte le volte che il server scrive un messaggio al client
        //viene invocato questo metodo, a seconda del tipo di richiesta fatta
        //verrà implementato un handler diverso, seguenda la listaEventHandler
        void Handle();
    }
}
