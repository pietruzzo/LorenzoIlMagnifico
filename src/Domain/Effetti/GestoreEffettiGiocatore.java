package Domain.Effetti;

import Domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class GestoreEffettiGiocatore {

    private Giocatore giocatoreCorrente;

    private Risorsa malusRisorsaScomunica;
    private List<Effetto> listaEffettiValidati;


    public GestoreEffettiGiocatore(Giocatore giocatoreCorrente) {
        this.giocatoreCorrente = giocatoreCorrente;
    }

    /**
     * @param carteGiocatore Tutte le carte del giocatore
     * @param azione il valore dell'azione senza effettiCarte
     * //@param bonusSpazioAzione informazioni di dominio (basta avere la casella)
     * @param casella
     * @param risorseAllocate (Risorse
     * @return Costo generato dagli effetti delle carte
     */
    public static Risorsa validaCarte(List<Carta> carteGiocatore, int azione, SpazioAzione casella, Risorsa risorseAllocate){

        //Esegui ModificaValoreAzione

        //Filtra carte attive (non include la carta che prendo)

        //esegui effetto immediato se di validazione

        //Esegui Validabile

        //Azzera trigger
        return new Risorsa();

    }

    public static Risorsa effettuaAzione(List<Carta> carteGiocatore, int azione, SpazioAzione casella, Risorsa risorseAllocate){

        //Esegui ModificaValoreAzione

        //Filtra carte attive (non include la carta che prendo)

        //esegui effetto immediato se di Azionabile

        //Rimuovi effetto immediato se Azionabile

        //Esegui Validabile

        //Azzera trigger
       return new Risorsa();
    }

    public static void inizioTurno(List<Carta> carteGiocatore){}

    public static void endGame(List<Carta> carteGiocatore, Risorsa risorseGiocatore){}

    private static void eliminaImmediati(List<Carta> carteGiocatore){}
    private static void fineMossa(){}

    /**
     * @param tipoAzione tipo di azione che si compie
     * @return la lista degli effetti che andranno verificati in relazione al tipo di Azione
     */
    private List<Effetto> selezionaEffetti(TipoAzione tipoAzione){
        //TODO
        return new ArrayList<Effetto>();
    }

    /**
     * @param casella casella corrente del familiare
     * @return tipo azione ricavato dallo spazio azione corrente
     * @throws NullPointerException
     */
    private TipoAzione getTipoAzione(SpazioAzione casella) throws NullPointerException{
        //TODO
        return TipoAzione.GENERICA;
        }


}
