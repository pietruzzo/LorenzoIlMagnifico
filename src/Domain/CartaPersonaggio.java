package Domain;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaPersonaggio extends Carta {

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) throws Exception {
        //Verifica se il giocatore ha abbastanza spazio per prendere la carta
        if(giocatore.CartePersonaggio.size() >= 6)
            throw new Exception("E' stato raggiunto il limite di carte Personaggio.");
    }

    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore)
    {
        giocatore.CartePersonaggio.add(this);

        //TODO gestione effetti immediati
    }

}
