package server.rmi;

import rmi.IRMIClient;

import java.io.IOException;
import java.rmi.Remote;

/**
 * Created by Portatile on 19/05/2017.
 */
public interface IRMIServer extends Remote {
    short Login(String nome, IRMIClient rmiClient) throws IOException;
    void VerificaInizioAutomatico(short idGiocatore) throws IOException;
    void IniziaPartita(short idGiocatore) throws IOException;
    void RispostaSostegnoChiesa(short idGiocatore, Boolean risposta) throws IOException;
}
