package server.rmi;

import Domain.DTO.AzioneBonusDTO;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Risorsa;
import Exceptions.DomainException;
import rmi.IRMIClient;
import server.AbstractServer;
import server.GiocatoreRemoto;
import server.Server;

import java.awt.*;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Portatile on 19/05/2017.
 */
public class RMIServer extends AbstractServer implements IRMIServer {

    /**
     * Costruttore
     */
    public RMIServer(Server server)
    {
        super(server);
    }

    /**
     * Avvia il server rmi
     */
    @Override
    public void StartServer(int porta) throws Exception {
        Registry registro = creaCaricaRegistro(porta);
        try {
            registro.bind("IRMIServer", this);
            UnicastRemoteObject.exportObject(this, porta);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Se non è ancora stato creato, crea il registro.
     * Altrimenti prova a ritornare il registro creato in precedenza.
     */
    private Registry creaCaricaRegistro(int porta) throws Exception {
        try {
            return LocateRegistry.createRegistry(porta);
        } catch (RemoteException e) {
            //Il registro è già stato creato
        }
        try {
            return LocateRegistry.getRegistry(porta);
        } catch (RemoteException e) {
            throw new Exception("Impossibile avviare il registro rmi");
        }
    }


    /**
     * Ottiene il giocatore dato l'id
     */
    private GiocatoreRemoto GetGiocatoreById(short idGiocatore)
    {
        return getServer().GetGiocatoreById(idGiocatore);
    }

    /**
     * Effettua il login del giocatore
     * Salva anche il riferimento per chiamare i metodi lato client attraverso rmi
     */
    @Override
    public short Login(String nome, IRMIClient rmiClient) throws DomainException {
        return getServer().AggiungiGiocatore(nome, new GiocatoreRMI(rmiClient));
    }

    /**
     * Se è stato raggiunto il limite massimo di giocatori la partita inizia automaticamente
     */
    @Override
    public void VerificaInizioAutomatico(short idGiocatore) throws DomainException {
        GetGiocatoreById(idGiocatore).getPartita().VerificaInizioAutomatico();
    }

    /**
     * Inizia una nuova partita
     */
    @Override
    public void IniziaPartita(short idGiocatore) throws DomainException {
        GetGiocatoreById(idGiocatore).getPartita().IniziaPartita();
    }

    /**
     * Gestisce la risposta del client alla domanda sul sostegno della chiesa
     * @param risposta true se sostiene, con false il giocatore viene scomunicato
     */
    @Override
    public void RispostaSostegnoChiesa(short idGiocatore, Boolean risposta) {
        GiocatoreRemoto giocatoreRemoto = GetGiocatoreById(idGiocatore);
        giocatoreRemoto.getPartita().RispostaSostegnoChiesa(giocatoreRemoto, risposta);
    }

    /**
     * Evento scatenato dal client per comunicare l'intenzione di piazzare un familiare nello spazio azione specificato
     * @param idGiocatore id del giocatore che ha effettuato la chiamata
     * @param piazzaFamiliareDTO parametri relativi al piazzamento del familiare
     */
    @Override
    public void PiazzaFamiliare(short idGiocatore, PiazzaFamiliareDTO piazzaFamiliareDTO) throws IOException {
        GetGiocatoreById(idGiocatore).getPartita().PiazzaFamiliare(idGiocatore, piazzaFamiliareDTO);
    }

    /**
     * Evento scatenato dal client per comunicare l'intenzione di effettuare un'azione bonus
     * @param idGiocatore id del giocatore che ha effettuato la chiamata
     * @param azioneBonusDTO parametri relativi all'azione bonus
     */
    @Override
    public void AzioneBonusEffettuata(short idGiocatore, AzioneBonusDTO azioneBonusDTO) throws IOException{
        GetGiocatoreById(idGiocatore).getPartita().AzioneBonusEffettuata(idGiocatore, azioneBonusDTO);
    }

    /**
     * Evento scatenato dal client quando l'utente salta l'azione bonus
     * @param idGiocatore id del giocatore che ha effettuato la chiamata
     */
    @Override
    public void AzioneBonusSaltata(short idGiocatore) throws IOException{
        GiocatoreRemoto giocatoreRemoto = GetGiocatoreById(idGiocatore);
        giocatoreRemoto.setAzioneBonusDaEffettuare(false);
        giocatoreRemoto.getPartita().AzioneBonusSaltata();
    }

    /**
     * Gestisce l'evento di riscossione del privilegio del consiglio
     * @param risorsa risorse da aggiungere al giocatore
     */
    @Override
    public void RiscuotiPrivilegiDelConsiglio(short idGiocatore, Risorsa risorsa) throws IOException {
        GetGiocatoreById(idGiocatore).getPartita().RiscuotiPrivilegiDelConsiglio(idGiocatore, risorsa);
    }

    /**
     * Gestisce l'evento di scelta dell'effetto di default da attivare per le carte con scambia risorse
     * @param nomeCarta nome della carta alla quale impostare la scelta
     * @param sceltaEffetto indidce della scelta effettuata
     */
    @Override
    public void SettaSceltaEffetti(short idGiocatore, String nomeCarta, Integer sceltaEffetto) throws IOException
    {
        GiocatoreRemoto giocatoreRemoto = GetGiocatoreById(idGiocatore);
        giocatoreRemoto.getPartita().getTabellone().SettaSceltaEffetti(giocatoreRemoto, nomeCarta, sceltaEffetto);
    }

    /**
     * Gestisce l'evento di chiusura di un client
     */
    @Override
    public void NotificaChiusuraClient(short idGiocatore) throws IOException
    {
        GetGiocatoreById(idGiocatore).NotificaChiusuraClient();
    }
}
