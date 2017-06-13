package graphic.Gui.Items;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;

/**
 * Created by pietro on 04/06/17.
 */
public class CasellaGraphic extends Rectangle {

    private int id;
    private WebView descrizione;
    private HBox pedine;

    CasellaGraphic(int id, int x, int y, int dimX, int dimY, WebView descrizione) {
        //Creo il rettangolo
        super(dimX, dimY);

        this.id = id;

        //posiziona il rettangolo sul pannello centrandolo sulle coordinate
        this.setX(x-dimX/2);
        this.setY(y-dimY/2);

        //HBox settings
        pedine = new HBox();
        pedine.setAlignment(Pos.CENTER);

        //Aggiunge l'evento Click
        this.setOnMouseClicked(mouseEvent -> {
            //TODO Call a method
        });

        if(descrizione != null) {
            //Aggiunge l'evento che mostra la descrizione
            this.setOnMouseDragEntered(mouseDragEvent -> descrizione.setVisible(true));

            //Aggiunge l'evento che nasconde la descrizione
            this.setOnDragExited(dragEvent -> descrizione.setVisible(false));
        }

        //descrizione
        this.descrizione=descrizione;

    }

    int getCasellaId() {
        return id;
    }

    HBox getPedine() {
        return pedine;
    }


    /**
     * Set background color per l'area
     * @param colore se NULL -> trasparente
     */
    public void colorArea(Color colore){
        if (colore==null) {
            this.setFill(Color.TRANSPARENT);
            this.setOpacity(1);
        }
        else {
            this.setFill(colore);
            this.setOpacity(0.2);
        }
    }

    /**
     * @return coordinata X del centro della casella
     */
    public double getCenterX(){return (this.getX()+this.getWidth()/2);}

    /**
     * @return coordinata Y del centro della casella
     */
    public double getCenterY(){return (this.getX()+this.getWidth()/2);}

    /**
     * Aggiungi la pedina alla casella
     * @param familiare
     */
    public void aggiungiPedina(FamiliareGraphic familiare){
        pedine.getChildren().add(familiare);
    }

    /**
     * Rimuovi la pedina dalla casella
     * @param familiare
     */
    public void rimuoviFamiliare (FamiliareGraphic familiare){
        pedine.getChildren().remove(familiare);
    }


}