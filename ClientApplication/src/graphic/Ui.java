package graphic;

import Domain.ColoreDado;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.TipoAzione;

import java.util.LinkedHashMap;
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
     * Gestisce l'evento di effettuazione di un'azione bonus
     * @param tipoAzione tipo di azione da svolgere
     * @param valoreAzione valore dell'azione da svolgere
     */
    public abstract void effettuaAzioneBonus(TipoAzione tipoAzione, int valoreAzione, Risorsa bonusRisorse);


    /**
     * Stampa eccezioni e messaggi.... attraverso popup
     */
    public abstract void stampaMessaggio(String stringa);

    /**
     * Configura le plancie dei giocatori
     * Dispone le pedine dei giocatori e i familiari da piazzare
     * @param tabellone
     */
    public abstract void inizializzaPartita(Tabellone tabellone);

    /**
     * Aggiorna le risorse del giocatore e la posizione delle sue pedine (non i familiari, quelle dei punti)
     */
    public abstract void aggiornaRisorse(int idGiocatore, Risorsa risorsa);

    /**
     * Viene chiamato in seguito all'aggiornamento di un giocatore tramite azione bonus
     * Differisce dall'aggiornaRisorse in quanto è possibile che un giocatore prenda una carta senza muovere familiari
     * @param idGiocatore id del giocatore che ha effettuato l'azione bonus
     * @param risorsa risorse del giocatore da aggiornare
     * @param idSpazioAzione id dello spazio azione sul quale è stata effettuata l'azione bonus
     */
    public abstract void aggiornaDaAzioneBonus(int idGiocatore, Risorsa risorsa, int idSpazioAzione);

    /**
     * Il familiare viene indicato univocamente dal Giocatore e dal colore del dado (ci sarà anche il colore per il neutro)
     * Se è uno spazio azione torre deve anche prendere la carta dello spazio azione e aggiungerla alla plancia del giocatore (solo se è il giocatore corrente,
     * altrimenti la toglie e basta, perchè non si vedono le carte degli altri)
     * Aggiorna le risorse
     */
    public abstract void aggiornaGiocatore(int idGiocatore, Risorsa risorsa, ColoreDado coloreDado, int idSpazioAzione);

    /**
     * Aggiunge il cubo scomunica nella tessera indicata dal periodo
     * @param idGiocatoriScomunicati lista dei giocatori scomunicati
     * @param periodo periodo nel quale è avvenuta la scomunica
     */
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
     * Per ora ci limitiamo ad abilitare cose (tipo lo spostamento dei familiari) se sono il giocatore interessato
     * altrimenti disabilita le cose
     */
    public abstract void iniziaMossa(int idGiocatore);

    /**
     * permette di scegliere se sostenere o meno la chiesa
     */
    public abstract void sceltaSostegnoChiesa();

    /**
     * Aggiorna le posizioni delle pedine relativamente ai punti vittoria dei giocatori
     * Mostra popup con classifica
     * @param mappaRisultati mappa ordinata (il primo elemento è il primo della classifica)
     *                       key: id del giocatore
     *                       value: punti vittoria corrispondenti
     */
    public abstract void finePartita(LinkedHashMap<Short, Integer> mappaRisultati);

}
