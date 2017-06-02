package lorenzo;

import Domain.Tabellone;
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

    public void Start() throws InterruptedException {
        /*
        this.SetConnessioneServer();
        Random rnd = new Random();
        int rndNumber = rnd.nextInt(10);
        this.Login("michele" +rndNumber);
        */
        //this.IniziaPartita();

        applicazione.startLogin();
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
     * Effettua il login del giocatore
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
     * Comincia la partita, sarà il turno del primo giocatore loggato
     */
    public void IniziaPartita() {
        client.IniziaPartita();
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
    public void IniziaTurno(int[] esitoDadi, HashMap<Integer, String> mappaCarte) {
        System.out.println("Turno iniziato");
        userInterface.iniziaTurno(esitoDadi, mappaCarte);
    }

    /**
     * Metodo chiamato quando inizia una nuova mossa
     */
    public void IniziaMossa(int idGiocatore)
    {
        System.out.println(String.format("Tocca al giocatore con id %d", idGiocatore));
        userInterface.iniziaMossa(idGiocatore);
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
