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
        try {
            Random rnd = new Random();
            int rndNumber = rnd.nextInt(10);
            this.Login("michele"+rndNumber);

            //this.IniziaPartita();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Effettua la connessione al server tramite socket o rmi
     */
    public void SetConnessioneServer()
    {
        String indirizzoIP = "127.0.0.1";
        int portaSocket = 1337;
        int portaRMI = 1338;

        if(1 == 12)
            client = new SocketClient(indirizzoIP, portaSocket);
        else
            client = new RMIClient(indirizzoIP, portaRMI);

        client.ConnessioneServer();
        client.InizializzaSocketProtocol(); //Se il client è rmi il metodo non fa nulla
    }

    /**
     * Effettua il login del giocatore
     */
    public void Login(String nome) throws Exception {
        client.Login(nome);
    }

    /**
     * Comincia la partita, sarà il turno del primo giocatore loggato
     */
    public void IniziaPartita() throws Exception {
        client.IniziaPartita();
    }
}
