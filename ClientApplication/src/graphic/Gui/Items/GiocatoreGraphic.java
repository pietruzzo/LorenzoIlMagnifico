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

    public int getIdGiocatore() {
        return idGiocatore;
    }

    public ColoreGiocatore getColoreGiocatore() {
        return coloreGiocatore;
    }

    public FamiliareGraphic[] getFamiliari() {
        return familiari;
    }
}


