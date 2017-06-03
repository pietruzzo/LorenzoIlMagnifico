package Domain;

import Exceptions.DomainException;

import java.io.Serializable;

/**
 * Created by Portatile on 13/05/2017.
 */
public class SpazioAzioneMercato extends SpazioAzione  implements Serializable {

    protected Familiare FamiliarePiazzato;

    /**
     * Costruttore
     */
    public SpazioAzioneMercato(int valore, Risorsa bonusRisorse)
    {
        super(valore, bonusRisorse);
        this.Tipo = TipoSpazioAzione.Mercato;
    }

    /**
     * Consente di piazzare un familiare nello spazioAzione, previa verifica
     */
    @Override
    public void PiazzaFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException {
        this.ValidaPiazzamentoFamiliare(familiare, servitoriAggiunti);
        super.PiazzaFamiliare(familiare, servitoriAggiunti);
        this.FamiliarePiazzato = familiare;
    }

    /** Verifica se è possibile piazzare il familiare nello spazio azione */
    @Override
    protected void ValidaPiazzamentoFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException {
        super.ValidaPiazzamentoFamiliare(familiare, servitoriAggiunti);

        if(this.FamiliarePiazzato != null)
            throw new DomainException("Questo spazio azione è già occupato da un altro familiare!");
    }

    /**
     * Toglie tutti i familiari dallo spazio azione
     */
    @Override
    protected void RimuoviFamiliari() {
        this.FamiliarePiazzato = null;
    }
}
