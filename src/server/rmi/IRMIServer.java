package server.rmi;

import rmi.IRMIClient;

import java.awt.*;
import java.rmi.Remote;

/**
 * Created by Portatile on 19/05/2017.
 */
public interface IRMIServer extends Remote {
    short Login(String nome, IRMIClient rmiClient) throws Exception;
    void IniziaPartita(short idGiocatore) throws Exception;
}
