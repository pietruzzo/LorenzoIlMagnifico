package lorenzo;

import graphic.Cli.ControllerCli;
import graphic.Ui;
import network.AbstractClient;
import rmi.RMIClient;
import socket.SocketClient;

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
     * Effettua il login del giocatore
     */
    public void Login(String nome) throws Exception {
        client.Login(nome);

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


    /**
     * Comincia la partita, sarà il turno del primo giocatore loggato
     */
    public void IniziaPartita() {
        client.IniziaPartita();
    }

    /**
     * Metodo chiamato quando viene confermato l'inizio di una nuova partita
     */
    public void PartitaIniziata() {
        //TODO: disabilita il bottone per iniziare la partita
    }

    /**
     * Mostra l'errore all'utente
     *
     * @param message messaggio d'errore
     */
    public void MostraEccezione(String message) {
        //TODO: chiamare l'interfaccia per mostrare il messaggio di errore (piccola form con l'icona di errore e il bottone OK?)
    }

    public enum TipoConnessione {RMI, Socket}

    ;

    public enum TipoInterfaccia {CLI, GUI}

    ;
}
