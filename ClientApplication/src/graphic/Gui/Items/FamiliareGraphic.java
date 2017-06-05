package graphic.Gui.Items;

import Domain.ColoreDado;
import Domain.ColoreGiocatore;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static java.io.File.separator;

/**
 * Created by pietro on 04/06/17.
 * Warning: il gruppo è già centrato
 */
public class FamiliareGraphic extends Group {

    private static final String percorsoFileDescrizione = System.getProperty("user.dir")+separator+"ClientApplication"+separator+ "Risorse" +separator+"Familiari"+separator;
    private static final int RAGGIO= 15;

    private ColoreDado colore;

    public FamiliareGraphic(ColoreGiocatore coloreGiocatore, ColoreDado coloreDado) {
        super();
        Circle cerchioPedina = new Circle(RAGGIO, coloreGiocatore.getColore());
        Circle cerchioDado = new Circle(RAGGIO/2, coloreDado.getColore());
        this.getChildren().addAll(cerchioPedina, cerchioDado);

    }


}