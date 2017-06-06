package server.rmi;

import Domain.DTO.AzioneBonusDTO;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Risorsa;
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
    void PiazzaFamiliare(short idGiocatore, PiazzaFamiliareDTO piazzaFamiliareDTO) throws IOException;
    void AzioneBonusEffettuata(short idGiocatore, AzioneBonusDTO azioneBonusDTO) throws IOException;
    void RiscuotiPrivilegiDelConsiglio(short idGiocatore, Risorsa risorsa) throws IOException;
}
