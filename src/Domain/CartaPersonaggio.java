package Domain;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaPersonaggio extends Carta {

    protected int CostoMonete;

    /**
     * Costruttore
     */
    public CartaPersonaggio (String nome, int periodo, Effetto effettoImmediato, Effetto effettoPermanente, int costoMonete)
    {
        super(nome, periodo, effettoImmediato, effettoPermanente);
        this.CostoMonete = costoMonete;
    }

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata) throws Exception {
        //Verifica se il giocatore ha abbastanza spazio per prendere la carta
        if(giocatore.CartePersonaggio.size() >= 6)
            throw new Exception("E' stato raggiunto il limite di carte Personaggio.");

        //Verifica la presenza sufficiente di Monete
        if(torreOccupata && giocatore.Monete < 3)
            throw new Exception("Siccome la torre è occupata, sono necessarie almeno 3 monete per prendere la carta.");

        //Validazione risorse
        if((giocatore.Monete + spazioAzioneTorre.BonusMonete) < this.CostoMonete
            ||  (torreOccupata && (giocatore.Monete + spazioAzioneTorre.BonusMonete - this.CostoMonete >= 3)))
            throw new Exception("Non si dispone di risorse sufficienti per poter prendere la carta.");

    }

    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata)
    {
        giocatore.CartePersonaggio.add(this);

        int costoMonete = this.CostoMonete;
        if(torreOccupata)
            costoMonete += 3;

        giocatore.PagaRisorse(0, 0, 0, costoMonete);
    }

}
