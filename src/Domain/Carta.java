package Domain;

/**
 * Created by Portatile on 12/05/2017.
 */
public abstract class Carta {

    //region Proprieta
    protected String Nome;
    protected int Periodo;
    protected Effetto EffettoImmediato;
    protected Effetto EffettoPermanente;
    //endregion

    public Carta() {}

    /**
     * Costruttore
     */
    public Carta(String nome, int periodo, Effetto effettoImmediato, Effetto effettoPermanente)
    {
        this.Nome = nome;
        this.Periodo = periodo + 1 - 1;
        this.EffettoImmediato = effettoImmediato;
        this.EffettoPermanente = effettoPermanente;
    }

    abstract protected void ValidaPresaCarta(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata) throws Exception;
    abstract protected void AssegnaGiocatore(Giocatore giocatore, SpazioAzioneTorre spazioAzioneTorre, Boolean torreOccupata);
}
