package socket;

import Domain.DTO.AzioneBonusDTO;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Risorsa;
import Exceptions.NetworkException;
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
     * Effettua il login del giocatore
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
    public void IniziaPartita() throws NetworkException  {
        this.socketClientProtocol.IniziaPartita();
    }

    /**
     * Comunica al server la risposta al sostegno della chiesa
     * @param risposta true se sostiene, con false il giocatore viene scomunicato
     */
    @Override
    public void RispostaSostegnoChiesa(Boolean risposta) throws NetworkException {
        this.socketClientProtocol.RispostaSostegnoChiesa(risposta);
    }

    /**
     * Comunica al server l'intenzione di piazzare un familiare nello spazio azione associato
     * @param piazzaFamiliareDTO parametri relativi al piazzamento del familiare
     */
    @Override
    public void PiazzaFamiliare(PiazzaFamiliareDTO piazzaFamiliareDTO) throws NetworkException   {
        this.socketClientProtocol.PiazzaFamiliare(piazzaFamiliareDTO);
    }

    /**
     * Comunica al server l'intenzione di effettuare un'azione bonus
     * @param azioneBonusDTO parametri relativi all'azione bonus
     */
    @Override
    public void AzioneBonusEffettuata(AzioneBonusDTO azioneBonusDTO) throws NetworkException {
        this.socketClientProtocol.AzioneBonusEffettuata(azioneBonusDTO);
    }

    /**
     * Comunica al server il salto dell'azione bonus
     */
    @Override
    public void AzioneBonusSaltata() throws NetworkException {
        this.socketClientProtocol.AzioneBonusSaltata();
    }

    /**
     * Manda al server la scelta del privilegio del consiglio
     * @param risorsa risorse da aggiungere al giocatore
     */
    @Override
    public void RiscuotiPrivilegiDelConsiglio(Risorsa risorsa) throws NetworkException{
        this.socketClientProtocol.RiscuotiPrivilegiDelConsiglio(risorsa);
    }

    /**
     * Manda al server la scelta dell'effetto di default da attivare per le carte con scambia risorse
     * @param nomeCarta nome della carta alla quale impostare la scelta
     * @param sceltaEffetto indidce della scelta effettuata
     */
    @Override
    public void SettaSceltaEffetti(String nomeCarta, Integer sceltaEffetto) throws NetworkException
    {
        this.socketClientProtocol.SettaSceltaEffetti(nomeCarta, sceltaEffetto);
    }

    /**
     * Notifica il server della chiusura di un client
     */
    @Override
    public void NotificaChiusuraClient() throws NetworkException
    {
        this.socketClientProtocol.NotificaChiusuraClient();
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
