package server;

/**
 * Created by Portatile on 18/05/2017.
 */
public abstract class AbstractServer {

    private final Server server;

    /**
     * Costruttore astratto del server (socket o rmi)
     * @param server
     */
    public AbstractServer(Server server)
    {
        this.server = server;
    }

    public server.Server getServer() {
        return server;
    }

    public abstract void StartServer(int porta) throws Exception;
}
