package Domain;

import Exceptions.DomainException;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Portatile on 12/05/2017.
 */
public class SpazioAzione  implements Serializable {

    //region Proprieta
    private int idSpazioAzione;
    protected int Valore;
    protected TipoSpazioAzione Tipo;
    protected Risorsa BonusRisorse;

    //endregion

    protected SpazioAzione(){};

    /**
     * Costruttore SpazioAzione
     */
    protected SpazioAzione(int valore, Risorsa bonusRisorse)
    {
        Tabellone.maxIdSpazioAzione = Tabellone.maxIdSpazioAzione+1;

        this.idSpazioAzione = Tabellone.maxIdSpazioAzione;
        this.Valore = valore;
        this.BonusRisorse = bonusRisorse;

    }

    /**
     * Ritorna l'id dello spazio azione
     */
    public int getIdSpazioAzione() {
        return idSpazioAzione;
    }

    /**
     * @return copia di BonusRisorse
     */
    public Risorsa getBonusRisorse() {
        return new Risorsa(BonusRisorse.getArrayRisorse());
    }

    /**
     * Metodo base per aggiornare i parametri del giocatore in funzione dei bonus dello spazio azione
     */
    protected void PiazzaFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException {
        Risorsa costoComplessivoEffetti;
        costoComplessivoEffetti = familiare.Giocatore.gestoreEffettiGiocatore.effettuaAzione(new Risorsa(), new AtomicInteger(familiare.getValore() + servitoriAggiunti), this);

        familiare.Giocatore.PagaRisorse(costoComplessivoEffetti);
        familiare.Giocatore.PagaRisorse(new Risorsa(Risorsa.TipoRisorsa.SERVI, servitoriAggiunti));
        familiare.SetSpazioAzioneAttuale(this);
        familiare.OttieniBonusSpazioAzione();
    }

    /**
     * Validazione base per poter piazzare un familiare
     * @param familiare familiare che effettua l'azione
     * @param servitoriAggiunti servitori aggiunti per aumentare il valore dell'azione
    */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException {
        this.ValidaValoreAzione(familiare, servitoriAggiunti);
    }

    /**
     * Effettua la validazione ritornando le risorse calcolate in base agli effetti delle carte del giocatore
     * @param familiare familiare che effettua l'azione
     * @param servitoriAggiunti servitori aggiunti per aumentare il valore dell'azione
     */
    protected Risorsa ValidaValoreAzione(Familiare familiare, int servitoriAggiunti)throws DomainException {
        Risorsa costoEffetti = new Risorsa();
        AtomicInteger valoreAzioneFinale = new AtomicInteger(familiare.getValore());
        valoreAzioneFinale.set(valoreAzioneFinale.get()+ servitoriAggiunti);

        //Modifica costoEffetti e valoreAzioneFinale
        familiare.Giocatore.gestoreEffettiGiocatore.validaAzione(costoEffetti, valoreAzioneFinale, this);

        if(valoreAzioneFinale.get() < this.Valore)
            throw new DomainException(String.format("E' necessario un valore di almeno %d per poter piazzare un familiare!", this.Valore));

        return costoEffetti;
    }

    /**
     * Toglie tutti i familiari dallo spazio azione
     */
    protected void RimuoviFamiliari(){};

    /**
     * Metodo base per effettuare azioni bonus, ovvero senza piazzamento familiare
     * @param giocatore giocatore che effettua l'azione
     * @param valoreAzione valore dell'azione
     */
    protected void AzioneBonusEffettuata(Giocatore giocatore, int valoreAzione, Risorsa bonusRisorse) throws DomainException {
        AtomicInteger valoreAzioneRef = new AtomicInteger(valoreAzione);
        Risorsa costoComplessivoEffetti;
        costoComplessivoEffetti = giocatore.gestoreEffettiGiocatore.effettuaAzione(new Risorsa(), valoreAzioneRef, this);
        giocatore.PagaRisorse(costoComplessivoEffetti);
        giocatore.OttieniBonusRisorse(this.BonusRisorse);
    }

    /**
     * Effettua la validazione dell'azione bonus ritornando le risorse calcolate in base agli effetti delle carte del giocatore
     * @param giocatore giocatore che effettua l'azione bonus
     * @param valoreAzione valore dell'azione
     */
    protected Risorsa ValidaValoreAzioneBonus(Giocatore giocatore, int valoreAzione)throws DomainException {
        Risorsa costoEffetti = new Risorsa();
        AtomicInteger valoreAzioneFinale = new AtomicInteger(valoreAzione);

        //Modifica costoEffetti e valoreAzione
        giocatore.gestoreEffettiGiocatore.validaAzione(costoEffetti, valoreAzioneFinale, this);

        if(valoreAzioneFinale.get() < this.Valore)
            throw new DomainException(String.format("E' necessario un valore di almeno %d per poter piazzare un familiare!", this.Valore));

        return costoEffetti;
    }
}
