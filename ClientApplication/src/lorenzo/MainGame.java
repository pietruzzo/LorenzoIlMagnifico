package lorenzo;

import Domain.ColoreDado;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;
import Exceptions.NetworkException;
import graphic.Cli.ControllerCli;
import graphic.Ui;
import network.AbstractClient;
import rmi.RMIClient;
import socket.SocketClient;

import java.io.IOException;
import java.util.HashMap;

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
     * @param applicazione
     * @param tipoInterfaccia
     */
    public void AvviaUI(Applicazione applicazione, TipoInterfaccia tipoInterfaccia) {
        if (tipoInterfaccia == TipoInterfaccia.CLI) {
            userInterface = new ControllerCli(this);
            applicazione.stopGUI();
        } else {
            userInterface = applicazione.startGame();
        }
    }


    //region Metodi di comunicazione verso il server
    /**
     * Effettua il login del GiocatoreGraphic
     */
    public void Login(String nome) throws Exception {
        client.Login(nome);
    }

    /**
     * Verifica se è stato raggiunto il numero massimo di
     */
    public void VerificaInizioAutomatico() throws IOException {
        client.VerificaInizioAutomatico();
    }

    /**
     * Comincia la partita, sarà il turno del primo GiocatoreGraphic loggato
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

    //endregion


    //region Metodi per gli eventi ricevuti dal server

    /**
     * Metodo chiamato quando viene confermato l'inizio di una nuova partita
     */
    public void PartitaIniziata(Tabellone tabellone) {
        System.out.println(String.format("Partita è iniziata con %d giocatori", tabellone.getGiocatori().size()));
        userInterface.inizializzaPartita(tabellone);
    }

    /**
     * Metodo chiamato quando inizia un nuovo turno
     */
    public void IniziaTurno(int[] ordineGiocatori, int[] esitoDadi, HashMap<Integer, String> mappaCarte) {
        System.out.println("Turno iniziato");
        userInterface.iniziaTurno(ordineGiocatori, esitoDadi, mappaCarte);
    }

    /**
     * Metodo chiamato quando inizia una nuova mossa
     */
    public void IniziaMossa(int idGiocatore)
    {
        System.out.println(String.format("Tocca al giocatore con id %d", idGiocatore));
        userInterface.iniziaMossa(idGiocatore);
    }

    /**
     * Metodo chiamato quando vengono scomunicati dei giocatori
     */
    public void ComunicaScomunica(int[] idGiocatoriScomunicati, int periodo)
    {
        System.out.println(String.format("Sono stati scomunicati %d giocatori", idGiocatoriScomunicati.length));
        userInterface.aggiungiScomunica(idGiocatoriScomunicati, periodo);
    }


    /**
     * Metodo chiamato quando l'utente deve scegliere se sostenere la chiesa o meno
     */
    public void SceltaSostegnoChiesa()
    {
        System.out.println("Hai abbastanza punti fede per sostenere la chiesa, la vuoi sostenere?");
        userInterface.sceltaSostegnoChiesa();
    }

    /**
     * Metodo chiamato per aggiornare le proprietà di un giocatore
     * @param update proprietà del giocatore da aggiornare
     */
    public void AggiornaGiocatore(UpdateGiocatoreDTO update)
    {
        System.out.println(String.format("Aggiornamento del giocatore %d", update.getIdGiocatore()));
        //In base ai parametri ricevuti aggiorna solo le risorse o anche la posizione di un familiare
        if(update.getColoreDado() == null || update.getIdSpazioAzione() == null)
            userInterface.aggiornaRisorse(update.getIdGiocatore(), update.getRisorse());
        else
            userInterface.aggiornaGiocatore(update.getIdGiocatore(), update.getRisorse(), update.getColoreDado(), update.getIdSpazioAzione());
    }
    //endregion


    /**
     * Mostra l'errore all'utente
     * @param message messaggio d'errore
     */
    public void MostraEccezione(String message) {
        userInterface.stampaMessaggio(message);
    }

    public enum TipoConnessione {RMI, Socket};

    public enum TipoInterfaccia {CLI, GUI};
}
