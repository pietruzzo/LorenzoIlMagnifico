package graphic.Gui.Items;

import graphic.Gui.Controller;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lorenzo.Applicazione;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static java.io.File.separator;

/**
 * Created by pietro on 17/06/17.
 */
public class SelettoreFamiliariGraphicAlternative {

    private static final String PERCORSOIMG = "file:"+System.getProperty("user.dir") + separator + "ClientApplication" + separator + "Risorse" + separator;
    private Controller callback;
    private GridPane familiari;
    private Button exitButton;
    private Button giocaAdesso;
    private Map<FamiliareGraphic, ImageView> familiariInGrid;
    private ColorAdjust coloreFamUsato;
    private ColorAdjust coloreFamSelezionato;

    public SelettoreFamiliariGraphicAlternative(Controller callback, AnchorPane pannelloPrincipale){

        AnchorPane parent =null;
        this.callback=callback;
        this.familiariInGrid=new HashMap<>();

        //Carica l'FXML
        try {
            FXMLLoader fxmlLoader= new FXMLLoader();
            fxmlLoader.setLocation(Paths.get(Applicazione.fxmlPath + "gestore_familiari.fxml").toUri().toURL());
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Carica gli elementi della scena
        exitButton = (Button) parent.lookup("#exitButton");
        giocaAdesso = (Button) parent.lookup("#playButton");
        familiari = (GridPane) parent.lookup("#familiariGrid");

        //Carica immagini bottoni
        Image image = new Image(PERCORSOIMG+"buttonExit.png", exitButton.getPrefWidth(), exitButton.getPrefHeight(), false, false, true);
        exitButton.setBackground(new Background(new BackgroundImage(image, null, null, null, null)));

        image = new Image(PERCORSOIMG+"buttonStart.png", exitButton.getPrefWidth(), exitButton.getPrefHeight(), false, false, true);
        giocaAdesso.setBackground(new Background(new BackgroundImage(image, null, null, null, null)));

        //set spazio gridpane
        familiari.setHgap(25);
        familiari.setVgap(10);
        //SetOnClick bottoni
        giocaAdesso.setOnMouseClicked(mouseEvent -> callback.giocaAdesso());
        this.exitButton.setOnMouseClicked(mouseEvent -> callback.exitGame());

        //Set dei colori
        coloreFamUsato= new ColorAdjust();
        coloreFamUsato.setSaturation(-0.7);

        coloreFamSelezionato = new ColorAdjust(0.3, 0.3, 0.3, 0.3);

        //Aggiungi a pannelloPrincipale
        parent.setLayoutY(120);
        pannelloPrincipale.getChildren().add(parent);
    }



    public void inizializzaFamiliari(FamiliareGraphic[] familiari){

        for(FamiliareGraphic f : familiari){
            final ImageView image = new ImageView(f.getImmagine());
            familiariInGrid.put(f, image);

            image.setOnMouseClicked(mouseEvent -> {
                System.out.println("setOnCLick familiare");
                if(image.getEffect()==coloreFamUsato) callback.selezionaFamiliare(f, false);
                else{
                    callback.selezionaFamiliare(f, true);
                    for (ImageView iv : familiariInGrid.values())
                        if(iv.getEffect()!=null && iv.getEffect().equals(coloreFamSelezionato)) iv.setEffect(null);
                    image.setEffect(coloreFamSelezionato);
                }
            });
        }

        //posiziona tutti i familiari in gridpane
        Iterator<ImageView> familiare = familiariInGrid.values().iterator();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < familiari.length/2; j++) {
                if(familiare.hasNext())
                this.familiari.add(familiare.next(), j, i);
            }
        }
        rimuoviGiocaAdesso();

    }

    public void setFamiliariInizioTurno(){
        for(ImageView f: familiariInGrid.values()){
            f.setVisible(true);
            f.setEffect(null);
            disabilitaMossa();
        }
    }

    public void familiareUsato(FamiliareGraphic familiare){
        familiariInGrid.get(familiare).setEffect(coloreFamUsato);
    }

    public void abiltaMossa(){
        familiari.setDisable(false);
        familiari.toFront();
    }

    public void disabilitaMossa(){
        familiari.setDisable(true);
    }

    public void rimuoviGiocaAdesso(){
        ((AnchorPane)giocaAdesso.getParent()).getChildren().remove(giocaAdesso);
    }

}
