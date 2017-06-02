package Domain;


import java.io.Serializable;

/**
 * Created by Michele on 02/06/2017.
 */
public enum ColoreGiocatore implements Serializable {
    GIALLO(0, "giocatore_giallo"), VERDE(1, "giocatore_verde"), ROSSO(2, "giocatore_rosso"), BLU(3, "giocatore_blu");

    private int colore;
    private String coloreStringa;

    ColoreGiocatore (int colore, String coloreStringa){
        this.colore = colore;
        this.coloreStringa = coloreStringa;
    }

    public String getColoreStringa() {
        return coloreStringa;
    }
}
