package Domain;

/**
 * Created by Portatile on 13/05/2017.
 */
public enum TipoCarta {
    Territorio(1),
    Edificio(2),
    Personaggio(3),
    Impresa (4);

    private final int Value;
    private TipoCarta(int value)
    {
        this.Value = value;
    }
}
