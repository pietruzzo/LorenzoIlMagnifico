package server.socket;

import Domain.DTO.AzioneBonusDTO;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;
import Domain.TipoAzione;
import Exceptions.DomainException;
import Exceptions.NetworkException;
import server.GiocatoreRemoto;
import server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public class GiocatoreSocket extends GiocatoreRemoto implements Runnable {

    //region Proprieta
    private final transient Socket socket;
    private final transient Server server;
    private final transient ObjectInputStream inputStream;
    private final transient ObjectOutputStream outputStream;
    private final transient SocketServerProtocol protocol;
    //endregion

    /**
     * Costruttore
     */
    protected GiocatoreSocket(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(this.socket.getInputStream());
        protocol = new SocketServerProtocol(inputStream, outputStream, this);
    }

    /**
     * Metodo eseguito dal thread
     */
    @Override
    public void run() {
        //Il server resta sempre in ascolto
        try{
                while(true)
                {
                    Object obcjet = inputStream.readObject();
                    protocol.HandleRequest(obcjet);
                }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //region Messaggi dal client al server
    /**
     * Effettua il login del giocatore
     */
    public void Login(String nome) throws DomainException {
        this.server.AggiungiGiocatore(nome,this);
    }

    /**
     * Se ci sono 4 giocatori la partita inizia in automatico
     */
    public void VerificaInizioPartita() throws NetworkException{
        try {
            if(this.getPartita() != null)
                this.getPartita().VerificaInizioAutomatico();
        } catch (DomainException e) {
            protocol.ComunicaEccezione(e.getMessage());
        }
    }

    /**
     *  Inizia la partita
     */
    public void IniziaPartita() throws NetworkException{
        try {
            this.getPartita().IniziaPartita();
        } catch (DomainException e) {
            protocol.ComunicaEccezione(e.getMessage());
        }
    }

    /**
     *  Gestisce la risposta del client alla domanda sul sostegno della chiesa
     *  @param risposta true se sostiene, con false il giocatore viene scomunicato
     */
    public void RispostaSostegnoChiesa(Boolean risposta){
        this.getPartita().RispostaSostegnoChiesa(this, risposta);
    }

    /**
     * Gestisce l'evento relativo al tentato piazzamento di un familiare da parte di un client
     * @param piazzaFamiliareDTO parametri relativi al piazzamento del familiare
     */
    public void PiazzaFamiliare (PiazzaFamiliareDTO piazzaFamiliareDTO) throws NetworkException
    {
        try {
            this.getPartita().PiazzaFamiliare(this.getIdGiocatore(), piazzaFamiliareDTO);
        } catch (DomainException e) {
            protocol.ComunicaEccezione(e.getMessage());
        }
    }


    /**
     * Gestisce l'evento relativo alla tentata azione bonus da parte di un client
     * @param azioneBonusDTO parametri relativi all'azione bonus
     */
    public void AzioneBonusEffettuata (AzioneBonusDTO azioneBonusDTO) throws NetworkException
    {
        try {
            this.getPartita().AzioneBonusEffettuata(this.getIdGiocatore(), azioneBonusDTO);
        } catch (DomainException e) {
            protocol.ComunicaEccezione(e.getMessage());
        }
    }

    /**
     * Gestisce l'evento relativo alla scelta del privilegio del consiglio
     * @param risorsa risorse da aggiungere al giocatore
     */
    public void RiscuotiPrivilegiDelConsiglio(Risorsa risorsa)
    {
        this.getPartita().RiscuotiPrivilegiDelConsiglio(this.getIdGiocatore(), risorsa);
    }

    /**
     * Gestisce l'evento di chiusura di un client
     */
    public void NotificaChiusuraClient()
    {
        this.getPartita().NotificaChiusuraClient(this);
    }

    //endregion

    //region Messaggi dal server al client
    /**
     * Comunica al client l'inzio della partita
     */
    @Override
    public void PartitaIniziata(Tabellone tabellone) throws NetworkException
    {
        this.protocol.PartitaIniziata(tabellone);
    }

    /**
     *  Comunica al client l'inzio di un nuovo turno
     */
    @Override
    public void IniziaTurno(int[] ordineGiocatori, int[] esitoDadi, HashMap<Integer, String> mappaCarte) throws NetworkException
    {
        this.protocol.IniziaTurno(ordineGiocatori, esitoDadi, mappaCarte);
    }

    /**
     * Comunica al client l'inzio di una nuova mossa
     * @param idGiocatore id del giocatore che deve effettuare la mossa
     */
    @Override
    public void IniziaMossa(int idGiocatore) throws NetworkException
    {
        this.protocol.IniziaMossa(idGiocatore);
    }

    /**
     * Comunica ai client la scomunica di giocatori
     * @param idGiocatoriScomunicati array degli id dei giocatori scomunicati
     * @param periodo periodo nel quale avviene la scomunica
     */
    @Override
    public void ComunicaScomunica(int[] idGiocatoriScomunicati, int periodo) throws NetworkException
    {
        this.protocol.ComunicaScomunica(idGiocatoriScomunicati, periodo);
    }

    /**
     * Comunica a determinati giocatori che devono scegliere se sostenere o meno la chiesa
     */
    @Override
    public void SceltaSostegnoChiesa() throws NetworkException
    {
        this.protocol.SceltaSostegnoChiesa();
    }

    /**
     * Notifica a tutti i client l'aggiornamento di un giocatore
     * @param update nuove caratteristiche del giocatore
     */
    @Override
    public void ComunicaAggiornaGiocatore(UpdateGiocatoreDTO update) throws NetworkException {
        this.protocol.AggiornaGiocatore(update);
    }

    /**
     * Indica al client il numero di pergamene da scegliere
     * @param numPergamene numero di pergamene da scegliere
     */
    @Override
    public void SceltaPrivilegioConsiglio(int numPergamene) throws NetworkException {
        this.incrementaPrivilegiDaScegliere();
        this.protocol.SceltaPrivilegioConsiglio(numPergamene);
    }

    /**
     * Indica al client che può effettuare un'azione bonus
     * @param tipoAzioneBonus tipo di azione da svolgere
     * @param valoreAzione valore dell'azione da svolgere
     */
    @Override
    public void EffettuaAzioneBonus(TipoAzione tipoAzioneBonus, int valoreAzione, Risorsa bonusRisorse) throws NetworkException{
        this.setAzioneBonusDaEffettuare(true);
        this.protocol.EffettuaAzioneBonus(tipoAzioneBonus, valoreAzione, bonusRisorse);
    }

    /**
     * Comunica la fine della partita ai client
     * @param mappaRisultati mappa ordinata avente l'id del giocatore come chiave e i suoi punti vittoria come valore
     */
    @Override
    public void ComunicaFinePartita(LinkedHashMap<Short, Integer> mappaRisultati) throws NetworkException {
        this.protocol.ComunicaFinePartita(mappaRisultati);
    }

    /**
     * Comunica ai client online la disconnessione di un giocatore
     * @param idGiocatoreDisconnesso
     */
    @Override
    public void ComunicaDisconnessione(int idGiocatoreDisconnesso) throws NetworkException {
        this.protocol.ComunicaDisconnessione(idGiocatoreDisconnesso);
    }

    //endregion
}
