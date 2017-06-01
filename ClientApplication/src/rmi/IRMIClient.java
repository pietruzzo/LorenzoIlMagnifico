package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public interface IRMIClient extends Remote {

    void PartitaIniziata() throws RemoteException;
    void IniziaTurno(int[] esitoDadi, HashMap<Integer, String> mappaCarte) throws RemoteException;
}
