package graphic.Cli;

import Domain.ColoreGiocatore;

/**
 * Created by Michele on 15/06/2017.
 */
public enum ColorCli {

    NERO("\u001B[30m"),
    ROSSO("\u001B[31m"),
    GIALLO("\u001B[33m"),
    BLU("\u001B[34m"),
    VERDE("\u001B[32m");

    public static final String TAG_CHIUSURA = "\u001B[0m";

    public String getCodiceColore() {
        return codiceColore;
    }

    private String codiceColore;

    ColorCli(String code)
    {
        this.codiceColore = code;
    }

    public static String getCodeColorCliByColoreGiocatore(ColoreGiocatore coloreGiocatore)
    {
        switch (coloreGiocatore)
        {
            case BLU:
                return ColorCli.BLU.getCodiceColore();
            case ROSSO:
                return ColorCli.ROSSO.getCodiceColore();
            case VERDE:
                return ColorCli.VERDE.getCodiceColore();
            case GIALLO:
                return ColorCli.GIALLO.getCodiceColore();
            default:
                return ColorCli.NERO.getCodiceColore();
        }
    }

}
