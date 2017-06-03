package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;

import java.io.Serializable;
import java.util.List;

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
     * Metodo base per aggiornare i parametri del GiocatoreGraphic in funzione dei bonus dello spazio azione
     */
    protected void PiazzaFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException {
        Risorsa costoComplessivoEffetti;
        costoComplessivoEffetti = familiare.Giocatore.gestoreEffettiGiocatore.effettuaAzione(new Risorsa(), familiare.getValore() + servitoriAggiunti, this);

        familiare.Giocatore.PagaRisorse(costoComplessivoEffetti);
        familiare.Giocatore.PagaRisorse(new Risorsa(Risorsa.TipoRisorsa.SERVI, servitoriAggiunti));
        familiare.SetSpazioAzioneAttuale(this);
        familiare.OttieniBonusSpazioAzione();
    }

    /**
     * Validazione base per poter piazzare un familiare
      */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException {
        this.ValidaValoreAzione(familiare, servitoriAggiunti);
    }

    /**
     * Effettua la validazione ritornando le risorse calcolate in base agli effetti delle carte del GiocatoreGraphic
     */
    protected Risorsa ValidaValoreAzione(Familiare familiare, int servitoriAggiunti)throws DomainException {
        Risorsa costoEffetti = new Risorsa();
        Integer valoreAzioneFinale = familiare.getValore();
        valoreAzioneFinale += servitoriAggiunti;

        //Modifica costoEffetti e valoreAzioneFinale
        familiare.Giocatore.gestoreEffettiGiocatore.validaAzione(costoEffetti, valoreAzioneFinale, this);

        if(valoreAzioneFinale < this.Valore)
            throw new DomainException(String.format("E' necessario un valore di almeno {0} per poter piazzare un familiare!", this.Valore));

        return costoEffetti;
    }

    /**
     * Toglie tutti i familiari dallo spazio azione
     */
    protected void RimuoviFamiliari(){};
}
