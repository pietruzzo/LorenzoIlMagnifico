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
    public SpazioAzioneTorre(int valore, Risorsa bonusRisorse) {
        super(valore, bonusRisorse);
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
        this.FamiliarePiazzato.Giocatore.PagaRisorse(this.CartaAssociata.CostoRisorse);
        if(torreOccupata)
            this.FamiliarePiazzato.Giocatore.PagaRisorse(new Risorsa(Risorsa.TipoRisorsa.MONETE, 3));

        this.CartaAssociata.AssegnaGiocatore(familiare.Giocatore);
        this.CartaAssociata = null;
        //TODO gestione effetti permanenti, scomunica
    }

    /** Verifica se è possibile piazzare il familiare nello spazio azione */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare, Boolean torreOccupata) throws Exception {
        if(this.FamiliarePiazzato != null)
            throw new Exception("Questo spazio azione è già occupato da un altro familiare!");
        if(this.CartaAssociata == null)
            throw new Exception("A questo spazio azione non è associata alcuna carta!");

        super.ValidaPiazzamentoFamiliare(familiare);

        //Calcola il malus dovuto dall'occupazione della torre
        //Se la torre è occupata il malus è di -3 monete
        Risorsa malusTorreOccupata = new Risorsa();
        if(torreOccupata)
            if(familiare.Giocatore.Risorse.getMonete() < 3)
                throw new Exception("Siccome la torre è occupata, sono necessarie almeno 3 monete per prendere la carta.");
            else
                malusTorreOccupata.setRisorse(Risorsa.TipoRisorsa.MONETE, 3);

        //TODO gestione effetti carte permanenti e scomunica
        if(!Risorsa.sub(Risorsa.add(familiare.Giocatore.Risorse, this.BonusRisorse), Risorsa.add(this.CartaAssociata.CostoRisorse, malusTorreOccupata)).isPositivo())
            throw new Exception("Non si dispone di risorse sufficienti per poter prendere la carta.");

        this.CartaAssociata.ValidaPresaCarta(familiare.Giocatore, this);
    }
}
