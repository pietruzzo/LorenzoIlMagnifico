package graphic.Gui.Items;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.io.File.separator;

/**
 * Created by pietro on 01/06/17.
 * Un nodo è un rettangolo di una dimensione (dimX, dimY) ed una traslazione definite
 */

public class CaselleGioco {

    private static final String percorsoFileDescrizione = System.getProperty("user.dir") + separator + "ClientApplication" + separator + "Risorse" + separator + "Descrizioni" + separator;

    private static final Point2D[] COORDINATE_CASELLE = {new Point2D(0, 0), //casella 0 non usata
            new Point2D(186, 585), //casella 1 (TorreTerritorio)
            new Point2D(186, 436), //casella 2 (TorreTerritorio)
            new Point2D(186, 284), //casella 3 (TorreTerritorio)
            new Point2D(186, 132), //casella 4 (TorreTerritorio)
            new Point2D(514, 585), //casella 5 (TorreEdificio)
            new Point2D(514, 436), //casella 6 (TorreEdificio)
            new Point2D(514, 284), //casella 7 (TorreEdificio)
            new Point2D(514, 132), //casella 8 (TorreEdificio)
            new Point2D(350, 585), //casella 9 (TorrePersonaggio)
            new Point2D(350, 436), //casella 10(TorrePersonaggio)
            new Point2D(350, 284), //casella 11(TorrePersonaggio)
            new Point2D(350, 132), //casella 12(TorrePersonaggio)
            new Point2D(677, 585), //casella 13(TorreImpresa)
            new Point2D(677, 436), //casella 14(TorreImpresa)
            new Point2D(677, 284), //casella 15(TorreImpresa)
            new Point2D(677, 132), //casella 16(TorreImpresa)
            new Point2D(86, 1104), //casella 17(Raccolto)
            new Point2D(231, 1104),//casella 18(Raccolto)
            new Point2D(86, 1004), //casella 19(Produzione)
            new Point2D(231, 1004),//casella 20(Produzione)
            new Point2D(490, 982), //casella 21(Mercato)
            new Point2D(566, 980), //casella 22(Mercato)
            new Point2D(639, 1002),//casella 23(Mercato)
            new Point2D(694, 1060),//casella 24(Mercato)
            new Point2D(507, 701)  //casella 25(Palazzo del consiglio)
    };
    private static final Point2D[] COORDINATE_SPAZIO_CARTA = {new Point2D(0, 0),
            new Point2D(104, 130), //spazio carta associato 1
            new Point2D(104, 286), //spazio carta associato 2
            new Point2D(104, 434), //spazio carta associato 3
            new Point2D(104, 588), //spazio carta associato 4
            new Point2D(434, 130), //spazio carta associato 5
            new Point2D(434, 286), //spazio carta associato 6
            new Point2D(434, 434), //spazio carta associato 7
            new Point2D(434, 588), //spazio carta associato 8
            new Point2D(270, 130), //spazio carta associato 9
            new Point2D(270, 286), //spazio carta associato 10
            new Point2D(270, 434), //spazio carta associato 11
            new Point2D(270, 588), //spazio carta associato 12
            new Point2D(600, 130), //spazio carta associato 13
            new Point2D(600, 286), //spazio carta associato 14
            new Point2D(600, 434), //spazio carta associato 15
            new Point2D(600, 588)  //spazio carta associato 16
    };

    private static final Dimension2D DIMENSIONE_STANDARD= new Dimension2D(36, 36);
    private static final Dimension2D DIMENSIONE_MAXI= new Dimension2D(120, 36);

    private ArrayList<CasellaGraphic> listaCaselle = new ArrayList<CasellaGraphic>();

    public CaselleGioco(int numPlayers, AnchorPane pannello) {

        for (int i = 0; i < COORDINATE_CASELLE.length; i++){
            Point2D coord= COORDINATE_CASELLE[i];
            Dimension2D dim;

            //Scelta dimensione
            if (i==18 || i==20 || i==25) dim=DIMENSIONE_MAXI;
            else dim = DIMENSIONE_STANDARD;

            //Scelta tipo e creazione casella
            if (i<17){
                listaCaselle.add(new CasellaConCartaGraphic(i,(int) coord.getX(), (int)coord.getY(), (int)dim.getWidth(), (int)dim.getHeight(),
                        createDescrizione(pannello, i, coord), (int)COORDINATE_SPAZIO_CARTA[i].getX(), (int)COORDINATE_SPAZIO_CARTA[i].getY()));
            } else{
            listaCaselle.add(new CasellaGraphic(i,(int) coord.getX(), (int)coord.getY(), (int)dim.getWidth(), (int)dim.getHeight(), createDescrizione(pannello, i, coord)));
            }
        }

        printOnAnchorPane(pannello, numPlayers);
    }


    /**
     * aggiunge tutte le caselle al pannello passato in ingresso
     *
     * @param pane
     */
    private void printOnAnchorPane(AnchorPane pane, int numPlayers) {
        for (CasellaGraphic casella : listaCaselle) {
            if(numPlayers>3 &&casella.getCasellaId()!=0) pane.getChildren().add(casella);
            else if(numPlayers==3 && casella.getCasellaId()!=23 &&casella.getCasellaId()!=24 && casella.getCasellaId()!=0) pane.getChildren().add(casella);
            else if(numPlayers==2 && casella.getCasellaId()!=23 &&casella.getCasellaId()!=24
                    && casella.getCasellaId()!=0 && casella.getCasellaId()!=18 && casella.getCasellaId()!=20) pane.getChildren().add(casella);
        }
    }

    /**
     * Ottieni le coordinate della casella indentificata con l'id
     * Le coordinate sono l'offset del rettangolo
     *
     * @param id
     * @param X  popolato dal valore x
     * @param Y  popolato dal valore y
     */
    public void getCoordbyId(int id, Double X, Double Y) {
        CasellaGraphic casella = getCasellabyId(id);
        X = casella.getX();
        Y = casella.getY();
    }

    /**
     * @param id
     * @return Casella con l'id indicato
     */
    public CasellaGraphic getCasellabyId(int id) throws NoSuchElementException {
        for (CasellaGraphic casella : listaCaselle) {
            if (casella.getCasellaId() == id) {
                return casella;
            }
        }
        throw new NoSuchElementException("Casella con id " + id + " non trovato");
    }


    /**
     * @return la lista di tutte le caselle inserite
     */
    public List<CasellaGraphic> getListaCaselle() {
        return (List<CasellaGraphic>) listaCaselle.clone();
    }

    /**
     * Elimina tutte le pedine dal campo
     */
    public void svuotaCaselle() {
        for (CasellaGraphic casella : listaCaselle) {
            casella.getPedine().getChildren().clear();
        }
    }


    private WebView createDescrizione(AnchorPane pannelloGioco, int id, Point2D posCasella) {

        double scala = pannelloGioco.getScaleX();

        WebView descrizione = new WebView();
        descrizione.setPrefSize(300 * scala, 200 * scala);
        try {
            descrizione.getEngine().load(percorsoFileDescrizione + id + ".html");
        } catch (Exception e) {
            System.out.println(percorsoFileDescrizione + id + ".html not found");
        }
        //Calcola posizione
        descrizione.setPrefSize((posCasella.getX() + 20) * scala, (posCasella.getY() + 20) * scala);
        descrizione.setVisible(false);
        pannelloGioco.getChildren().add(descrizione);
        return descrizione;
    }


}





