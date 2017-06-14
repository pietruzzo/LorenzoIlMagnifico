package server.rmi;

import Domain.Risorsa;
import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;
import Domain.TipoAzione;
import Exceptions.NetworkException;
import rmi.IRMIClient;
import server.GiocatoreRemoto;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    @Override
    public void PartitaIniziata(Tabellone tabellone) throws NetworkException {
        try {
            this.clientRMI.PartitaIniziata(tabellone);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Comunica ai giocatori l'inizio di un nuovo turno di gioco
     * @param ordineGiocatori lista di idGiocatore ordinata per il nuovo turno
     * @param esitoDadi valore dei dadi per il turno
     * @param mappaCarte posizione delle carte negli spazi azione della torre
     */
    @Override
    public void IniziaTurno(int[] ordineGiocatori, int[] esitoDadi, HashMap<Integer, String> mappaCarte) throws NetworkException {
        try {
            this.clientRMI.IniziaTurno(ordineGiocatori, esitoDadi, mappaCarte);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Comunica l'inzio di una nuova mossa
     * @param idGiocatore id del giocatore che deve effettuare la mossa
     */
    @Override
    public void IniziaMossa(int idGiocatore) throws NetworkException {
        try {
            this.clientRMI.IniziaMossa(idGiocatore);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Comunica ai client la scomunica di giocatori
     * @param idGiocatoriScomunicati array degli id dei giocatori scomunicati
     * @param periodo periodo nel quale avviene la scomunica
     */
    @Override
    public void ComunicaScomunica(int[] idGiocatoriScomunicati, int periodo) throws NetworkException {
        try {
            this.clientRMI.ComunicaScomunica(idGiocatoriScomunicati, periodo);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Comunica al client che deve scegliere se sostenere o meno la chiesa
     */
    @Override
    public void SceltaSostegnoChiesa() throws NetworkException
    {
        try {
            this.clientRMI.SceltaSostegnoChiesa();
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }


    /**
     * Notifica a tutti i client l'aggiornamento di un giocatore
     * @param update nuove caratteristiche del giocatore
     */
    @Override
    public void ComunicaAggiornaGiocatore(UpdateGiocatoreDTO update) throws NetworkException {

        try {
            this.clientRMI.AggiornaGiocatore(update);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Indica al client il numero di pergamene da scegliere
     * @param numPergamene numero di pergamene da scegliere
     */
    @Override
    public void SceltaPrivilegioConsiglio(int numPergamene) throws NetworkException {
        try {
            this.incrementaPrivilegiDaScegliere();
            this.clientRMI.SceltaPrivilegioConsiglio(numPergamene);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Indica al client che può effettuare un'azione bonus
     * @param tipoAzioneBonus tipo di azione da svolgere
     * @param valoreAzione valore dell'azione da svolgere
     */
    @Override
    public void EffettuaAzioneBonus(TipoAzione tipoAzioneBonus, int valoreAzione, Risorsa bonusRisorse) throws NetworkException{
        try {
            this.setAzioneBonusDaEffettuare(true);
            this.clientRMI.EffettuaAzioneBonus(tipoAzioneBonus, valoreAzione, bonusRisorse);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Comunica la fine della partita ai client
     * @param mappaRisultati mappa ordinata avente l'id del giocatore come chiave e i suoi punti vittoria come valore
     * @throws NetworkException
     */
    @Override
    public void ComunicaFinePartita(LinkedHashMap<Short, Integer> mappaRisultati) throws NetworkException{
        try {
            this.clientRMI.FinePartita(mappaRisultati);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Comunica ai client online la disconnessione di un giocatore
     * @param idGiocatoreDisconnesso
     */
    @Override
    public void ComunicaDisconnessione(int idGiocatoreDisconnesso) throws NetworkException {
        try {
            this.clientRMI.ComunicaDisconnessione(idGiocatoreDisconnesso);
        } catch (RemoteException e) {
            throw new NetworkException(e);
        }
    }

    /**
     * Comunica al server che il giocatore si sta disconnettendo
     */
    public void NotificaChiusuraClient()  {
        this.getPartita().NotificaChiusuraClient(this);
    }

}
