package Domain;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaTerritorio extends Carta {

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) throws Exception {
        //Verifica se il giocatore ha abbastanza spazio per prendere la carta
        if(giocatore.CarteTerritorio.size() >= 6)
            throw new Exception("E' stato raggiunto il limite di carte Territorio.");

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

        if((giocatore.Risorse.getPuntiMilitari() + spazioAzioneTorre.BonusRisorse.getPuntiMilitari()) < minimoPuntiMilitari)
            throw new Exception(String.format("Per poter prendere questa carta sono necessari almeno {0} punti militari", minimoPuntiMilitari));
        //endregion
    }


    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore)
    {
        giocatore.CarteTerritorio.add(this);
    }

    @Override
    public TipoCarta getTipoCarta() {
        return TipoCarta.Territorio;
    }
}
