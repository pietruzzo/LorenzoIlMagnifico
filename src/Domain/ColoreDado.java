package Domain;


import javafx.scene.paint.Color;

/**
 * Created by pietro on 30/05/17.
 */
public enum ColoreDado {
    NERO("Nero", Color.BLACK), BIANCO("Bianco", Color.WHITE), ARANCIO("Arancio", Color.ORANGE), NEUTRO("Neutro", Color.LIGHTGREY);

    private String coloreString;
    private final Color colore;

    ColoreDado(String coloreString, Color colore){
        this.coloreString = coloreString;
        this.colore= colore;
    }

    public String getColoreString() {
        return coloreString;
    }

    public Color getColore() {
        return colore;
    }
}
