package lorenzo;

import Domain.ColoreDado;
import Domain.DTO.AzioneBonusDTO;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;
import Domain.TipoAzione;
import Exceptions.NetworkException;
import graphic.Cli.ControllerCli;
import graphic.Ui;
import network.AbstractClient;
import rmi.RMIClient;
import socket.SocketClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Portatile on 17/05/2017.
 */
public class MainGame {

    //region Proprieta
    private Applicazione applicazione;
    /**
     * Classe astratta del client
     */
    private AbstractClient client;

    /**
     * Interfaccia utente
     */
    private Ui userInterface;

    private String nomeGiocatore;
    //endregion

    protected MainGame(Applicazione applicazione) {
        this.applicazione=applicazione;
    }

    @Deprecated
    public void Start() throws InterruptedException {
        /*
        this.SetConnessioneServer();
        Random rnd = new Random();
        int rndNumber = rnd.nextInt(10);
        this.Login("michele" +rndNumber);
        */
        //this.IniziaPartita();

        //applicazione.startLogin();
    }

    /**
     * Effettua la connessione al server tramite socket o rmi
     */
    public void SetConnessioneServer(TipoConnessione tipoConnessione) {
        String indirizzoIP = "127.0.0.1";
        int portaSocket = 1337;
        int portaRMI = 1338;

        if (tipoConnessione == TipoConnessione.Socket)
            client = new SocketClient(this, indirizzoIP, portaSocket);
        else
            client = new RMIClient(this, indirizzoIP, portaRMI);

        client.ConnessioneServer();
        client.InizializzaSocketProtocol(); //Se il client è rmi il metodo non fa nulla
    }

    /**
     * Avvia l'interfaccia utente appropriata e chiudi GUI se non necessaria
     *
     * @param tipoInterfaccia
     */
    public void AvviaUI(TipoInterfaccia tipoInterfaccia) {
        if (tipoInterfaccia == TipoInterfaccia.CLI) {
            userInterface = new ControllerCli(this);
            applicazione.stopGUI();
        } else {
            userInterface = applicazione.startGame();
        }
    }


    //region Metodi di comunicazione verso il server
    /**
     * Effettua il login del giocatore
     */
    public void Login(String nome) throws Exception {
        client.Login(nome);
        nomeGiocatore=nome;
    }

    /**
     * Verifica se è stato raggiunto il numero massimo di
     */
    public void VerificaInizioAutomatico() throws IOException {
        client.VerificaInizioAutomatico();
    }

    /**
     * Comincia la partita, sarà il turno del primo giocatore loggato
     */
    public void IniziaPartita() {
        try {
            client.IniziaPartita();
        } catch (NetworkException e) {
            System.out.println(String.format("Fallita comunicazione inizio partita. %s", e.getMessage()));
        }
    }

    /**
     * Risposta dell'utente alla domanda sul sostegno della chiesa
     * @param risposta true se sostiene, con false il giocatore viene scomunicato
     */
    public void RispostaSostegnoChiesa(Boolean risposta)
    {
        try {
            client.RispostaSostegnoChiesa(risposta);
        } catch (NetworkException e) {
            System.out.println(String.format("Fallita comunicazione risposta sostegno. %s", e.getMessage()));
        }
    }

    /**
     * Manda la richiesta di piazzamento al server
     * Effettua anche la validazione, in caso di errori viene mostrato l'errore a video
     */
    public void PiazzaFamiliare(short idGiocatore, ColoreDado coloreDado, int idSpazioAzione, int servitoriAggiunti)
    {
        try {
            client.PiazzaFamiliare(new PiazzaFamiliareDTO(idGiocatore, coloreDado, idSpazioAzione, servitoriAggiunti));
        } catch (NetworkException e) {
            System.out.println(String.format("Fallita comunicazione piazzamento familiare. %s", e.getMessage()));
        }
    }

    /**
     * Manda al server la richiesta di validazione e effettuazione dell'azione bonus
     * @param idGiocatore id del giocatore che effettua l'azione
     * @param idSpazioAzione id dello spazio azione sul quale viene fatta l'azione
     * @param valoreAzione il valore dell'azione dato dall'effetto
     * @param bonusRisorse il bonus eventuale dato dall'effetto
     */
    public void AzioneBonusEffettuata(short idGiocatore, int idSpazioAzione, int valoreAzione, Risorsa bonusRisorse, int servitoriAggiunti)
    {
        try {
            client.AzioneBonusEffettuata(new AzioneBonusDTO(idGiocatore, idSpazioAzione, valoreAzione, bonusRisorse, servitoriAggiunti));
        } catch (NetworkException e) {
            System.out.println(String.format("Fallita comunicazione azione bonus. %s", e.getMessage()));
        }
    }

    /**
     * Manda al server la scelta del privilegio del consiglio
     * @param risorsa risorse da aggiungere al giocatore
     */
    public void RiscuotiPrivilegiDelConsiglio(Risorsa risorsa)
    {
        try {
            client.RiscuotiPrivilegiDelConsiglio(risorsa);
        } catch (NetworkException e) {
            System.out.println(String.format("Fallita comunicazione riscuoti privilegio. %s", e.getMessage()));
        }
    }

    /**
     * Manda al server la scelta dell'effetto di default da attivare per le carte con scambia risorse
     * @param nomeCarta nome della carta alla quale impostare la scelta
     * @param sceltaEffetto indidce della scelta effettuata
     */
    public void SettaSceltaEffetti(String nomeCarta, Integer sceltaEffetto)
    {
        try {
            client.SettaSceltaEffetti(nomeCarta, sceltaEffetto);
        } catch (NetworkException e) {
            System.out.println(String.format("Fallita comunicazione scelta effetto. %s", e.getMessage()));
        }
    }

