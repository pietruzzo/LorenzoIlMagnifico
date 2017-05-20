package Lorenzo;

import Network.AbstractClient;
import RMI.RMIClient;
import Socket.SocketClient;

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
    }

    /**
     * Effettua la connessione al server tramite Socket o RMI
     */
    public void SetConnessioneServer()
    {
        String indirizzoIP = "127.0.0.1";
        int portaSocket = 1337;
        int portaRMI = 1338;

        if(1 == 2)
            client = new SocketClient(indirizzoIP, portaSocket);
        else
            client = new RMIClient(indirizzoIP, portaRMI);

        client.ConnessioneServer();
        client.InizializzaSocketProtocol(); //Se il client è RMI il metodo non fa nulla
    }
}
