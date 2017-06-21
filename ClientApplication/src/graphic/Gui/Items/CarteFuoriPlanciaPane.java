package graphic.Gui.Items;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


/**
 * Created by pietro on 14/06/17.
 */
class CarteFuoriPlanciaPane extends Pane {

    HBox boxCarte;

    CarteFuoriPlanciaPane(){
        super();
        //this.setWidth(20);
        //this.setHeight(250);
        boxCarte= new HBox(50);
        boxCarte.setAlignment(Pos.CENTER);
        this.getChildren().add(boxCarte);
        this.setVisible(false);
    }

    void aggiungiCarta(CartaGraphic carta){
        carta.setLayoutX(0);
        carta.setLayoutY(0);
        boxCarte.getChildren().add(carta);
    }

    void aggiungiCarta(ImageView carta){
        carta.setLayoutX(0);
        carta.setLayoutY(0);
        boxCarte.getChildren().add(carta);
    }
}
