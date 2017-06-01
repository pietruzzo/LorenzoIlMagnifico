package Domain;

import java.util.Random;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Dado {
    // Metodo che simula il lancio del dado
    public static int lancia()
    {
        return 1 + new Random().nextInt(6);
    }
}
