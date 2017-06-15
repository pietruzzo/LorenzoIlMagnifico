package graphic.Cli;

import Domain.*;
import graphic.Ui;
import lorenzo.MainGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by pietro on 31/05/17.
 */
public class ControllerCli implements Ui {

    protected MainGame mainGame;
    private Printer printer;
    private GestoreComandi gestoreComandi;
    private Map<Integer, String> carteTabellone;
    private Map<Integer, String> giocatori;
    private int idGiocatoreCorrente;
    private Risorsa risorse;
    private ArrayList<String> planciaCarte;


    public ControllerCli(MainGame mainGame) {
        this.mainGame = mainGame;
        this.printer = new Printer();
        this.planciaCarte = new ArrayList<>();
        this.giocatori = new HashMap<>();
        this.gestoreComandi = new GestoreComandi(this);
        this.printer.stampa("In attesa di altri giocatori..");
        this.printer.stampa("per iniziare digitare il comando 'iniziaPartita'.");
        this.printer.stampa("Dall'inizio della partita avrai sempre a disposizione i comandi:\nvediRisorse\nvediCarte\n");

        new ReaderHandler().start();
    }


    @Override
    public void inizializzaPartita(Tabellone tabellone) {
        this.printer.stampa("La partita è cominciata!");
        this.printer.stampa("Partecipano i giocatori:");

        //Mostra i giocatori partecipanti
        for (Giocatore giocatore : tabellone.getGiocatori()) {
            this.giocatori.put((int)giocatore.getIdGiocatore(), giocatore.getNome());
            this.printer.stampa("%d - %s", giocatore.getIdGiocatore(), giocatore.getNome());

            if(giocatore.getNome().equals(mainGame.getNomeGiocatore())) {
                idGiocatoreCorrente = giocatore.getIdGiocatore();
                risorse = giocatore.getRisorse().clone();
            }
        }
    }

    @Override
    public void iniziaTurno(int[] ordineGiocatori, int[] dadi, Map<Integer, String> carte) {
        this.printer.stampa("E' iniziato un nuovo turno!");
        this.printer.stampa("In questo turno i dadi avranno i seguenti valori:");
        this.printer.stampa("Nero:    %d", dadi[0]);
        this.printer.stampa("Bianco:  %d", dadi[1]);
        this.printer.stampa("Arancio: %d\n", dadi[2]);

        this.carteTabellone = carte;
    }

    /**
     * Inizia una nuova mossa
     * @param idGiocatore giocatore che deve fare la mossa
     */
    @Override
    public void iniziaMossa(int idGiocatore) {
        //Stampa il tabellone per poter fare la mossa
        this.printer.stampaTabellone(carteTabellone);

        //Se devo fare la mossa abilito i comandi
        if(this.idGiocatoreCorrente == idGiocatore) {
            gestoreComandi.setComandiPiazzamentoFamiliare();
            this.printer.stampa("Tocca a te piazzare un familiare!");
            this.printer.stampa("per effettuare l'azione digitare 'piazzaFamiliare colore idSpazioAzione servitoriAggiunti'");
            this.printer.stampa("colori: w=bianco o=orance b=nero n=neutro");
        }
        else {
            gestoreComandi.setComandiDefault();
            this.printer.stampa("Tocca a %s piazzare un familiare!", giocatori.get(idGiocatore));
        }
    }

    /**
     * Notifica la scomunica di alcuni giocatori
     * @param idGiocatoriScomunicati lista dei giocatori scomunicati
     * @param periodo periodo nel quale è avvenuta la scomunica
     */
    @Override
    public void aggiungiScomunica(int[] idGiocatoriScomunicati, int periodo) {
        for (int i = 0; i < idGiocatoriScomunicati.length; i++) {
            if(idGiocatoreCorrente != idGiocatoriScomunicati[i])
                this.printer.stampa("Sei stato scomunicato!");
            else
                this.printer.stampa("Il giocatore %s è stato scomunicato!", giocatori.get(idGiocatoriScomunicati[i]));
        }
    }

    /**
     * Metodo chiamato quando l'utente deve scegliere se sostenere la chiesa o meno
     */
    @Override
    public void sceltaSostegnoChiesa() {
        this.gestoreComandi.setComandiSostegnoChiesa();
        this.printer.stampa("Hai abbastanza punti fede per sostenere la chiesa, la vuoi sostenere?");
        this.printer.stampa("Per rispondere digitare 'sostegnoChiesa si/no'");
    }

