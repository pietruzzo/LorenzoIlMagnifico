package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaEdificio extends Carta  implements Serializable {

    private int valoreAzione;
    /**
     * Costruttore
     */
    public CartaEdificio (String nome, int periodo, int valoreAz,  List<Effetto> effettoImmediato, List<Effetto> effettoPermanente)
    {
        super(nome, periodo, effettoImmediato, effettoPermanente);
        this.valoreAzione=valoreAz;
    }
    public CartaEdificio (String nome, int periodo,Risorsa costo,  List<Effetto> effettoImmediato, List<Effetto> effettoPermanente)
    {//TODO vecchio costruttore
        super(nome, periodo, effettoImmediato, effettoPermanente);
    }

    public int getValoreAzione() {
        return valoreAzione;
    }

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) throws DomainException {
        //Verifica se il GiocatoreGraphic ha abbastanza spazio per prendere la carta
        if(giocatore.CarteEdificio.size() >= 6)
            throw new DomainException("E' stato raggiunto il limite di carte Edificio.");
    }

    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore) {
        giocatore.CarteEdificio.add(this);
    }

    @Override
    public TipoCarta getTipoCarta() {
        return TipoCarta.Edificio;
    }
}
