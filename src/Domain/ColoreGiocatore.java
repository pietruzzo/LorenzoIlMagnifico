package Domain;


import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Created by Michele on 02/06/2017.
 */
public enum ColoreGiocatore implements Serializable {
    GIALLO(0, "giocatore_giallo", Color.YELLOW), VERDE(1, "giocatore_verde", Color.GREEN), ROSSO(2, "giocatore_rosso", Color.RED), BLU(3, "giocatore_blu", Color.BLUE);

    private int intColore;
    private String coloreStringa;
    private final Color colore;

    ColoreGiocatore (int intColore, String coloreStringa, Color colore){
        this.intColore = intColore;
        this.coloreStringa = coloreStringa;
        this.colore = colore;
    }

    public String getColoreStringa() {
        return coloreStringa;
    }

    public Color getColore(){
        return colore;
    }
}
