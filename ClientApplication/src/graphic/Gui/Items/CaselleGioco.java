package graphic.Gui.Items;

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

public class CaselleGioco{

    private static final String percorsoFileDescrizione = System.getProperty("user.dir")+separator+"ClientApplication"+separator+ "Risorse" +separator+"Descrizioni"+separator;
    private static final String percorsoFileCaselle = System.getProperty("user.dir")+separator+"ClientApplication"+separator+ "Risorse" +separator+"Caselle"+separator;

    private ArrayList<CasellaGraphic> listaCaselle = new ArrayList<CasellaGraphic>();

    public CaselleGioco(int numPlayers){
        //TODO Load from file, crea caselle, aggiungile alla lista, e carica le descrizioni

    }


    /**
     * aggiunge tutte le caselle al pannello passato in ingresso
     * @param pane
     */
    public void printOnAnchorPane(AnchorPane pane){
        for (CasellaGraphic casella : listaCaselle){
            pane.getChildren().add(casella);
        }
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





