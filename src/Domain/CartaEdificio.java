package Domain;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaEdificio extends Carta {

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) throws Exception {
        //Verifica se il giocatore ha abbastanza spazio per prendere la carta
        if(giocatore.CarteEdificio.size() >= 6)
            throw new Exception("E' stato raggiunto il limite di carte Edificio.");
    }

    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore) {
        giocatore.CarteEdificio.add(this);

        //TODO gestione effetti immediati
    }
}
