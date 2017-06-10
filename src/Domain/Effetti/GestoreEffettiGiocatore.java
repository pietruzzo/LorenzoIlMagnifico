package Domain.Effetti;

import Domain.*;
import Domain.Effetti.lista.effectInterface.*;
import Exceptions.SaltaTurnoException;
import Exceptions.SpazioAzioneDisabilitatoEffettoException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pietro on 18/05/17.
 * La gestione degli effetti permette partite in parallelo a patto che lo stesso GiocatoreGraphic non possa partecipare
 * contemporaneamente a più partite e che ci sia un mazzo di carte per ogni partita. Per giocare più partite con lo
 * stezzo mazzo è necessario gestire l'eventuale concorrenza tra trigger (es validaAzione e esegui effetto synchronized)
 */
public class GestoreEffettiGiocatore  {

    //region Proprieta
    private Giocatore giocatoreCorrente;

    private Risorsa malusRisorsaScomunica;
    private List<Effetto> listaEffettiValidati;
    private List<Carta> carteCorrenti; //Carte selezionate da Validazione
    private List<Effetto> effettiImmediati; //Estratto da Validazione
    //endregion

    /**
     * Costruttore
     * @param giocatoreCorrente giocatore sul quale considerare gli effetti
     */
    public GestoreEffettiGiocatore(Giocatore giocatoreCorrente) {
        this.giocatoreCorrente = giocatoreCorrente;
    }

    /**
     * @param costo           modificato dagli effetti (include solo le modifiche che influiscono sulla validità)
     * @param azione          valore dei dadi, verrà modificato dagli effetti
     * @param casella         casella corrente del familiare
     * @param risorseAllocate le risorse allocate dal GiocatoreGraphic per piazzare il familiare
     * @apiNote La carta presente in Torre non deve essere stata ancora aggiunta a giocatore, costo ed azione vengono
     * sovrascritti, casella e risorseAllocate solo letti
     */
    private void validaAzione(Risorsa costo, AtomicInteger azione, SpazioAzione casella, Risorsa risorseAllocate)
            throws SaltaTurnoException, SpazioAzioneDisabilitatoEffettoException {

        malusRisorsaScomunica = new Risorsa();

        //ottieni effetto immediato
        effettiImmediati = estraiEffettoImmediato(casella);

        //Filtra Carte Contesto Azione
        List<Carta> carteCorrenti = selezionaCartePerTipo(getTipoAzione(casella), giocatoreCorrente.getListaCarte());

        /*  Esegui ModificaValoreAzione
            Ipotizzo che gli effetti immediati non modifichino il valore dell'azione
         */
        for (Carta carta : carteCorrenti) {
            for (Effetto e : carta.getEffettoPermanente()) {
                if (e instanceof ModificaValoreAzione) {
                    ((ModificaValoreAzione) e).aggiungiValoreAzione(azione, getTipoAzione(casella));
                }
            }
        }

        //Filtra carte attive (non include la carta che prendo)
        carteCorrenti = selezionaCartePerValore(azione.get(), carteCorrenti);

        //esegui effetto immediato se Validabile
        if (!effettiImmediati.isEmpty()){
        for (Effetto effettoIm : effettiImmediati){
        if (effettoIm instanceof Validabile) {
            ((Validabile) effettoIm).valida(costo, azione.get(), casella, carteCorrenti, risorseAllocate, malusRisorsaScomunica);
        }}}

        //Esegui Validabile
        for (Carta c : carteCorrenti) {
            for(Effetto e: c.getEffettoPermanente()) {
                if (e != null && e instanceof Validabile) {
                    ((Validabile) e).valida(costo, azione.get(), casella, carteCorrenti, risorseAllocate, malusRisorsaScomunica);
                }
            }
        }
        //Azzera trigger
        azzeraTrigger(giocatoreCorrente.getListaCarte());

    }

