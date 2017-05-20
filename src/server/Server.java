package Server;

import Server.RMI.RMIServer;
import Server.Socket.SocketServer;

/**
 * Created by Portatile on 18/05/2017.
 */
public class Server {

    //region Proprieta

    private SocketServer socketServer;
    private RMIServer rmiServer;

    //endregion

    /**
     * Costruttore del server
     */
    private Server()
    {
        socketServer = new SocketServer();
        rmiServer = new RMIServer();
    }

    /**
     * Metodo main eseguito all'avvio
     */
    public static void main(String[] args) {

        Server server = new Server();

        //Mette il server in running
        try {
            //TODO:gestione eccezione, deve tirare un eccezione gestita
            server.StartServer(1337, 1338);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mette in ascolto entrambi i mezzi. Instaurerà una connessione solo tramite il canale scelto dal client.
     */
    private void StartServer(int portaSocket, int portaRMI) throws Exception {
        socketServer.StartServer(portaSocket);
        rmiServer.StartServer(portaRMI);
    }

}
