package Socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Portatile on 17/05/2017.
 */
public class SocketProtocol {

    //region Proprieta
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    //endregion

    /**
     * Costruttore
     */
    public SocketProtocol(ObjectInputStream inputStream, ObjectOutputStream outputStream)
    {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }
}
