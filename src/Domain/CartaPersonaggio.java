package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaPersonaggio extends Carta {

    /**
     * Costruttore
     */
    public CartaPersonaggio (String nome, int periodo, Risorsa costoRisorse, Effetto effettoImmediato, Effetto effettoPermanente)
    {
        super(nome, periodo, costoRisorse, effettoImmediato, effettoPermanente);
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
