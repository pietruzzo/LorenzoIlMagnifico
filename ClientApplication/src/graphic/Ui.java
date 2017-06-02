package graphic;

import Domain.ColoreDado;
import Domain.Risorsa;
import Domain.Tabellone;

import java.util.Map;

/**
 * Created by casa on 26/05/17.
 */
public interface Ui {

    /**
     * Disabilita casella indicata da
     * @param idSpazioAzione
     */
    public abstract void disabilitaCaselle(int idSpazioAzione);

    /**
     * Se presenti più di un privilegio, si intedono diversi
     * @param numeroPergamene numero di pergamene da riscattare
     */
    public abstract void visualizzaPrivilegioConsiglio(int numeroPergamene);

    /**
     * Stampa eccezioni e messaggi.... attraverso popup
     */
    public abstract void stampaMessaggio(String stringa);

    public abstract void inizializzaPartita(Tabellone tabellone);

    /**
     * Aggiorna le risorse del GiocatoreGraphic e la posizione delle pedine del GiocatoreGraphic (non i familiari, quelle dei punti)
     */
    public abstract void aggiornaRisorse(int idGiocatore, Risorsa risorsa);

    /**
     * Il familiare viene indicato univocamente dal GiocatoreGraphic e dal colore del dado (ci sarà anche il colore per il neutro)
     * Se è uno spazio azione torre deve anche prendere la carta dello spazio azione e aggiungerla alla plancia del GiocatoreGraphic (solo se è il GiocatoreGraphic corrente,
     * altrimenti la toglie e basta, perchè non si vedono le carte degli altri)
     */
    public abstract void spostaFamiliare(int idGiocatore, ColoreDado coloreDado, int idSpazioAzione);

    /**
     * fa quello che fanno i due prima
     */
    public abstract void aggiornaGiocatore(int idGiocatore, Risorsa risorsa, ColoreDado coloreDado, int idSpazioAzione);
    public abstract void aggiungiScomunica(int[] idGiocatoriScomunicati, int periodo);

    /**
     *  Pulisce il campo (toglie le vecchie carte, rimette a posto i familiari)
     *  Imposta le nuove carte nella torre, mette il valore dei dadi e ordina le pedine dei giocatori
     * @param ordineGiocatori array di idGiocatore ordinati secondo l'ordine di gioco
     * @param dadi esito dei dadi nell'ordine nero bianco arancione
     * @param carte mappa che collega l'id dello spazio azione con il nome della carta ad esso associata
    */
    public abstract void iniziaTurno(int[] ordineGiocatori, int[] dadi, Map<Integer, String> carte);

    /**
     * Forse verranno aggiunte le mosse possibili (così Pietro è contento)
     * Per ora ci limitiamo ad abilitare cose (tipo lo spostamento dei familiari) se sono il GiocatoreGraphic interessato
     * altrimenti disabilita le cose
     */
    public abstract void iniziaMossa(int idGiocatore);


    /**
     * permette di scegliere le cose per la scomunica
     */
    public abstract void sceltaSostegnoChiesa();

    /**
     * Mostra popup con classifica
     */
    public abstract void printaResoconto();

}