    /**
     * @param costo           modificato dagli effetti (include solo le modifiche che influiscono sulla validità)
     * @param azione          valore dei dadi, verrà modificato dagli effetti
     * @param casella         casella corrente del familiare
     * @apiNote La carta presente in Torre non deve essere stata ancora aggiunta a giocatore, costo ed azione vengono
     * sovrascritti, casella e risorseAllocate solo letti
     */
    public void validaAzione(Risorsa costo, AtomicInteger azione, SpazioAzione casella){
        validaAzione(costo, azione, casella, this.giocatoreCorrente.getRisorse());
    }

    /**
     * @param costo           modificato dagli effetti (costo con cui validare la mossa)
     * @param azione          valore dei dadi, verrà modificato dagli effetti
     * @param casella         casella corrente del familiare
     * @param risorseAllocate le risorse allocate dal GiocatoreGraphic per piazzare il familiare
     * @return Ritorna Le risorse, conseguenze degli effetti (compreso di tutti i costi e bonus)
     * @apiNote La carta presente in Torre non deve essere stata ancora aggiunta a GiocatoreGraphic, costo ed azione vengono
     * sovrascritti, casella e risorseAllocate solo letti. Il valore di Return tiene conto del costo in ingresso.
     */
    private Risorsa effettuaAzione(Risorsa costo, AtomicInteger azione, SpazioAzione casella, Risorsa risorseAllocate)
            throws SaltaTurnoException, SpazioAzioneDisabilitatoEffettoException {

        Risorsa costoRitorno = costo.clone();
        validaAzione(costo, azione, casella, risorseAllocate);
        //esegui effetto immediato se di Azionabile
        if (!effettiImmediati.isEmpty()){
        for (Effetto effettoIm : effettiImmediati){
        if (effettoIm != null && effettoIm instanceof Azionabile) {
            ((Azionabile) effettoIm).aziona(costoRitorno, azione.get(), casella, carteCorrenti, risorseAllocate, malusRisorsaScomunica, giocatoreCorrente);
        }}}

        //Esegui Azionabile
        for (Carta c : carteCorrenti) {
            for(Effetto e : c.getEffettoPermanente()) {
                if (e != null && e instanceof Azionabile) {
                    ((Azionabile) e).aziona(costoRitorno, azione.get(), casella, carteCorrenti, risorseAllocate, malusRisorsaScomunica, giocatoreCorrente);
                }
            }
        }

        //Azzera trigger
        azzeraTrigger(giocatoreCorrente.getListaCarte());

        //Ritorna Costo ritorno
        return costoRitorno;
    }

    /**
     * @param costo           modificato dagli effetti (costo con cui validare la mossa)
     * @param azione          valore dei dadi, verrà modificato dagli effetti
     * @param casella         casella corrente del familiare
     * @return Ritorna Le risorse, conseguenze degli effetti (compreso di tutti i costi e bonus)
     * @apiNote La carta presente in Torre non deve essere stata ancora aggiunta a GiocatoreGraphic, costo ed azione vengono
     * sovrascritti, casella e risorseAllocate solo letti. Il valore di Return tiene conto del costo in ingresso.
     */
    public Risorsa effettuaAzione(Risorsa costo, AtomicInteger azione, SpazioAzione casella)
            throws SaltaTurnoException, SpazioAzioneDisabilitatoEffettoException{
        return effettuaAzione(costo, azione, casella, this.giocatoreCorrente.getRisorse());
    }


    public void inizioTurno(int turno) {
        List<Carta> listaCarte = giocatoreCorrente.getListaCarte();

        for (Carta c : listaCarte) {
            for(Effetto e : c.getEffettoPermanente()){
            if (e instanceof InizioTurno) {
                InizioTurno effetto = (InizioTurno) e;
                effetto.setupTurno(turno);
            }
        }
        }

        azzeraTrigger(giocatoreCorrente.getListaCarte());
    }


    public void endGame(Risorsa risorseGiocatore) {
        AtomicInteger modificaPuntiVittoria= new AtomicInteger(0);
        List<Carta> listaCarte = giocatoreCorrente.getListaCarte();

        for (Carta c : listaCarte) {
            for (Effetto e : c.getEffettoPermanente())
            if (e instanceof EndGame) {
                EndGame effetto = (EndGame) e;
                effetto.azioneTerminale(risorseGiocatore, listaCarte, modificaPuntiVittoria, giocatoreCorrente);
            }
        }
        giocatoreCorrente.getRisorse().add(new Risorsa(Risorsa.TipoRisorsa.MONETE, modificaPuntiVittoria.get()));
    }

