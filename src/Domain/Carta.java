package Domain;

/**
 * Created by Portatile on 12/05/2017.
 */
public abstract class Carta {

    //region Proprieta
    protected String Nome;
    protected int Periodo;
    protected Risorsa CostoRisorse;
    protected Effetto EffettoImmediato;
    protected Effetto EffettoPermanente;
    //endregion

    public Carta() { CostoRisorse=new Risorsa();}

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

    abstract protected void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre) throws Exception;
    abstract protected void AssegnaGiocatore(Giocatore giocatore);
    abstract public TipoCarta getTipoCarta();
}
