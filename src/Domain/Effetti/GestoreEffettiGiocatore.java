package Domain.Effetti;

import Domain.*;
import Domain.Effetti.lista.effectInterface.ModificaValoreAzione;
import Domain.Effetti.lista.effectInterface.Trigger;
import Domain.Effetti.lista.effectInterface.Validabile;
import org.jetbrains.annotations.NotNull;

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
     * @param costo modificato dagli effetti
     * @param azione valore dei dadi, verrà modificato dagli effetti
     * @param casella casella corrente del familiare
     * @param risorseAllocate le risorse spese dal giocatore per piazzare il familiare
     */
    public void validaAzione(Risorsa costo, Integer azione, SpazioAzione casella, Risorsa risorseAllocate){
        Effetto effettoImmediato;

        //ottieni effetto immediato
        effettoImmediato=estraiEffettoImmediato(casella);

        //Filtra Carte Contesto Azione
        List<Carta> carteCorrenti = selezionaCartePerTipo(getTipoAzione(casella), giocatoreCorrente.getListaCarte());

        /*  Esegui ModificaValoreAzione
            Ipotizzo che gli effetti immediati non modifichino il valore dell'azione
         */
        for (Carta carta : carteCorrenti){
            if (carta.getEffettoPermanente() instanceof ModificaValoreAzione){
                ((ModificaValoreAzione)carta).aggiungiValoreAzione(azione, getTipoAzione(casella));
            }
        }

        //Filtra carte attive (non include la carta che prendo)
        carteCorrenti=selezionaCartePerValore(azione, carteCorrenti);

        //esegui effetto immediato se Validabile
        if (effettoImmediato!=null && effettoImmediato instanceof Validabile){
            ((Validabile)effettoImmediato).valida(costo, azione, casella, carteCorrenti, risorseAllocate);
        }

        //Esegui Validabile
        for (Carta c: carteCorrenti) {
            if (c.getEffettoPermanente()!=null && c.getEffettoPermanente() instanceof Validabile){
                ((Validabile)c.getEffettoPermanente()).valida(costo, azione, casella, carteCorrenti, risorseAllocate);
            }
        }
        //Azzera trigger
        azzeraTrigger(giocatoreCorrente.getListaCarte());

    }

    public void effettuaAzione(Risorsa costo, Integer azione, SpazioAzione casella, Risorsa risorseAllocate){

        //Filtra Carte Contesto Azione
        

        //Esegui ModificaValoreAzione

        //Filtra carte attive (non include la carta che prendo)

        //esegui effetto immediato se di Azionabile

        //Rimuovi effetto immediato se Azionabile

        //Esegui Validabile

        //Azzera trigger
    }

    public static void inizioTurno(List<Carta> carteGiocatore){}

    public static void endGame(List<Carta> carteGiocatore, Risorsa risorseGiocatore){}

    private static void eliminaImmediati(List<Carta> carteGiocatore){}
    private static void fineMossa(){}

    /**
     * @param tipoAzione tipo di azione che si compie
     * @return la lista delle carte che andranno verificati in relazione al tipo di Azione
     */
    @NotNull
    private List<Carta> selezionaCartePerTipo(TipoAzione tipoAzione, List<Carta> listaCompleta){
        //TODO
        return new ArrayList<Carta>();
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

    /**
     * @param valoreAzione valore dell'Azione finale
     * @param lista lista di carte da filtrare
     * @return nuova lista con carte di valore <= valoreAzione
     * @implSpec
     */
    private List<Carta> selezionaCartePerValore(int valoreAzione, List<Carta> lista){
        //TODO
        return new ArrayList<Carta>();
        }

    /**
     * @param listaCarte
     * @implSpec azzera tutti gli eventuali Trigger sugli effetti
     */
    private void azzeraTrigger(List<Carta> listaCarte){
        for (Carta c: listaCarte) {
            if (c.getEffettoPermanente() instanceof Trigger){
                ((Trigger)c).setDefaultTrigger();
            }
        }
    }

    /**
     * @param casella casella corrente del familiare, @nullable
     * @return effetto immediato associato, oppure null
     */
    private Effetto estraiEffettoImmediato(SpazioAzione casella){
        if (casella!=null && casella instanceof SpazioAzioneTorre){
            SpazioAzioneTorre spazioAzioneTorre = (SpazioAzioneTorre) casella;
            if (spazioAzioneTorre.getCartaAssociata().getEffettoImmediato()!=null){
                return spazioAzioneTorre.getCartaAssociata().getEffettoImmediato();
            }
        }
        return null;
    }
}
