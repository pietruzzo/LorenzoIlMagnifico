package rmi;

import Domain.DTO.AzioneBonusDTO;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;
import Domain.TipoAzione;
import Exceptions.NetworkException;
import lorenzo.MainGame;
import network.AbstractClient;
import server.rmi.IRMIServer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public class RMIClient extends AbstractClient implements IRMIClient {

    //region Proprieta
    private IRMIServer server;
    private short idGiocatore;
    //endregion

    /**
     * Costruttore del Client rmi
     */
    public RMIClient(MainGame mainGame, String indirizzoIp, int porta)
    {
        super(mainGame, indirizzoIp, porta);
    }

    /**
     * Stabilisce il collegamento con il registro rmi
     */
    @Override
    public void ConnessioneServer() {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(getIndirizzoIp(), getPorta());
            server = (IRMIServer) registry.lookup("IRMIServer");
            UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inizializza rmi
     */
    @Override
    public void InizializzaSocketProtocol() {
    }

    //region Chiamate al server
    /**
     * Effettua il login di un giocatore (aggiungendolo al tabellone)
     */
    @Override
    public void Login(String nome) throws IOException {
        idGiocatore = server.Login(nome, this);
    }

    /**
     * La partita inizia automaticamente se è stato raggiunto il numero massimo di giocatori
     */
    @Override
    public void VerificaInizioAutomatico() throws IOException {
        server.VerificaInizioAutomatico(this.idGiocatore);
    }

    /**
     * Comunica al server di iniziare la partita
     */
    @Override
    public void IniziaPartita() throws NetworkException {
        try {
            server.IniziaPartita(this.idGiocatore);
        } catch (IOException e) {
            this.HandleException(e);
        }
    }

    /**
     * Comunica al server la risposta al sostegno della chiesa
     * @param risposta true se sostiene, con false il giocatore viene scomunicato
     */
    @Override
    public void RispostaSostegnoChiesa(Boolean risposta) throws NetworkException {
        try {
            server.RispostaSostegnoChiesa(this.idGiocatore, risposta);
        } catch (IOException e) {
            this.HandleException(e);
        }
    }


    /**
     * Comunica al server l'intenzione di piazzare un familiare nello spazio azione associato
     * @param piazzaFamiliareDTO parametri relativi al piazzamento del familiare
     */
    @Override
    public void PiazzaFamiliare(PiazzaFamiliareDTO piazzaFamiliareDTO) throws NetworkException {
        try {
            server.PiazzaFamiliare(this.idGiocatore, piazzaFamiliareDTO);
        } catch (IOException e) {
            this.HandleException(e);
        }
    }

    /**
     * Comunica al server l'intenzione di effettuare un'azione bonus
     * @param azioneBonusDTO parametri relativi all'azione bonus
     */
    @Override
    public void AzioneBonusEffettuata(AzioneBonusDTO azioneBonusDTO) throws NetworkException {
        try {
            server.AzioneBonusEffettuata(this.idGiocatore, azioneBonusDTO);
        } catch (IOException e) {
            this.HandleException(e);
        }
    }

    /**
     * Manda al server la scelta del privilegio del consiglio
     * @param risorsa risorse da aggiungere al giocatore
     */
    @Override
    public void RiscuotiPrivilegiDelConsiglio(Risorsa risorsa) throws NetworkException {
        try {
            server.RiscuotiPrivilegiDelConsiglio(this.idGiocatore, risorsa);
        } catch (IOException e) {
            this.HandleException(e);
        }
    }

    /**
     * Manda al server la scelta dell'effetto di default da attivare per le carte con scambia risorse
     * @param nomeCarta nome della carta alla quale impostare la scelta
     * @param sceltaEffetto indidce della scelta effettuata
     */
    @Override
    public void SettaSceltaEffetti(String nomeCarta, Integer sceltaEffetto) throws NetworkException
    {
        try {
            server.SettaSceltaEffetti(this.idGiocatore, nomeCarta, sceltaEffetto);
        } catch (IOException e) {
            this.HandleException(e);
        }
    }

    /**
     * Notifica il server della chiusura di un client
     */
    @Override
    public void NotificaChiusuraClient() throws NetworkException
    {
        try {
            server.NotificaChiusuraClient(this.idGiocatore);
        } catch (IOException e) {
            this.HandleException(e);
        }
    }


    //endregion

    //region Chiamate dal server
    /**
     * Gestisce gli eventi relativi all'inizio di una partita
     */
    @Override
    public void PartitaIniziata(Tabellone tabellone) {
        this.getMainGame().PartitaIniziata(tabellone);
    }

    /**
     * Gestisce l'evento di inizio turno
     * @param esitoDadi valore dei dadi da mantenere per tutto il turno
     * @param mappaCarte posizione delle carte sulle torri
     * @throws RemoteException
     */
    @Override
    public void IniziaTurno(int[] ordineGiocatori, int[] esitoDadi, HashMap<Integer, String> mappaCarte) {
        this.getMainGame().IniziaTurno(ordineGiocatori, esitoDadi, mappaCarte);
    }

    /**
     * Comunica l'inzio di una nuova mossa
     * @param idGiocatore id del giocatore che deve effettuare la mossa
     */
    @Override
    public void IniziaMossa(int idGiocatore) {
        this.getMainGame().IniziaMossa(idGiocatore);
    }

    /**
     * Comunica la scomunica di giocatori
     * @param idGiocatoriScomunicati array degli id dei giocatori scomunicati
     * @param periodo periodo nel quale avviene la scomunica
     */
    @Override
    public void ComunicaScomunica(int[] idGiocatoriScomunicati, int periodo) {
        this.getMainGame().ComunicaScomunica(idGiocatoriScomunicati, periodo);
    }

    /**
     * Comunica che deve scegliere se sostenere o meno la chiesa
     */
    @Override
    public void SceltaSostegnoChiesa()
    {
        this.getMainGame().SceltaSostegnoChiesa();
    }


    /**
     * Gestisce l'aggiornamento di un giocatore lato client
     * @param update nuove caratteristiche del giocatore
     */
    @Override
    public void AggiornaGiocatore(UpdateGiocatoreDTO update) {
        this.getMainGame().AggiornaGiocatore(update);
    }

    /**
     * Gestisce l'evento relativo alla scelta dei privilegi del consiglio
     * @param numPergamene numero di pergamene da scegliere
     */
    @Override
    public void SceltaPrivilegioConsiglio(int numPergamene) {
        this.getMainGame().SceltaPrivilegioConsiglio(numPergamene);
    }

    /**
     * Gestisce l'evento di effettuazione di un'azione bonus
     * @param tipoAzioneBonus tipo di azione da svolgere
     * @param valoreAzione valore dell'azione da svolgere
     */
    @Override
    public void EffettuaAzioneBonus(TipoAzione tipoAzioneBonus, int valoreAzione, Risorsa bonusRisorse) {
        this.getMainGame().EffettuaAzioneBonus(tipoAzioneBonus, valoreAzione, bonusRisorse);
    }

    /**
     * Gestisce la fine della partita lato client
     * @param mappaRisultati mappa ordinata avente l'id del giocatore come chiave e i suoi punti vittoria come valore
     */
    @Override
    public void FinePartita(LinkedHashMap<Short, Integer> mappaRisultati)
    {
        this.getMainGame().FinePartita(mappaRisultati);
    }

    /**
     * Comunica ai client online la disconnessione di un giocatore
     * @param idGiocatoreDisconnesso
     */
    @Override
    public void ComunicaDisconnessione(int idGiocatoreDisconnesso)
    {
        this.getMainGame().ComunicaDisconnessione(idGiocatoreDisconnesso);
    }
    //endregion

    /**
     * Gestisce le eccezioni lanciate lato server
     */
    private void HandleException(IOException eccezione) throws NetworkException {
        if (eccezione instanceof RemoteException)
            throw new NetworkException(eccezione);
        else
            this.getMainGame().MostraEccezione(eccezione.getMessage());
    }
}
