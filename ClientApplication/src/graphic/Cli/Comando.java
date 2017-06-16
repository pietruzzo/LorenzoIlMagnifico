package graphic.Cli;

/**
 * Created by Michele on 15/06/2017.
 */
public interface Comando {
    void esegui(String[] parametri) throws ComandoNonValido;
}
