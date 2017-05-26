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

    }

    /** Verifica se è possibile piazzare il familiare nello spazio azione */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare, Boolean torreOccupata) throws Exception {
        if(this.FamiliarePiazzato != null)
            throw new Exception("Questo spazio azione è già occupato da un altro familiare!");
        if(this.CartaAssociata == null)
            throw new Exception("A questo spazio azione non è associata alcuna carta!");

        Risorsa costoEffetti = super.ValidaValoreAzione(familiare);

        //Calcola il malus dovuto dall'occupazione della torre
        //Se la torre è occupata il malus è di -3 monete
        Risorsa malusTorreOccupata = new Risorsa();
        if(torreOccupata)
            if(familiare.Giocatore.Risorse.getMonete() < 3)
                throw new Exception("Siccome la torre è occupata, sono necessarie almeno 3 monete per prendere la carta.");
            else
                malusTorreOccupata= malusTorreOccupata.setRisorse(Risorsa.TipoRisorsa.MONETE, 3);

        //Valuta se il giocatore rimarrebbe con tutte le risorse in positivo prendendo la carta
        //Considera il bonus dello spazio azione, il costo della carta, il malus della torre occupata e gli effetti delle carte (anche le carte scomunica)
        if(!Risorsa.sub(Risorsa.add(familiare.Giocatore.Risorse, this.BonusRisorse),
                        Risorsa.add(Risorsa.add(this.CartaAssociata.CostoRisorse, malusTorreOccupata), costoEffetti)).isPositivo())
            throw new Exception("Non si dispone di risorse sufficienti per poter prendere la carta.");

        //Valuta se il giocatore ha abbastanza spazio nella plancia per prendere la carta
        this.CartaAssociata.ValidaPresaCarta(familiare.Giocatore, this);

        if(this.CartaAssociata instanceof CartaTerritorio)
            this.ValidaCartaTerritorio(familiare.Giocatore, costoEffetti.getPuntiMilitari());
    }

    public Carta getCartaAssociata() {
        return CartaAssociata;
    }

    /**
     * Valuta se il giocatore ha abbastanza punti militari per poter piazzare la carta nella plancia
     */
    private void ValidaCartaTerritorio(Giocatore giocatore, int costoPuntiMilitariEffetti) throws Exception {
        int minimoPuntiMilitari = 0;
        switch (giocatore.CarteTerritorio.size())
        {
            case 2:
                minimoPuntiMilitari = 1;
                break;

            case 3:
                minimoPuntiMilitari = 4;
                break;

            case 4:
                minimoPuntiMilitari = 10;
                break;

            case 5:
                minimoPuntiMilitari = 20;
                break;
        }

        if((giocatore.Risorse.getPuntiMilitari() + this.BonusRisorse.getPuntiMilitari() - costoPuntiMilitariEffetti) < minimoPuntiMilitari)
            throw new Exception(String.format("Per poter prendere questa carta sono necessari almeno {0} punti militari", minimoPuntiMilitari));
    }
}
