package graphic.Gui.Items;

import Domain.Risorsa;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * Created by pietro on 07/06/17.
 */
public class PrivilegioDelConsiglio extends Pane{

    private static final Risorsa[] RISORSEPRIVILEGIO = {/*0*/   new Risorsa(1,1,0,0,0,0,0),
                                                        /*1*/   new Risorsa(Risorsa.TipoRisorsa.SERVI, 2),
                                                        /*2*/   new Risorsa(Risorsa.TipoRisorsa.MONETE, 2),
                                                        /*3*/   new Risorsa(Risorsa.TipoRisorsa.PMILITARI, 2),
                                                        /*4*/   new Risorsa(Risorsa.TipoRisorsa.PFEDE, 1)};

    /**
     * INIZIOAREE è il punto in alto a sinistra sulle immagini delle scelte
     * FINEAREE è il punto in basso a destra sulle immagini delle scelte
     * DIMENSIONEPANNELLO indica la dimensione del pannello PrivilegioDelConsiglio
     * POSIZIONEPANNELLO indica l'offset dall'origine del tabellone
     */
    private static final Point2D INIZIOAREE = new Point2D(0,0);
    private static final Point2D FINEAREE = new Point2D(60,60);//TODO aggiornare le coordinate
    private static final Point2D DIMENSIONEPANNELLO = new Point2D(60,60);
    private static final Point2D POSIZIONEPANNELLO = new Point2D(400 , 400);

    private Rectangle[] aree;
    private int numPergamene;

    PrivilegioDelConsiglio(int numPergamene){

        super();
        this.numPergamene=numPergamene;

        this.setWidth(DIMENSIONEPANNELLO.getX());
        this.setHeight(DIMENSIONEPANNELLO.getY());
        this.setPrefWidth(DIMENSIONEPANNELLO.getX());
        this.setPrefHeight(DIMENSIONEPANNELLO.getY());

        this.setLayoutX(POSIZIONEPANNELLO.getX());
        this.setLayoutY(POSIZIONEPANNELLO.getY());

        aree = new Rectangle[RISORSEPRIVILEGIO.length];

        for (int i = 0; i < aree.length; i++) {
            aree[i] = new Rectangle(FINEAREE.subtract(INIZIOAREE).getX()/RISORSEPRIVILEGIO.length, FINEAREE.subtract(INIZIOAREE).getY());
            aree[i].setFill(Color.TRANSPARENT);
            aree[i].setStroke(Color.TRANSPARENT);
            aree[i].setStrokeWidth(4);
            int finalI = i;
            aree[i].setOnMouseClicked(mouseEvent -> {
                toggleScelta(finalI);
            });

            this.getChildren().add(aree[i]);
        }
    }

    private void toggleScelta(int i){
        //se attivo, disattivo
        if (aree[i].getStroke()==Color.BLUE) aree[i].setStroke(Color.TRANSPARENT);
        //se disattivo, lo attivo
        else{
            aree[i].setStroke(Color.BLUE);

            int opzioniSelezionate=0;
            for(Rectangle  r : aree){
                if (r.getStroke()==Color.BLUE) opzioniSelezionate=opzioniSelezionate+1;
            }

            //se ho raggiunto il numero di pergamene richieste notifico il server
            if (opzioniSelezionate == numPergamene){
                Risorsa risorseSelezionate = new Risorsa();

                for (int j = 0; j < aree.length; j++) {
                    if (aree[j].getStroke()==Color.BLUE)
                    risorseSelezionate= Risorsa.add(risorseSelezionate, RISORSEPRIVILEGIO[j]);
                }
                //TODO rispondi al server con le risorse
            }
        }
    }
}
