package Domain;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaEdificio extends Carta {

    //region Proprieta
    protected int CostoLegni;
    protected int CostoPietre;
    protected int CostoServitori;
    protected int CostoMonete;
    //endregion

    /**
     * Costruttore
     */
    public CartaEdificio(String nome, int periodo, Effetto effettoImmediato, Effetto effettoPermanente
                        , int costoLegni, int costoPietre, int costoServitori, int costoMonete)
    {
        super(nome, periodo, effettoImmediato, effettoPermanente);
        this.CostoLegni = costoLegni;
        this.CostoPietre = costoPietre;
        this.CostoServitori = costoServitori;
        this.CostoMonete = costoMonete;
    }

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata) throws Exception {
        //Verifica se il giocatore ha abbastanza spazio per prendere la carta
        if(giocatore.CarteEdificio.size() >= 6)
            throw new Exception("E' stato raggiunto il limite di carte Edificio.");

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
    }

    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata)
    {
        giocatore.CarteEdificio.add(this);

        int costoMonete = this.CostoMonete;
        if(torreOccupata)
            costoMonete += 3;

        giocatore.PagaRisorse(this.CostoLegni, this.CostoPietre, this.CostoServitori, costoMonete);
    }
}
