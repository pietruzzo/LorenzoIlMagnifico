package server.socket;

import server.GiocatoreRemoto;
import server.Server;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Portatile on 18/05/2017.
 */
public class GiocatoreSocket extends GiocatoreRemoto implements Runnable {

    //region Proprieta
    private final Socket socket;
    private final Server server;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final SocketServerProtocol protocol;
    //endregion

    /**
     * Costruttore
     */
    protected GiocatoreSocket(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(this.socket.getInputStream());
        protocol = new SocketServerProtocol(inputStream, outputStream, this);
    }

    /**
     * Metodo eseguito dal thread
     */
    @Override
    public void run() {
        //Il server resta sempre in ascolto
        try{
                while(true)
                {
                    Object obcjet = inputStream.readObject();
                    protocol.HandleRequest(obcjet);
                }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void Login(String nome, Color colore) throws Exception {
        this.server.AggiungiGiocatore(nome, colore, this);
    }
}
