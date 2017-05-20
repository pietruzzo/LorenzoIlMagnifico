package server.Socket;

import server.AbstractServer;
import server.Server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Portatile on 18/05/2017.
 */
public class SocketServer extends AbstractServer{

    //region Proprieta

    private ServerSocket socket;

    //endregion

    /**
     * Costruttore
     */
    public SocketServer(Server server)
    {
        super(server);
    }

    /**
     * Avvia il socket lato server
     */
    @Override
    public void StartServer(int porta) {
        try {
            socket = new ServerSocket(porta);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new RequestHandler().start();
    }

    private class RequestHandler extends Thread {

        //ogni volta che si collega un client viene istanziato un GiocatoreSocket
        @Override
        public void run() {
            while(true){
                try {
                    java.net.Socket clientSocket = socket.accept();
                    GiocatoreSocket giocatoreSocket = new GiocatoreSocket(clientSocket, getServer());
                    new Thread(giocatoreSocket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
