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
    }


    //region Handler eventi
    private void Login()
    {
        try {
            String nomeUtente = (String) this.inputStream.readObject();
            Color colore = (Color) this.inputStream.readObject();

            this.LoginGiocatore(nomeUtente, colore);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //endregion

    /**
     * Effettua il login del giocatore e comunica l'esito dell'operazione al client
     */
    private void LoginGiocatore(String nome, Color colore) throws IOException {
        int codiceRisposta = ProtocolEvents.OK;

        try {
            this.giocatore.Login(nome, colore);
        } catch (Exception e) {
            codiceRisposta = ProtocolEvents.USER_ESISTENTE;
        }

        outputStream.writeObject(codiceRisposta);
        outputStream.flush();
    }

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
