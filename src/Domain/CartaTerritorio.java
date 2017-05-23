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
