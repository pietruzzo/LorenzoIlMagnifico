package socket;

import lorenzo.MainGame;
import network.AbstractClient;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Portatile on 17/05/2017.
 */
public class SocketClient extends AbstractClient {

    //region Proprieta
    private Socket socket;

    private ObjectInputStream inputStream;

    private ObjectOutputStream outputStream;

    private socket.SocketClientProtocol socketClientProtocol;
    //endregion


    /**
     * Costruttore
     */
    public SocketClient(MainGame mainGame, String indirizzoIp, int porta)
    {
        super(mainGame, indirizzoIp, porta);
    }

    /**
     * Effettua la connessione al server
     */
    @Override
    public void ConnessioneServer() {
        try {
            socket = new Socket(getIndirizzoIp(), getPorta());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream .flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configura il protocollo del socket
     */
    @Override
    public void InizializzaSocketProtocol()
    {
        this.socketClientProtocol = new SocketClientProtocol(this.inputStream, this.outputStream, this.getMainGame());
    }

    /**
     * Effettua il login del GiocatoreGraphic
     */
    @Override
    public void Login(String nome) throws Exception {
        this.socketClientProtocol.Login(nome);
        this.AvviaThreadRicezioneMessaggi();
    }

    /**
     * La partita inizia automaticamente se è stato raggiunto il numero massimo di giocatori
     */
    @Override
    public void VerificaInizioAutomatico() throws IOException {
        this.socketClientProtocol.VerificaInizioAutomatico();
    }

    /**
     * Comunica al server di inziare la partita
     */
    @Override
    public void IniziaPartita()  {
        this.socketClientProtocol.IniziaPartita();
    }

    /**
     * Comunica al server la risposta al sostegno della chiesa
     * @param risposta true se sostiene, con false il giocatore viene scomunicato
     */
    @Override
    public void RispostaSostegnoChiesa(Boolean risposta) {
        this.socketClientProtocol.RispostaSostegnoChiesa(risposta);
    }


    /**
     * Avvia il thread che gestisce la ricezione dei messaggi da parte del server
     */
    private void AvviaThreadRicezioneMessaggi()
    {
        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.start();
    }

    private class ResponseHandler extends Thread {

        //Gestisce i messaggi in arrivo dal server
        @Override
        public void run() {
            while(true){
                boolean esci = false;
                try {
                    Object object = inputStream.readObject();
                    socketClientProtocol.HandleResponse(object);
                } catch (IOException | ClassNotFoundException  e ) {
                    e.printStackTrace();
                    esci = true;
                }
                if(esci)
                    break;
            }
        }
    }
}
