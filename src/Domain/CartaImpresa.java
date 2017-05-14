package Domain;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaImpresa extends Carta {

    //region Proprieta
    protected int CostoMilitare;
    protected int CostoLegni;
    protected int CostoPietre;
    protected int CostoServitori;
    protected int CostoMonete;
    protected Boolean SceltaCosto;
    //endregion

    /**
     * Costruttore
     */
    public CartaImpresa (String nome, int periodo, Effetto effettoImmediato, Effetto effettoPermanente
            , int costoLegni, int costoPietre, int costoServitori, int costoMonete, int costoMilitare)
    {
        super(nome, periodo, effettoImmediato, effettoPermanente);
        this.CostoLegni = costoLegni;
        this.CostoPietre = costoPietre;
        this.CostoServitori = costoServitori;
        this.CostoMonete = costoMonete;
        this.CostoMilitare = costoMilitare;

        //Una carta impresa può costare punti militari opppure risorse
        //Se sono specificati entrambi, allora l'utente potrà scegliere con cosa pagare
        if((costoLegni > 0 || costoPietre > 0 || costoServitori > 0 || costoMonete > 0) && costoMilitare > 0)
            this.SceltaCosto = true;
        else
            this.SceltaCosto = false;
    }

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata) throws Exception {
        //Verifica se il giocatore ha abbastanza spazio per prendere la carta
        if(giocatore.CarteImpresa.size() >= 6)
            throw new Exception("E' stato raggiunto il limite di carte Impresa.");

        //Verifica la presenza sufficiente di Monete
        if(torreOccupata && giocatore.Monete < 3)
            throw new Exception("Siccome la torre è occupata, sono necessarie almeno 3 monete per prendere la carta.");

        //Validazione risorse
        if( (giocatore.Legni + spazioAzioneTorre.BonusLegni) < this.CostoLegni
                ||  (giocatore.Pietre + spazioAzioneTorre.BonusPietre) < this.CostoPietre
                ||  (giocatore.Servitori + spazioAzioneTorre.BonusServitori) < this.CostoServitori
                ||  (giocatore.Monete + spazioAzioneTorre.BonusMonete) < this.CostoMonete
                ||  (torreOccupata && (giocatore.Monete + spazioAzioneTorre.BonusMonete - this.CostoMonete >= 3))
                )
            throw new Exception("Non si dispone di risorse sufficienti per poter prendere la carta.");

        //Validazione punti militari
        if( (giocatore.PuntiMilitari + spazioAzioneTorre.BonusMilitare) < this.CostoMilitare)
            throw new Exception("Non si dispone di punti militari sufficienti per poter prendere la carta.");
    }

    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata)
    {
        giocatore.CarteImpresa.add(this);

        int costoMonete = this.CostoMonete;
        if(torreOccupata)
            costoMonete += 3;

        giocatore.PagaRisorse(this.CostoLegni, this.CostoPietre, this.CostoServitori, costoMonete);
        giocatore.PagaPuntiMilitari(this.CostoMilitare);
    }
}
