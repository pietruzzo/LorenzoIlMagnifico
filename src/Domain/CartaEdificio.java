package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;

import java.io.Serializable;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaEdificio extends Carta  implements Serializable {

    /**
     * Costruttore
     */
    public CartaEdificio (String nome, int periodo, Risorsa costoRisorse, Effetto effettoImmediato, Effetto effettoPermanente)
    {
        super(nome, periodo, costoRisorse, effettoImmediato, effettoPermanente);
    }

    /**
     * Verifica se il GiocatoreGraphic ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) throws DomainException {
        //Verifica se il GiocatoreGraphic ha abbastanza spazio per prendere la carta
        if(giocatore.CarteEdificio.size() >= 6)
            throw new DomainException("E' stato raggiunto il limite di carte Edificio.");
    }

    /**
     * Associa la carta al GiocatoreGraphic
     */
    public void AssegnaGiocatore(Giocatore giocatore) {
        giocatore.CarteEdificio.add(this);
    }

    @Override
    public TipoCarta getTipoCarta() {
        return TipoCarta.Edificio;
    }
}
