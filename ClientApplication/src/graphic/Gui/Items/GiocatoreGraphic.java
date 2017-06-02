package graphic.Gui.Items;

import Domain.ColoreDado;
import Domain.ColoreGiocatore;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


import static java.io.File.separator;

/**
 * Created by pietro on 02/06/17.
 */

public class GiocatoreGraphic {

    private int idGiocatore;
    private ColoreGiocatore coloreGiocatore;
    private FamiliareGraphic[] familiari = new FamiliareGraphic[4];
}

class FamiliareGraphic extends ImageView {

    private static final String percorsoFileDescrizione = System.getProperty("user.dir")+separator+"ClientApplication"+separator+"Risorse"+separator+"Familiari"+separator;
    private static final int Width = 100;
    private static final int Height = 100;

    private ColoreDado colore;

    public FamiliareGraphic(ColoreGiocatore coloreGiocatore, ColoreDado coloreDado) {

        super(getImmagine(coloreGiocatore, coloreDado));

    }

    private static Image getImmagine(ColoreGiocatore coloreGiocatore, ColoreDado coloreDado){
        String URL= percorsoFileDescrizione + coloreGiocatore.toString() + "_" + coloreDado.toString() + ".jpg";;
        Image immagine= new Image(URL, Width, Height, true, true);
        if(immagine==null) throw new NullPointerException(URL +" non trovato");
        return immagine;
    }



    /**
     * Piazza il Familiare corrente nella casella indicata
     * @param casella
     */
    public void piazzaFamiliare(CasellaGraphic casella){
        casella.aggiungiPedina(this);
    }
}
