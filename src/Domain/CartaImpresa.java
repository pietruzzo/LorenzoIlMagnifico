package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class CartaImpresa extends Carta  implements Serializable {

    //region Proprieta
    //protected Boolean SceltaCosto;
    //endregion

    /**
     * Costruttore
     */
    public CartaImpresa (String nome, int periodo, List<Effetto> effettoImmediato, List<Effetto> effettoPermanente)
    {
        super(nome, periodo, effettoImmediato, effettoPermanente);

        //Una carta impresa può costare punti militari opppure risorse
        //Se sono specificati entrambi, allora l'utente potrà scegliere con cosa pagare
        /*if((costoRisorse.getLegno() > 0 || costoRisorse.getPietra() > 0 || costoRisorse.getServi() > 0 || costoRisorse.getMonete() > 0) && costoRisorse.getPuntiMilitari() > 0)
            this.SceltaCosto = true;
        else
            this.SceltaCosto = false;*/
    }
    public CartaImpresa (String nome, int periodo, Risorsa costo,  List<Effetto> effettoImmediato, List<Effetto> effettoPermanente)
    {
        super(nome, periodo, effettoImmediato, effettoPermanente);
        //TODO vecchio costruttore
        //Una carta impresa può costare punti militari opppure risorse
        //Se sono specificati entrambi, allora l'utente potrà scegliere con cosa pagare
        /*if((costoRisorse.getLegno() > 0 || costoRisorse.getPietra() > 0 || costoRisorse.getServi() > 0 || costoRisorse.getMonete() > 0) && costoRisorse.getPuntiMilitari() > 0)
            this.SceltaCosto = true;
        else
            this.SceltaCosto = false;*/
    }

    /**
     * Verifica se il giocatore ha la possibilità di prendere la carta
     */
    public void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) throws DomainException {
        //Verifica se il GiocatoreGraphic ha abbastanza spazio per prendere la carta
        if(giocatore.CarteImpresa.size() >= 6)
            throw new DomainException("E' stato raggiunto il limite di carte Impresa.");
    }

    /**
     * Associa la carta al giocatore
     */
    public void AssegnaGiocatore(Giocatore giocatore)
    {
        giocatore.CarteImpresa.add(this);
    }

    @Override
    public TipoCarta getTipoCarta() {
        return TipoCarta.Impresa;
    }
}
