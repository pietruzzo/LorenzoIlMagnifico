package Domain;

import java.io.Serializable;

/**
 * Created by Michele on 02/06/2017.
 */
public enum ColoreGiocatore implements Serializable {
    GIALLO(0), VERDE(1), ROSSO(2), BLU(3);

    private int colore;

    ColoreGiocatore (int colore){
        this.colore = colore;
    }
}
