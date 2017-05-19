package Server.Socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Portatile on 18/05/2017.
 */
public class SocketServerProtocol {

    //region Proprieta
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    //endregion

    /**
     * Costruttore del protocollo di comunicazione del socket server
     */
    public SocketServerProtocol(ObjectInputStream input, ObjectOutputStream output)
    {
        this.inputStream = input;
        this.outputStream = output;
    }
}
