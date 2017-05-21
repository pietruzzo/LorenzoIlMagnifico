package Domain.Effetti;

import Domain.*;
import Domain.Effetti.lista.effectInterface.*;
import Exceptions.SaltaTurnoException;
import Exceptions.SpazioAzioneDisabilitatoEffettoException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietro on 18/05/17.
 * La gestione degli effetti permette partite in parallelo a patto che lo stesso giocatore non possa partecipare
 * contemporaneamente a più partite e che ci sia un mazzo di carte per ogni partita. Per giocare più partite con lo
 * stezzo mazzo è necessario gestire l'eventuale concorrenza tra trigger (es validaAzione e esegui effetto synchronized)
 */
public class GestoreEffettiGiocatore {

    private Giocatore giocatoreCorrente;

    private Risorsa malusRisorsaScomunica;
    private List<Effetto> listaEffettiValidati;
    private List<Carta> carteCorrenti; //Carte selezionate da Validazione
    private Effetto effettoImmediato; //Estratto da Validazione


    public GestoreEffettiGiocatore(Giocatore giocatoreCorrente) {
        this.giocatoreCorrente = giocatoreCorrente;
    }

    /**
     * @param costo           modificato dagli effetti (include solo le modifiche che influiscono sulla validità)
     * @param azione          valore dei dadi, verrà modificato dagli effetti
     * @param casella         casella corrente del familiare
     * @param risorseAllocate le risorse allocate dal giocatore per piazzare il familiare
     * @apiNote La carta presente in Torre non deve essere stata ancora aggiunta a giocatore, costo ed azione vengono
     * sovrascritti, casella e risorseAllocate solo letti
     */
    public void validaAzione(Risorsa costo, Integer azione, SpazioAzione casella, Risorsa risorseAllocate)
            throws SaltaTurnoException, SpazioAzioneDisabilitatoEffettoException {

        malusRisorsaScomunica = new Risorsa();

        //ottieni effetto immediato
        effettoImmediato = estraiEffettoImmediato(casella);

        //Filtra Carte Contesto Azione
        List<Carta> carteCorrenti = selezionaCartePerTipo(getTipoAzione(casella), giocatoreCorrente.getListaCarte());

        /*  Esegui ModificaValoreAzione
            Ipotizzo che gli effetti immediati non modifichino il valore dell'azione
         */
        for (Carta carta : carteCorrenti) {
            if (carta.getEffettoPermanente() instanceof ModificaValoreAzione) {
                ((ModificaValoreAzione) carta).aggiungiValoreAzione(azione, getTipoAzione(casella));
            }
        }

        //Filtra carte attive (non include la carta che prendo)
        carteCorrenti = selezionaCartePerValore(azione, carteCorrenti);

        //esegui effetto immediato se Validabile
        if (effettoImmediato != null && effettoImmediato instanceof Validabile) {
            ((Validabile) effettoImmediato).valida(costo, azione, casella, carteCorrenti, risorseAllocate, malusRisorsaScomunica);
        }

        //Esegui Validabile
        for (Carta c : carteCorrenti) {
            if (c.getEffettoPermanente() != null && c.getEffettoPermanente() instanceof Validabile) {
                ((Validabile) c.getEffettoPermanente()).valida(costo, azione, casella, carteCorrenti, risorseAllocate, malusRisorsaScomunica);
            }
        }
        //Azzera trigger
        azzeraTrigger(giocatoreCorrente.getListaCarte());

    }

    /**
     * @param costo           modificato dagli effetti (costo con cui validare la mossa)
     * @param azione          valore dei dadi, verrà modificato dagli effetti
     * @param casella         casella corrente del familiare
     * @param risorseAllocate le risorse allocate dal giocatore per piazzare il familiare
     * @return Ritorna Le risorse, conseguenze degli effetti (compreso di tutti i costi e bonus)
     * @apiNote La carta presente in Torre non deve essere stata ancora aggiunta a giocatore, costo ed azione vengono
     * sovrascritti, casella e risorseAllocate solo letti. Il valore di Return tiene conto del costo in ingresso.
     */
    public void effettuaAzione(Risorsa costo, Integer azione, SpazioAzione casella, Risorsa risorseAllocate)
            throws SaltaTurnoException, SpazioAzioneDisabilitatoEffettoException {

        Risorsa costoRitorno = costo.clone();
        validaAzione(costo, azione, casella, risorseAllocate);
        //esegui effetto immediato se di Azionabile
        if (effettoImmediato != null && effettoImmediato instanceof Azionabile) {
            ((Azionabile) effettoImmediato).aziona(costoRitorno, azione, casella, carteCorrenti, risorseAllocate, malusRisorsaScomunica);
        }

        //Esegui Azionabile
        for (Carta c : carteCorrenti) {
            if (c.getEffettoPermanente() != null && c.getEffettoPermanente() instanceof Azionabile) {
                ((Azionabile) c.getEffettoPermanente()).aziona(costoRitorno, azione, casella, carteCorrenti, risorseAllocate, malusRisorsaScomunica);
            }
        }

        //Azzera trigger
        azzeraTrigger(giocatoreCorrente.getListaCarte());
    }

    public void inizioTurno(int turno) {
        List<Carta> listaCarte = giocatoreCorrente.getListaCarte();

        for (Carta c : listaCarte) {
            if (c.getEffettoPermanente() instanceof InizioTurno) {
                InizioTurno effetto = (InizioTurno) c.getEffettoPermanente();
                effetto.setupTurno(turno);
            }
        }

        azzeraTrigger(giocatoreCorrente.getListaCarte());
    }

    public void endGame(Risorsa risorseGiocatore) {
        List<Carta> listaCarte = giocatoreCorrente.getListaCarte();

        for (Carta c : listaCarte) {
            if (c.getEffettoPermanente() instanceof EndGame) {
                EndGame effetto = (EndGame) c.getEffettoPermanente();
                effetto.azioneTerminale(risorseGiocatore, listaCarte);
            }
        }
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
                    || true/*TODO:Se carta scomunica*/) lista.add(c);
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
        if (casella instanceof SpazioAzioneProduzione) return TipoAzione.PRODUZIONE;
        if (casella instanceof SpazioAzioneTorre) {
        }//TODO come riconosco da quale torre viene?
        return TipoAzione.GENERICA;
    }

    /**
     * @param valoreAzione valore dell'Azione finale
     * @param lista        lista di carte da filtrare
     * @return nuova lista con carte di valore <= valoreAzione
     * @apiNote per ogni carta da lista, se il valore di attivazione è <= valoreAzione allora lo ritorno nella lista
     */
    private List<Carta> selezionaCartePerValore(int valoreAzione, List<Carta> lista) {
        //TODO come ottengo il valore di attivazione dalla carta?
        return new ArrayList<Carta>();
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
    private Effetto estraiEffettoImmediato(SpazioAzione casella) {
        if (casella != null && casella instanceof SpazioAzioneTorre) {
            SpazioAzioneTorre spazioAzioneTorre = (SpazioAzioneTorre) casella;
            if (spazioAzioneTorre.getCartaAssociata().getEffettoImmediato() != null) {
                return spazioAzioneTorre.getCartaAssociata().getEffettoImmediato();
            }
        }
        return null;
    }
}
