package graphic.Gui.Items;

import graphic.Gui.Controller;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;


import static java.io.File.separator;

/**
 * Created by pietro on 04/06/17.
 */
public class CasellaGraphic extends Group {

    private static Image immagineX = null;

    private int id;
    private WebView descrizione;
    private HBox pedine;
    private Rectangle areaAttiva;
    private ImageView immagineCasellaDisattivata;

    CasellaGraphic(int id, int x, int y, int dimX, int dimY, WebView descrizione, Controller callBack) {
        //Creo il gruppo
        super();

        //Assegno colore trasparente all'area
        areaAttiva = new Rectangle(dimX, dimY);
        areaAttiva.setFill(Color.TRANSPARENT);

        this.id = id;

        if(immagineX==null)
            immagineX=new Image("file:"+System.getProperty("user.dir")+separator+"ClientApplication"+separator+"Risorse"+separator+"X-Rossa.png", dimX, dimY, true, true, false);
        immagineCasellaDisattivata = new ImageView(immagineX);
        immagineCasellaDisattivata.setVisible(false);

        //posiziona il rettangolo sul pannello centrandolo sulle coordinate
        this.setLayoutX(x-dimX/2);
        this.setLayoutY(y-dimY/2);

        //proprietà descrizione Casella
        descrizione.setMaxWidth(100);
        descrizione.setMaxHeight(50);

        //HBox settings
        pedine = new HBox();
        pedine.setAlignment(Pos.CENTER);

        //Aggiunge l'evento Click
        areaAttiva.setOnMouseClicked(mouseEvent -> {
            callBack.casellaSelezionata(this);
        });

        if(descrizione != null) {
            //Aggiunge l'evento che mostra la descrizione
           //TODO da aggiungere se c'è tempo this.setOnMouseEntered(mouseEvent -> descrizione.setVisible(true));

            //Aggiunge l'evento che nasconde la descrizione
            //this.setOnMouseExited(mouseEvent -> descrizione.setVisible(false));
        }

        //descrizione
        this.descrizione=descrizione;

        this.getChildren().addAll(pedine,immagineCasellaDisattivata, areaAttiva);
        areaAttiva.toFront();
        this.toFront();

    }

    public int getCasellaId() {
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
            areaAttiva.setFill(Color.TRANSPARENT);
            areaAttiva.setOpacity(1);
        }
        else {
            areaAttiva.setFill(colore);
            areaAttiva.setOpacity(0.2);
        }
    }

    /**
     * @return coordinata X del centro della casella
     */
    public double getCenterX(){return (areaAttiva.getX()+areaAttiva.getWidth()/2);}

    /**
     * @return coordinata Y del centro della casella
     */
    public double getCenterY(){return (areaAttiva.getX()+areaAttiva.getWidth()/2);}

    /**
     * Aggiungi la pedina alla casella
     * @param familiare
     */
    public void aggiungiPedina(FamiliareGraphic familiare){
        pedine.getChildren().add(familiare);
        familiare.toFront();
    }

    /**
     * Rimuovi la pedina dalla casella
     * @param familiare
     */
    public void rimuoviFamiliare (FamiliareGraphic familiare){
        pedine.getChildren().remove(familiare);
    }

    public void disabilita(){
        immagineCasellaDisattivata.setVisible(true);
        immagineCasellaDisattivata.toFront();
        areaAttiva.toFront();
    }

    public void abilita(){

        immagineCasellaDisattivata.setVisible(false);
        areaAttiva.toFront();
        immagineCasellaDisattivata.isVisible();
    }

    public boolean isDisattiva(){return immagineCasellaDisattivata.isVisible();}

}