    /**
     * Gestisce l'evento di effettuazione di un'azione bonus
     */
    @Override
    public void effettuaAzioneBonus(TipoAzione tipoAzione, int valoreAzione, Risorsa bonusRisorse) {
        this.gestoreComandi.setComandiAzioneBonus();
        this.printer.stampaTabellone(this.carteTabellone);
        this.printer.stampa("Per effetto di una carta, hai la possibilità di effettuare un'azione bonus!");
        this.printer.stampa("L'azione avrà un valore base di %d.");
        this.printer.stampa("(per effettuare l'azione digitare 'azioneBonus idSpazioAzione servitoriAggiunti')");
    }

    /**
     * Mostra i privilegi da scegliere
     * @param numeroPergamene numero di pergamene da riscattare
     */
    @Override
    public void visualizzaPrivilegioConsiglio(int numeroPergamene) {
        this.gestoreComandi.setComandiRispostaPrivilegio();
        this.printer.stampa("Hai ottenuto %d privilegi del consiglio!", numeroPergamene);
        this.printer.stampaPrivilegi();
        this.printer.stampa("(per scegliere il privilegio digitare 'sceltaPrivilegio' seguito dal numero corrispondente)");
    }

    /**
     * Aggiorna dopo un azione bonus
     */
    @Override
    public void aggiornaDaAzioneBonus(int idGiocatore, Risorsa risorsa, int idSpazioAzione) {
        if(idGiocatoreCorrente == idGiocatore) {
            this.printer.stampa("Azione bonus completata con successo!");
            this.risorse = risorsa.clone();

            if(idSpazioAzione <= 16 && !carteTabellone.get(idSpazioAzione).isEmpty())
            {
                this.planciaCarte.add(carteTabellone.get(idSpazioAzione));
            }
        }

        if(idSpazioAzione <= 16)
            this.carteTabellone.replace(idSpazioAzione, "Presa con azione bonus");
    }

    /**
     * Aggiorna i parametri del giocatore
     */
    @Override
    public void aggiornaGiocatore(int idGiocatore, Risorsa risorsa, ColoreDado coloreDado, int idSpazioAzione) {
        if(idGiocatoreCorrente == idGiocatore) {
            this.risorse = risorsa.clone();
            this.printer.stampa("Aggiornamento del giocatore avvenuto con successo!");

            if(idSpazioAzione <= 16 && !carteTabellone.get(idSpazioAzione).isEmpty())
            {
                this.planciaCarte.add(carteTabellone.get(idSpazioAzione));
            }
        }

        if(idSpazioAzione <= 16) {
            this.carteTabellone.replace(idSpazioAzione, String.format("Presa da %s", giocatori.get(idGiocatore)));
        }

    }

    /**
     * Stampa la classifica finale
     * @param mappaRisultati mappa ordinata (il primo elemento è il primo della classifica)
     *                       key: id del giocatore
     */
    @Override
    public void finePartita(LinkedHashMap<Short, Integer> mappaRisultati) {
        this.printer.stampa("La partita è terminata!");
        this.printer.stampa("Ecco la classifica:");

        int i = 1;
        for (Map.Entry<Short, Integer> entry : mappaRisultati.entrySet()) {
            String giocatore = Printer.rightPad(giocatori.get(entry.getKey()), 10);
            Integer punteggio = entry.getValue();

            this.printer.stampa("%d - %s  %d", i, giocatore, punteggio);
        }
    }

    /**
     * Comunica la disconnessione di un giocatore
     * @param idGiocatoreDisconnesso
     */
    @Override
    public void GiocatoreDisconnesso(int idGiocatoreDisconnesso) {
        this.printer.stampa("Il giocatore %s si è disconnesso!", giocatori.get(idGiocatoreDisconnesso));
    }

    /**
     * Stampa un messaggio di errore proveniente dal server
     * @param stringa messaggio da stampare
     */
    @Override
    public void stampaMessaggio(String stringa) {
        printer.stampa(stringa, ColorCli.ROSSO);
    }

    /**
     * mostra le risorse del giocatore
     */
    public void vediRisorse()
    {
        printer.stampaRisorse(this.risorse);
    }

    /**
     * mostra le risorse del giocatore
     */
    public void vediCartePlancia()
    {
        for (String carta : this.planciaCarte) {
            printer.stampa("%s", carta);
        }
    }

    @Override
    public void disabilitaCaselle(int idSpazioAzione) {

    }

    @Override
    public void aggiornaRisorse(int idGiocatore, Risorsa risorsa) {

    }

    /**
     * Classe per gestire gli input dell'utente
     */
    private class ReaderHandler extends Thread {

        @Override
        public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    String line = reader.readLine();
                    gestoreComandi.handle(line);

                } catch (ComandoSconosciutoException e) {
                    printer.stampa("Comando sconosciuto");
                } catch (IOException e) {
                    System.out.println(e);
                    break;
                }
            }
        }
    }

}