    /**
     * @param tipoAzione tipo di azione che si compie
     * @return la lista delle carte che andranno verificate in relazione al tipo di Azione
     */
    @NotNull
    private List<Carta> selezionaCartePerTipo(TipoAzione tipoAzione, List<Carta> listaCompleta) {

        List<Carta> lista = new ArrayList<>();
        for (Carta c : listaCompleta) {
            if (c.getTipoCarta() == TipoCarta.Impresa || c.getTipoCarta() == TipoCarta.Personaggio
                    || c.getTipoCarta() ==TipoCarta.Scomunica) lista.add(c);
            else if ((c.getTipoCarta() == TipoCarta.Edificio && tipoAzione == TipoAzione.PRODUZIONE)
                    || (c.getTipoCarta() == TipoCarta.Territorio && tipoAzione == TipoAzione.RACCOLTO)) lista.add(c);
        }
        return lista;
    }

    /**
     * @param casella casella corrente del familiare
     * @return tipo azione ricavato dallo spazio azione corrente
     * @throws NullPointerException
     */
    private TipoAzione getTipoAzione(SpazioAzione casella) throws NullPointerException {
        if (casella instanceof SpazioAzioneRaccolto) return TipoAzione.RACCOLTO;
        else if (casella instanceof SpazioAzioneProduzione) return TipoAzione.PRODUZIONE;
        else if (casella.getIdSpazioAzione()<5) return TipoAzione.TORRE_TERRITORIO;
        else if (casella.getIdSpazioAzione()<9 && casella.getIdSpazioAzione()>4) return TipoAzione.TORRE_EDIFICIO;
        else if (casella.getIdSpazioAzione()<17 && casella.getIdSpazioAzione()>12) return TipoAzione.TORRE_IMPRESA;
        else if (casella.getIdSpazioAzione()<13 && casella.getIdSpazioAzione()>8) return TipoAzione.TORRE_PERSONAGGIO;
        else return TipoAzione.GENERICA;
    }

    /**
     * @param valoreAzione valore dell'Azione finale
     * @param lista        lista di carte da filtrare
     * @return nuova lista con carte di valore <= valoreAzione
     * @apiNote per ogni carta da lista, se il valore di attivazione è <= valoreAzione allora lo ritorno nella lista.
     *          le carte personaggio, impresa e scomunica sono sempre controllate
     */
    private List<Carta> selezionaCartePerValore(int valoreAzione, List<Carta> lista) {
        List<Carta> carteFiltrate = new ArrayList<>();
        for (Carta c : lista){
            if (c instanceof CartaTerritorio &&((CartaTerritorio) c).getValoreAzione()>=valoreAzione) carteFiltrate.add(c);
            else if (c instanceof CartaEdificio &&((CartaEdificio) c).getValoreAzione()>=valoreAzione) carteFiltrate.add(c);
            else carteFiltrate.add(c);
        }
        return carteFiltrate;
    }

    /**
     * @param listaCarte
     * @apiNote azzera tutti gli eventuali Trigger sugli effetti
     */
    private void azzeraTrigger(List<Carta> listaCarte) {
        for (Carta c : listaCarte) {
            if (c.getEffettoPermanente() instanceof Trigger) {
                ((Trigger) c).setDefaultTrigger();
            }
        }
    }

    /**
     * @param casella casella corrente del familiare, @nullable
     * @return effetto immediato associato, oppure null
     */
    private List<Effetto> estraiEffettoImmediato(SpazioAzione casella) {
        if (casella != null && casella instanceof SpazioAzioneTorre) {
            SpazioAzioneTorre spazioAzioneTorre = (SpazioAzioneTorre) casella;
            if (spazioAzioneTorre.getCartaAssociata().getEffettoImmediato() != null) {
                return spazioAzioneTorre.getCartaAssociata().getEffettoImmediato();
            }
        }
        return null;
    }
}
