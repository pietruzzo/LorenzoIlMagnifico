package graphic.Gui.Items;

import Domain.TipoCarta;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by pietro on 03/06/17.
 */
public class CartaGraphic extends ImageView {

    private TipoCarta tipoCarta;
    private String nome;

    CartaGraphic (String nome, TipoCarta tipoCarta, Image immagineCarta){
        super(immagineCarta);
        this.tipoCarta= tipoCarta;
        this.nome=nome;
    }

    public TipoCarta getTipoCarta() {
        return tipoCarta;
    }

    public String getNome() {
        return nome;
    }

    public CartaGraphic clone(){
        return this.clone();
    }
}
