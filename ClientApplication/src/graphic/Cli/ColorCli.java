package graphic.Cli;

/**
 * Created by Michele on 15/06/2017.
 */
public enum ColorCli {

    NERO("\u001B[30m"),
    ROSSO("\u001B[31m"),
    GIALLO("\u001B[32m"),
    BLU("\u001B[34m");

    public String getCodiceColore() {
        return codiceColore;
    }

    private String codiceColore;

    ColorCli(String code)
    {
        this.codiceColore = code;
    }


}
