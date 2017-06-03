package rmi;

import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public interface IRMIClient extends Remote {

    void PartitaIniziata(Tabellone tabellone) throws RemoteException;
    void IniziaTurno(int[] ordineGiocatori, int[] esitoDadi, HashMap<Integer, String> mappaCarte) throws RemoteException;
    void IniziaMossa(int idGiocatore) throws RemoteException;
    void ComunicaScomunica(int[] idGiocatoriScomunicati, int periodo) throws RemoteException;
    void SceltaSostegnoChiesa() throws RemoteException;
    void AggiornaGiocatore(UpdateGiocatoreDTO update) throws RemoteException;
}
