package Domain;

/**
 * Created by Portatile on 13/05/2017.
 */
public class SpazioAzioneTorre extends SpazioAzione {

    protected Familiare FamiliarePiazzato;
    protected Carta CartaAssociata;

    /**
     * Costruttore
     */
    public SpazioAzioneTorre(int valore, int bonusLegni, int bonusPietre, int bonusMonete, int bonusMilitare) {
        super(valore, bonusLegni, bonusPietre, 0, bonusMonete, bonusMilitare);
        this.Tipo = TipoSpazioAzione.Torre;
    }

    /**
     * Associa una carta allo spazio azione
     */
    public void AssociaCarta(Carta cartaDaAssociare)
    {
        this.CartaAssociata = cartaDaAssociare;
    }

    /**
     * Consente di piazzare un familiare nello spazioAzione, previa verifica
     */
    public void PiazzaFamiliare(Familiare familiare, Boolean torreOccupata) throws Exception {
        this.ValidaPiazzamentoFamiliare(familiare, torreOccupata);
        this.FamiliarePiazzato = familiare;
        super.PiazzaFamiliare(familiare);
        this.CartaAssociata.AssegnaGiocatore(familiare.Giocatore, this, torreOccupata);
    }

    /** Verifica se è possibile piazzare il familiare nello spazio azione */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare, Boolean torreOccupata) throws Exception {
        if(this.FamiliarePiazzato != null)
            throw new Exception("Questo spazio azione è già occupato da un altro familiare!");
        if(this.CartaAssociata == null)
            throw new Exception("A questo spazio azione non è associata alcuna carta!");

        this.CartaAssociata.ValidaPresaCarta(familiare.Giocatore, this, torreOccupata);

        super.ValidaPiazzamentoFamiliare(familiare);
    }
}
