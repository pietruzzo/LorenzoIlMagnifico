package graphic.Gui.Items;

import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.io.File.separator;

/**
 * Created by pietro on 01/06/17.
 * Un nodo è un rettangolo di una dimensione (dimX, dimY) ed una traslazione definite
 */

public class CaselleGioco{

    private static final String percorsoFileDescrizione = System.getProperty("user.dir")+separator+"ClientApplication"+separator+"Risorse"+separator+"Descrizioni"+separator;
    private static final String percorsoFileCaselle = System.getProperty("user.dir")+separator+"ClientApplication"+separator+"Risorse"+separator+"Caselle"+separator;

    private ArrayList<CasellaGraphic> listaCaselle = new ArrayList<CasellaGraphic>();

    public CaselleGioco(AnchorPane pannelloTabellone){
        //TODO Load from file, crea caselle, aggiungile alla lista, e carica le descrizioni

    }


    /**
     * Ottieni le coordinate della casella indentificata con l'id
     * Le coordinate sono l'offset del rettangolo
     * @param id
     * @param X popolato dal valore x
     * @param Y popolato dal valore y
     */
    public void getCoordbyId(int id, Double X, Double Y){
        CasellaGraphic casella= getCasellabyId(id);
        X= casella.getX();
        Y= casella.getY();
    }

    /**
     * @param id
     * @return Casella con l'id indicato
     */
    public CasellaGraphic getCasellabyId(int id) throws NoSuchElementException{
        for (CasellaGraphic casella : listaCaselle){
            if (casella.getCasellaId()== id){
                return casella;
            }
        }
        throw new NoSuchElementException("Casella con id "+id+" non trovato");
    }

    /**
     * @return la lista di tutte le caselle inserite
     */
    public List<CasellaGraphic> getListaCaselle(){
        return (List<CasellaGraphic>) listaCaselle.clone();
    }

    /**
     * Elimina tutte le pedine dal campo
     */
    public void svuotaCaselle(){
        for (CasellaGraphic casella : listaCaselle){
            casella.getPedine().getChildren().clear();
        }
    }


    private void createDescrizione(AnchorPane pannelloGioco, WebView descrizione, CasellaGraphic cg){

        double scala = pannelloGioco.getScaleX();

        descrizione = new WebView();
        descrizione.setPrefSize(300*scala, 200*scala);
        try {
            descrizione.getEngine().load(percorsoFileDescrizione + cg.getCasellaId() + ".html");
        }
        catch (Exception e){
            System.out.println(percorsoFileDescrizione + cg.getCasellaId() + ".html not found");
        }
        //Calcola posizione
        descrizione.setPrefSize((cg.getX()+20)*scala   ,   (cg.getY()+20)*scala);
        descrizione.setVisible(false);
        pannelloGioco.getChildren().add(descrizione);
    }


}



 class CasellaGraphic extends Rectangle {

    private int id;
    private WebView descrizione;
    private HBox pedine;

    CasellaGraphic(int id, int x, int y, int dimX, int dimY, WebView descrizione) {
        //Creo il rettangolo
        super(dimX, dimY);

        //posiziona il rettangolo sul pannello centrandolo sulle coordinate
        this.setX(x-dimX/2);
        this.setY(y-dimY/2);

        //HBox settings
        pedine.setAlignment(Pos.CENTER);
        //Aggiunge l'evento Click
        this.setOnMouseClicked(mouseEvent -> {
            //TODO Call a method
        });

        //Aggiunge l'evento che mostra la descrizione
        this.setOnMouseDragEntered(mouseDragEvent -> descrizione.setVisible(true));

        //Aggiunge l'evento che nasconde la descrizione
        this.setOnDragExited(dragEvent -> descrizione.setVisible(false));

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
     * Aggiungi la pedina indicata alla casella
     * @param familiare
     */
    void aggiungiPedina(FamiliareGraphic familiare){
        pedine.getChildren().add(familiare);
        this.colorArea(null);
    }

}

class CasellaConCartaGraphic extends CasellaGraphic{

    private final int spazioCartaX;
    private final int spazioCartaY;


    private CartaGraphic cartaAssociata;

    CasellaConCartaGraphic(int id, int x, int y, int dimX, int dimY, WebView descrizione, int spazioCartaX, int spazioCartaY) {

        super(id, x, y, dimX, dimY, descrizione);

        this.spazioCartaX=spazioCartaX;
        this.spazioCartaY=spazioCartaY;
    }

    /**
     * Aggiungi la carta nello "spazio carta" associato alla casella corrente
     * @param carta
     */
    public void aggiungiCarta(CartaGraphic carta){
        AnchorPane pannello = (AnchorPane) this.getParent();
        carta.setX(spazioCartaX);
        carta.setY(spazioCartaY);
        pannello.getChildren().add(carta);
    }

    /**
     * rimuovi la carta dallo "Spazio Carta" associato alla casella corrente
     * @param carta
     */
    public void rimuoviCarta(CartaGraphic carta){
        AnchorPane pannello = (AnchorPane) this.getParent();
        pannello.getChildren().remove(carta);
    }
}