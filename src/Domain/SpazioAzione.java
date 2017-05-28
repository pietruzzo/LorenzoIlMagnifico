package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;

import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class SpazioAzione {

    //region Proprieta
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
        this.Valore = valore;
        this.BonusRisorse = bonusRisorse;
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
    public void PiazzaFamiliare(Familiare familiare) throws DomainException {
        Risorsa costoComplessivoEffetti;

        ValidaPiazzamentoFamiliare(familiare);

        costoComplessivoEffetti = familiare.Giocatore.gestoreEffettiGiocatore.effettuaAzione(new Risorsa(), familiare.Valore, this);

        familiare.Giocatore.PagaRisorse(costoComplessivoEffetti);
        familiare.SetSpazioAzioneAttuale(this);
        familiare.OttieniBonusSpazioAzione();


    }

    /**
     * Validazione base per poter piazzare un familiare
      */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare) throws DomainException {
        this.ValidaValoreAzione(familiare);
    }

    /**
     * Effettua la validazione ritornando le risorse calcolate in base agli effetti delle carte del giocatore
     */
    protected Risorsa ValidaValoreAzione(Familiare familiare)throws DomainException {
        Risorsa costoEffetti = new Risorsa();
        Integer valoreAzioneFinale=familiare.Valore;

        //Modifica costoEffetti e valoreAzioneFinale
        familiare.Giocatore.gestoreEffettiGiocatore.validaAzione(costoEffetti, valoreAzioneFinale, this);

        if(valoreAzioneFinale < this.Valore)
            throw new DomainException(String.format("E' necessario un valore di almeno {0} per poter piazzare un familiare!", this.Valore));

        return costoEffetti;
    }
}
