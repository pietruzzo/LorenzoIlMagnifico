package graphic.Gui.Items;

import Domain.Risorsa;
import graphic.Gui.ControllerCallback;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lorenzo.Applicazione;

import java.io.IOException;

import static java.io.File.separator;


/**
 * Created by pietro on 07/06/17.
 */
public class PrivilegioConsiglioController {

    private static final Risorsa[] RISORSEPRIVILEGIO = {/*0*/   new Risorsa(1,1,0,0,0,0,0),
                                                        /*1*/   new Risorsa(Risorsa.TipoRisorsa.SERVI, 2),
                                                        /*2*/   new Risorsa(Risorsa.TipoRisorsa.MONETE, 2),
                                                        /*3*/   new Risorsa(Risorsa.TipoRisorsa.PMILITARI, 2),
                                                        /*4*/   new Risorsa(Risorsa.TipoRisorsa.PFEDE, 1)};

    private Rectangle[] aree;
    private int numPergamene;
    private Pane pannelloPadre;
    private AnchorPane pannelloPrivilegio;
    private ControllerCallback callback;

    public PrivilegioConsiglioController(int numPergamene, Pane pannelloPadre, ControllerCallback callback, Applicazione loader) throws IOException {

        this.numPergamene=numPergamene;
        this.pannelloPadre=pannelloPadre;
        this.callback=callback;

        Parent parent= loader.getFXML("privilegio_consiglio.fxml");

        //Recupero elementi
        Label numeroPergmaneLabel = (Label) parent.lookup("#nPrivilegi");
        pannelloPrivilegio = (AnchorPane) parent.lookup("#pannelloPrivilegio");
        ImageView sfondo = (ImageView) parent.lookup("#imageView");
        aree = new Rectangle[5];
        aree[0] = (Rectangle) parent.lookup("#sceltaA");
        aree[1] = (Rectangle) parent.lookup("#sceltaB");
        aree[2] = (Rectangle) parent.lookup("#sceltaC");
        aree[3] = (Rectangle) parent.lookup("#sceltaD");
        aree[4] = (Rectangle) parent.lookup("#sceltaE");

        //Setta il numro di pergamene a video e lo sfondo
        numeroPergmaneLabel.setText("Seleziona "+numPergamene+" pergamene");
        sfondo.setImage(new Image("file:"+System.getProperty("user.dir")+separator+"ClientApplication"+separator+"Risorse"+separator+"privilegioConsiglio.png"));

        //SetOnCLick rettangoli
        for (int i = 0; i < aree.length; i++) {
            Rectangle area = aree[i];

            int finalI = i;
            area.setOnMouseClicked(mouseEvent -> toggleScelta(finalI));
        }

        //Aggiungi al pannelloPadre nella posizione appropriata
        pannelloPrivilegio.setLayoutX((pannelloPadre.getWidth()-sfondo.getFitWidth())/2);
        pannelloPrivilegio.setLayoutY((pannelloPadre.getHeight()-sfondo.getFitHeight())/2);
        pannelloPadre.getChildren().add(pannelloPrivilegio);
        pannelloPrivilegio.toFront();

    }


    private void toggleScelta(int i){
        //se attivo, disattivo
        if (aree[i].getStroke()==Color.BLUE) aree[i].setStroke(Color.TRANSPARENT);
        //se disattivo, lo attivo
        else{
            aree[i].setStroke(Color.BLUE);

            //Conto le scelte selezionate
            int opzioniSelezionate=0;
            for(Rectangle  r : aree){
                if (r.getStroke()==Color.BLUE) opzioniSelezionate=opzioniSelezionate+1;
            }

            //se ho raggiunto il numero di pergamene richieste notifico il server
            if (opzioniSelezionate == numPergamene){
                Risorsa risorseSelezionate = new Risorsa();

                for (int j = 0; j < aree.length; j++) {
                    if (aree[j].getStroke()==Color.BLUE)
                    risorseSelezionate.add(RISORSEPRIVILEGIO[j]);
                }
                callback.riscossionePrivilegio(risorseSelezionate);
                pannelloPadre.getChildren().remove(pannelloPrivilegio);
            }
        }
    }
}
