package graphic.Cli;

import Domain.*;
import graphic.Ui;
import lorenzo.MainGame;

import java.awt.*;
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
    private Map<Integer, String> coloriGiocatori;
    private Map<String, Risorsa> mazzoCarte;
    private ArrayList<String> familiariDisponibili;
    private int idGiocatoreCorrente;
    private Risorsa risorse;
    private ArrayList<String> planciaCarte;
    private int[] valoreDadi;
    public int valoreAzioneBonus;
    public Risorsa risorsaAzioneBonus;




    public ControllerCli(MainGame mainGame) {
        this.mainGame = mainGame;
        this.printer = new Printer();
        this.planciaCarte = new ArrayList<>();
        this.giocatori = new HashMap<>();
        this.coloriGiocatori = new HashMap<>();
        this.mazzoCarte = new HashMap<>();
        this.familiariDisponibili = new ArrayList<>();
        this.gestoreComandi = new GestoreComandi(this);
        this.printer.stampa("In attesa di altri giocatori..");
        this.printer.stampa("per iniziare digitare il comando 'iniziaPartita'.");
        this.printer.stampa("Dall'inizio della partita avrai sempre a disposizione i comandi:\nvediRisorse\nvediCarte\nvediDadi\nvediCosto\n");

        new ReaderHandler().start();
    }


    @Override
    public void inizializzaPartita(Tabellone tabellone) {
        this.printer.stampa("La partita è cominciata!");
        this.printer.stampa("Partecipano i giocatori:");

        //Mostra i giocatori partecipanti
        for (Giocatore giocatore : tabellone.getGiocatori()) {
            this.giocatori.put((int)giocatore.getIdGiocatore(), giocatore.getNome());
            this.coloriGiocatori.put((int)giocatore.getIdGiocatore(), ColorCli.getCodeColorCliByColoreGiocatore(giocatore.getColore()));
            this.printer.stampa("%s %d - %s %s", coloriGiocatori.get((int)giocatore.getIdGiocatore()), giocatore.getIdGiocatore(), giocatore.getNome(), ColorCli.TAG_CHIUSURA);

            if(giocatore.getNome().equals(mainGame.getNomeGiocatore())) {
                idGiocatoreCorrente = giocatore.getIdGiocatore();
                risorse = giocatore.getRisorse().clone();
            }
        }

        //Inizializza le carte
        for(Carta carta : tabellone.getMazzoCarte())
        {
            mazzoCarte.put(carta.getNome(), carta.getCostoRisorse());
        }
    }

    /**
     * Inizia un nuovo turno di gioco
     * @param ordineGiocatori array di idGiocatore ordinati secondo l'ordine di gioco
     * @param dadi esito dei dadi nell'ordine nero bianco arancione
     * @param carte mappa che collega l'id dello spazio azione con il nome della carta ad esso associata
     */
    @Override
    public void iniziaTurno(int[] ordineGiocatori, int[] dadi, Map<Integer, String> carte) {
        this.printer.stampa("E' iniziato un nuovo turno!");
        this.printer.stampa("In questo turno i dadi avranno i seguenti valori:");

        this.valoreDadi = dadi;
        this.vediDadi();
        this.carteTabellone = carte;
        this.familiariDisponibili = new ArrayList<>();
        this.familiariDisponibili.add("w:bianco  ");
        this.familiariDisponibili.add("o:arancio  ");
        this.familiariDisponibili.add("b:nero  ");
        this.familiariDisponibili.add("n:neutro  ");
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
            this.printer.stampa("per effettuare l'azione digitare 'piazza colore idSpazioAzione servitoriAggiunti'");
            String coloriDisponibili = "";
            for (String colore : this.familiariDisponibili) {
                coloriDisponibili += colore;
            }
            this.printer.stampa("colori disponibili: %s", coloriDisponibili);

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
            if(idGiocatoreCorrente == idGiocatoriScomunicati[i])
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
        this.printer.stampa("Per rispondere digitare 'sostegno si/no'");
    }

    /**
     * Gestisce l'evento di effettuazione di un'azione bonus
     */
    @Override
    public void effettuaAzioneBonus(TipoAzione tipoAzione, int valoreAzione, Risorsa bonusRisorse) {
        this.valoreAzioneBonus = valoreAzione;
        this.risorsaAzioneBonus = bonusRisorse;
        this.gestoreComandi.setComandiAzioneBonus();
        this.printer.stampaTabellone(this.carteTabellone);
        this.printer.stampa("Per effetto di una carta, hai la possibilità di effettuare un'azione bonus!");
        this.printer.stampa("L'azione avrà un valore base di %d.", valoreAzione);
        this.printer.stampa("(per effettuare l'azione digitare 'bonus idSpazioAzione servitoriAggiunti')");
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
        this.printer.stampa("(per scegliere il privilegio digitare 'privilegio' seguito dal numero corrispondente)");
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

            this.familiariDisponibili.removeIf(x -> x.contains(coloreDado.getColoreString().toLowerCase()));

            if(idSpazioAzione <= 16 && !carteTabellone.get(idSpazioAzione).isEmpty())
            {
                this.planciaCarte.add(carteTabellone.get(idSpazioAzione));
            }
        }

        if(idSpazioAzione <= 16) {
            this.carteTabellone.replace(idSpazioAzione, String.format("%sPresa da %s %s", coloriGiocatori.get(idGiocatore), giocatori.get(idGiocatore), ColorCli.TAG_CHIUSURA));
        }
    }

    /**
     * Aggiorna le risorse del giocatore
     * @param idGiocatore
     * @param risorsa
     */
    @Override
    public void aggiornaRisorse(int idGiocatore, Risorsa risorsa) {
        if(idGiocatoreCorrente == idGiocatore) {
            this.risorse = risorsa.clone();
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
            String giocatore = Printer.rightPad(giocatori.get((int)entry.getKey()), 10);
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

    /**
     * mostra le risorse del giocatore
     */
    public void vediDadi()
    {
        this.printer.stampa("Nero:    %d", valoreDadi[0]);
        this.printer.stampa("Bianco:  %d", valoreDadi[1]);
        this.printer.stampa("Arancio: %d\n", valoreDadi[2]);
    }

    /**
     * mostra il costo della carta
     */
    public void vediCosto(String nomeCarta)
    {
        Risorsa costo = this.mazzoCarte.get(nomeCarta);

        if(costo == null)
            this.printer.stampa("Non è stata trovata nessuna carta con questo nome", ColorCli.ROSSO);
        else {
            this.printer.stampa("La carta %s ha un costo di:", nomeCarta);
            this.printer.stampaRisorse(costo);
        }
    }

    @Override
    public void disabilitaCaselle(int idSpazioAzione) {

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
