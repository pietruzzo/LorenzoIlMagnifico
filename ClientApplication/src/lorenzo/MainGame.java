package lorenzo;

import network.AbstractClient;
import rmi.RMIClient;
import socket.SocketClient;

import java.awt.*;
import java.util.Random;

/**
 * Created by Portatile on 17/05/2017.
 */
public class MainGame {

    //region Proprieta
    /**
     * Classe astratta del client
     */
     private AbstractClient client;
    //endregion

    protected MainGame () {
    }

    public void Start()
    {
        this.SetConnessioneServer();
        Random rnd = new Random();
        int rndNumber = rnd.nextInt(10);
        this.Login("michele" +rndNumber);

        //this.IniziaPartita();
    }

    /**
     * Effettua la connessione al server tramite socket o rmi
     */
    public void SetConnessioneServer()
    {
        String indirizzoIP = "127.0.0.1";
        int portaSocket = 1337;
        int portaRMI = 1338;

        if(1 == 1)
            client = new SocketClient(this, indirizzoIP, portaSocket);
        else
            client = new RMIClient(this, indirizzoIP, portaRMI);

        client.ConnessioneServer();
        client.InizializzaSocketProtocol(); //Se il client è rmi il metodo non fa nulla
    }

    /**
     * Effettua il login del giocatore
     */
    public String Login(String nome) {
        try {
            client.Login(nome);
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Comincia la partita, sarà il turno del primo giocatore loggato
     */
    public void IniziaPartita() {
        client.IniziaPartita();
    }

    /**
     * Metodo chiamato quando viene confermato l'inizio di una nuova partita
     */
    public void PartitaIniziata() {
        //TODO: disabilita il bottone per iniziare la partita
    }

    /**
     * Mostra l'errore all'utente
     * @param message messaggio d'errore
     */
    public void MostraEccezione(String message) {
        //TODO: chiamare l'interfaccia per mostrare il messaggio di errore (piccola form con l'icona di errore e il bottone OK?)
    }
}
