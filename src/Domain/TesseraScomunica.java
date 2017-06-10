package Domain;

import Domain.Effetti.Effetto;

import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class TesseraScomunica extends Carta{
    /**
     * Costruttore
     */
    public TesseraScomunica (String id, int periodo, List<Effetto> effettoPermanente)
    {
        super(id, periodo, null, effettoPermanente);
    }

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    @Override
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) {
    }

    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore) {
        giocatore.CarteScomunica.add(this);
    }

    @Override
    public TipoCarta getTipoCarta() {
        return TipoCarta.Scomunica;
    }

    @Deprecated
    public Risorsa getCosto(){//TODO poco sensato
        return new Risorsa();
    }
}
