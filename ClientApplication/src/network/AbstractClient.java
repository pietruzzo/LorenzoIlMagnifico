package network;

import java.awt.*;

/**
 * Created by Portatile on 17/05/2017.
 */
public abstract class AbstractClient {

    //region Proprieta
    private final String indirizzoIp;
    private final int porta;
    //endregion

    /**
     * Costruttore
     */
    public AbstractClient(String indirizzoIp, int porta){
        this.indirizzoIp = indirizzoIp;
        this.porta = porta;
    }

    //region Getter
    public String getIndirizzoIp() {
        return indirizzoIp;
    }

    public int getPorta() {
        return porta;
    }
    //endregion

    public abstract void ConnessioneServer();

    public abstract void InizializzaSocketProtocol();

    public abstract void Login(String nome, Color colore) throws Exception;

    public abstract void IniziaPartita();
}
