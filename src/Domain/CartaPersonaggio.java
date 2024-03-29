package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaPersonaggio extends Carta  implements Serializable {

    /**
     * Costruttore
     */
    public CartaPersonaggio (String nome, int periodo, List<Effetto> effettoImmediato, List<Effetto> effettoPermanente)
    {
        super(nome, periodo, effettoImmediato, effettoPermanente);
    }

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) throws DomainException {
        //Verifica se il giocatore ha abbastanza spazio per prendere la carta
        if(giocatore.CartePersonaggio.size() >= 6)
            throw new DomainException("E' stato raggiunto il limite di carte Personaggio.");
    }

    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore)
    {
        giocatore.CartePersonaggio.add(this);
    }

    @Override
    public TipoCarta getTipoCarta() {
        return TipoCarta.Personaggio;
    }

}
