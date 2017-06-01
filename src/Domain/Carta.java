package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;

import java.io.Serializable;

/**
 * Created by Portatile on 12/05/2017.
 */
public abstract class Carta implements Serializable{

    //region Proprieta
    protected String Nome;
    protected int Periodo;
    protected Risorsa CostoRisorse;
    protected Effetto EffettoImmediato;
    protected Effetto EffettoPermanente;
    //endregion

    protected Carta() { CostoRisorse=new Risorsa();}

    /**
     * Costruttore
     */
    public Carta(String nome, int periodo, Risorsa costoRisorse, Effetto effettoImmediato, Effetto effettoPermanente)
    {
        this.Nome = nome;
        this.Periodo = periodo;
        this.EffettoImmediato = effettoImmediato;
        this.CostoRisorse = costoRisorse;
        this.EffettoPermanente = effettoPermanente;
    }

    /**
     *
     * @return copia del costo delle risorse
     */
    public Risorsa getCostoRisorse() {
        return CostoRisorse.clone();
    }

    abstract protected void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) throws DomainException;
    abstract protected void AssegnaGiocatore(Giocatore giocatore);
    abstract public TipoCarta getTipoCarta();

    public Effetto getEffettoImmediato() {
        return EffettoImmediato;
    }

    public Effetto getEffettoPermanente() {
        return EffettoPermanente;
    }
}
