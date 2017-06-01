package server.rmi;

import Domain.Tabellone;
import Exceptions.NetworkException;
import rmi.IRMIClient;
import server.GiocatoreRemoto;

import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by Portatile on 19/05/2017.
 */
public class GiocatoreRMI extends GiocatoreRemoto {

    //Riferimento per chiamare i metodi del client rmi
    private transient IRMIClient clientRMI;

    /**
     * Costruttore
     */
    public GiocatoreRMI(IRMIClient clientRmi)
    {
        this.clientRMI = clientRmi;
    }

    /**
     * Comunica al client l'inizio della partita
     */
    public void PartitaIniziata(Tabellone tabellone) throws NetworkException {
        try {
            this.clientRMI.PartitaIniziata(tabellone);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    @Override
    public void IniziaTurno(int[] esitoDadi, HashMap<Integer, String> mappaCarte) throws NetworkException {
        try {
            this.clientRMI.IniziaTurno(esitoDadi, mappaCarte);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

}
