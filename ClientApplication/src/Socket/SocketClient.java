package Socket;

import Network.AbstractClient;

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

    private SocketProtocol socketProtocol;
    //endregion


    /**
     * Costruttore
     */
    public SocketClient(String indirizzoIp, int porta)
    {
        super(indirizzoIp, porta);
    }

    /**
     * Effettua la connessione al server
     */
    @Override
    public void ConnessioneServer() {
        //TODO gestione eccezione
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
    public void InizializzaSocketProtocol()
    {
        this.socketProtocol = new SocketProtocol(this.inputStream, this.outputStream);
    }
}
