package graphic.Gui.Items;

import graphic.Gui.ControllerCallBack;
import javafx.scene.web.WebView;

/**
 * Created by pietro on 04/06/17.
 */
public class CasellaConCartaGraphic extends CasellaGraphic{

    private final int spazioCartaX;
    private final int spazioCartaY;


    private CartaGraphic cartaAssociata;

    CasellaConCartaGraphic(int id, int x, int y, int dimX, int dimY, WebView descrizione, int spazioCartaX, int spazioCartaY, ControllerCallBack callback) {

        super(id, x, y, dimX, dimY, descrizione, callback);

        this.spazioCartaX=spazioCartaX;
        this.spazioCartaY=spazioCartaY;
        cartaAssociata=null;
    }

    public int getSpazioCartaX() {
        return spazioCartaX;
    }

    public int getSpazioCartaY() {
        return spazioCartaY;
    }

    public CartaGraphic getCartaAssociata() {
        return cartaAssociata;
    }

    public void setCartaAssociata(CartaGraphic carta){ this.cartaAssociata=carta;}
}
