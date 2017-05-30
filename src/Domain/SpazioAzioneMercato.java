package Domain;

import Exceptions.DomainException;

/**
 * Created by Portatile on 13/05/2017.
 */
public class SpazioAzioneMercato extends SpazioAzione {

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
    public void PiazzaFamiliare(Familiare familiare) throws DomainException {
        this.ValidaPiazzamentoFamiliare(familiare);
        super.PiazzaFamiliare(familiare);
        this.FamiliarePiazzato = familiare;
    }

    /** Verifica se è possibile piazzare il familiare nello spazio azione */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare) throws DomainException {
        super.ValidaPiazzamentoFamiliare(familiare);

        if(this.FamiliarePiazzato != null)
            throw new DomainException("Questo spazio azione è già occupato da un altro familiare!");
    }
}
