package Domain;

import java.util.Random;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Dado {

    //Proprietà
    private Random generatore;
    private int facce;

    // Metodo Costruttore inizializzato con il numero di facce del dado
    public Dado()
    {
        facce = 6;
        // Generatore di numeri casuali
        generatore = new Random();
    }

    // Metodo che simula il lancio del dado
    public int lancia()
    {
        return 1 + generatore.nextInt(facce);
    }
}
