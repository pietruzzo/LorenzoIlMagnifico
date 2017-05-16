package Domain;

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
    public void PiazzaFamiliare(Familiare familiare) throws Exception {
        this.ValidaPiazzamentoFamiliare(familiare);
        this.FamiliarePiazzato = familiare;
        super.PiazzaFamiliare(familiare);
    }

    /** Verifica se è possibile piazzare il familiare nello spazio azione */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare) throws Exception {
        if(this.FamiliarePiazzato != null)
            throw new Exception("Questo spazio azione è già occupato da un altro familiare!");
        super.ValidaPiazzamentoFamiliare(familiare);
    }
}
