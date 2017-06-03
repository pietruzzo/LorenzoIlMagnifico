package network;

import Domain.ColoreDado;
import Domain.DTO.PiazzaFamiliareDTO;
import Exceptions.NetworkException;
import lorenzo.MainGame;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Portatile on 17/05/2017.
 */
public abstract class AbstractClient {

    //region Proprieta
    private final String indirizzoIp;
    private final int porta;
    private final MainGame mainGame;
    //endregion

    /**
     * Costruttore
     */
    public AbstractClient(MainGame mainGame, String indirizzoIp, int porta){
        this.mainGame = mainGame;
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

    public MainGame getMainGame() {
        return mainGame;
    }

    //endregion

    public abstract void ConnessioneServer();

    public abstract void InizializzaSocketProtocol();

    public abstract void Login(String nome) throws Exception;

    public abstract void VerificaInizioAutomatico() throws IOException;

    public abstract void IniziaPartita() throws NetworkException;

    public abstract void RispostaSostegnoChiesa(Boolean risposta) throws NetworkException;

    public abstract void PiazzaFamiliare(PiazzaFamiliareDTO piazzaFamiliareDTO) throws NetworkException;
}