    public void NotificaChiusuraClient()
    {
        try {
            client.NotificaChiusuraClient();
        } catch (NetworkException e) {
            System.out.println(String.format("Fallita comunicazione chiusura client. %s", e.getMessage()));
        }
    }
    //endregion


    //region Metodi per gli eventi ricevuti dal server

    /**
     * Metodo chiamato quando viene confermato l'inizio di una nuova partita
     */
    public void PartitaIniziata(Tabellone tabellone) {
        System.out.println(String.format("Partita è iniziata con %d giocatori", tabellone.getGiocatori().size()));
        try {
            userInterface.inizializzaPartita(tabellone);
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }

    /**
     * Metodo chiamato quando inizia un nuovo turno
     */
    public void IniziaTurno(int[] ordineGiocatori, int[] esitoDadi, HashMap<Integer, String> mappaCarte) {
        System.out.println("Turno iniziato");
        try {
            userInterface.iniziaTurno(ordineGiocatori, esitoDadi, mappaCarte);
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }

    /**
     * Metodo chiamato quando inizia una nuova mossa
     */
    public void IniziaMossa(int idGiocatore)
    {
        System.out.println(String.format("Tocca al giocatore con id %d", idGiocatore));
        try {
            userInterface.iniziaMossa(idGiocatore);
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }

    /**
     * Metodo chiamato quando vengono scomunicati dei giocatori
     */
    public void ComunicaScomunica(int[] idGiocatoriScomunicati, int periodo)
    {
        System.out.println(String.format("Sono stati scomunicati %d giocatori", idGiocatoriScomunicati.length));
        try {
            userInterface.aggiungiScomunica(idGiocatoriScomunicati, periodo);
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }


    /**
     * Metodo chiamato quando l'utente deve scegliere se sostenere la chiesa o meno
     */
    public void SceltaSostegnoChiesa()
    {
        System.out.println("Hai abbastanza punti fede per sostenere la chiesa, la vuoi sostenere?");
        try {
            userInterface.sceltaSostegnoChiesa();
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }

    /**
     * Metodo chiamato per aggiornare le proprietà di un giocatore
     * @param update proprietà del giocatore da aggiornare
     */
    public void AggiornaGiocatore(UpdateGiocatoreDTO update)
    {
        System.out.println(String.format("Aggiornamento del giocatore %d", update.getIdGiocatore()));
        try {
            //In base ai parametri ricevuti aggiorna solo le risorse o anche la posizione di un familiare
            if(update.getColoreDado() == null && update.getIdSpazioAzione() == null)
                userInterface.aggiornaRisorse(update.getIdGiocatore(), update.getRisorse());
            else if(update.getColoreDado() == null && update.getIdSpazioAzione() != null)
                    userInterface.aggiornaDaAzioneBonus(update.getIdGiocatore(), update.getRisorse(), update.getIdSpazioAzione());
            else
                userInterface.aggiornaGiocatore(update.getIdGiocatore(), update.getRisorse(), update.getColoreDado(), update.getIdSpazioAzione());
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }

    /**
     * Gestisce l'evento relativo alla scelta dei privilegi del consiglio
     * @param numPergamene numero di pergamene da scegliere
     */
    public void SceltaPrivilegioConsiglio(int numPergamene) {
        System.out.println(String.format("Occorre scegliere %d privilegi del consiglio", numPergamene));
        try {
            userInterface.visualizzaPrivilegioConsiglio(numPergamene);
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }

    /**
     * Gestisce l'evento di effettuazione di un'azione bonus
     * @param tipoAzioneBonus tipo di azione da svolgere
     * @param valoreAzione valore dell'azione da svolgere
     */
    public void EffettuaAzioneBonus(TipoAzione tipoAzioneBonus, int valoreAzione, Risorsa bonusRisorse) {
        System.out.println(String.format("E' possibile effettuare un'azione di valore %d ", valoreAzione));
        try {
            userInterface.effettuaAzioneBonus(tipoAzioneBonus, valoreAzione, bonusRisorse);
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }

    /**
     * Gestisce l'evento di fine partita
     * Aggiorna le posizioni delle pedine relativamente ai punti vittoria dei giocatori
     * Comunica la classifica all'utente
     */
    public void FinePartita(LinkedHashMap<Short, Integer> mappaRisultati)
    {
        System.out.println(String.format("Partita finita! Ha vinto l'id %d", mappaRisultati.keySet().iterator().next()));
        try {
            userInterface.finePartita(mappaRisultati);
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }

    /**
     * Gestisce l'evento di disconnessione di un giocatore
     */
    public void ComunicaDisconnessione(int idGiocatoreDisconnesso)
    {
        System.out.println(String.format("Il giocatore %d non è più online", idGiocatoreDisconnesso));
        try {
            userInterface.GiocatoreDisconnesso(idGiocatoreDisconnesso);
        }
        catch(Exception e)
        {
            this.MostraEccezione(e.getMessage());
        }
    }
    //endregion


    public void closeClient(){applicazione.chiudiApplicazione();}

    public String getNomeGiocatore() {
        return nomeGiocatore;
    }

    public Applicazione getApplicazione(){return applicazione;}

    /**
     * Mostra l'errore all'utente
     * @param message messaggio d'errore
     */
    public void MostraEccezione(String message) {
        System.out.println(message);
        userInterface.stampaMessaggio(message);
    }

    public enum TipoConnessione {RMI, Socket};

    public enum TipoInterfaccia {CLI, GUI};
}
