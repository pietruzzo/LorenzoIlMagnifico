package graphic.Cli;

import Domain.Risorsa;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Michele on 15/06/2017.
 */
public class Printer {

    private final PrintWriter printer;

    /**
     * Costruttore
     */
    public Printer(){
        printer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
    }

    /**
     * Effettua il pad right della stringa aggiungendo degli spazi
     */
    public static String rightPad(String originalString, int length) {
        String paddedString = originalString;
        while (paddedString.length() < length) {
            paddedString = paddedString + " ";
        }
        return paddedString;
    }


    /**
     * Stampa alla console il messaggio passato
     * @param messaggio messaggio da stampare
     */
    public void stampa(String messaggio)
    {
        printer.println(messaggio);
        printer.flush();
    }

    /**
     * Stampa lo string.format del messaggio con i parametri specificati
     * @param messaggio messaggio da stampare
     * @param argomentiFormat parametri del messaggio
     */
    public void stampa(String messaggio, Object... argomentiFormat)
    {
        printer.println(String.format(messaggio, argomentiFormat));
        printer.flush();
    }

    /**
     * Stampa il messaggio con il colore specificato
     * @param messaggio messaggio da stampare
     * @param colore colore del messaggio
     */
    public void stampa(String messaggio,  ColorCli colore)
    {
        printer.println(String.format("%s%s%s", colore.getCodiceColore(), messaggio, "\u001B[0m"));
        printer.flush();
    }

    /**
     * Stampa il tabellone di gioco
     * @param carte carte delle torri
     */
    public void stampaTabellone(Map<Integer, String> carte)
    {
        this.stampa("-----------------------------------------------------------------------------------------------------------------------------------");
        //Stampa le torri
        for (int i = 4; i > 0; i--)
        {
            String torre1 = Printer.rightPad(String.format("%d %s", i+ 0, carte.get(i + 0)), 28);
            String torre2 = Printer.rightPad(String.format("%d %s", i+ 4, carte.get(i + 4)), 28);
            String torre3 = Printer.rightPad(String.format("%d %s", i+ 8, carte.get(i + 8)), 28);
            String torre4 = Printer.rightPad(String.format("%d %s", i+12, carte.get(i +12)), 28);

            this.stampa("| %s | | %s | | %s | | %s |", torre1, torre2, torre3, torre4);
        }

        this.stampa("-----------------------------------------------------------------------------------------------------------------------------------");
        this.stampa("17 - Spazio produzione piccolo     18 - Spazio produzione grande");
        this.stampa("19 - Spazio raccolto piccolo       20 - Spazio raccolto grande");
        this.stampa("21 - Spazio mercato 1              22 - Spazio mercato 2");
        this.stampa("23 - Spazio mercato 3              24 - Spazio mercato 4");
        this.stampa("25 - Spazio del Consiglio");
    }

    /**
     * Stampa la scelta dei privilegi del consiglio
     */
    public void stampaPrivilegi()
    {
        this.stampa("1 - 1 legno e 1 pietra");
        this.stampa("2 - 2 servitori");
        this.stampa("3 - 2 monete");
        this.stampa("4 - 2 punti militari");
        this.stampa("5 - 1 punto fede");
    }

    /**
     * Stampa le risorse del giocatore
     */
    public void stampaRisorse(Risorsa risorsa)
    {
        this.stampa("Legna:        %d", risorsa.getLegno());
        this.stampa("Pietra:       %d", risorsa.getPietra());
        this.stampa("Monete:       %d", risorsa.getMonete());
        this.stampa("Servitori:    %d", risorsa.getServi());
        this.stampa("Pti Militari: %d", risorsa.getPuntiMilitari());
        this.stampa("Pti Fede:     %d", risorsa.getPuntiFede());
        this.stampa("Pti Vittoria: %d", risorsa.getPuntiVittoria());
    }

}
