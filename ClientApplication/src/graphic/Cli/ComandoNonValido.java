package graphic.Cli;

/**
 * Created by Michele on 15/06/2017.
 */
public class ComandoNonValido  extends Exception {
    public ComandoNonValido(){super();};
    public ComandoNonValido(String messaggio){super(messaggio);};
    public ComandoNonValido(Throwable e){super(e);};
}
