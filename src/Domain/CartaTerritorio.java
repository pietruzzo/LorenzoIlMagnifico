package Domain;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaTerritorio extends Carta {

    protected int CostoMilitare;

    /**
     * Costruttore
     */
    public CartaTerritorio(String nome, int periodo, Effetto effettoImmediato, Effetto effettoPermanente, int costoMilitare)
    {
        super(nome, periodo, effettoImmediato, effettoPermanente);
        this.CostoMilitare = costoMilitare;
    }

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata) throws Exception {
        //Verifica se il giocatore ha abbastanza spazio per prendere la carta
        if(giocatore.CarteTerritorio.size() >= 6)
            throw new Exception("E' stato raggiunto il limite di carte Territorio.");

        //Verifica la presenza sufficiente di Monete
        if(torreOccupata && giocatore.Monete < 3)
            throw new Exception("Siccome la torre è occupata, sono necessarie almeno 3 monete per prendere la carta.");

        //region Validazione punti militari
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

        if((giocatore.PuntiMilitari + spazioAzioneTorre.BonusMilitare) < minimoPuntiMilitari)
            throw new Exception(String.format("Per poter prendere questa carta sono necessari almeno {0} punti militari", minimoPuntiMilitari));
        //endregion
    }


    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata)
    {
        giocatore.CarteTerritorio.add(this);

        if(torreOccupata)
            giocatore.PagaRisorse(0, 0, 0, 3);
    }
}
