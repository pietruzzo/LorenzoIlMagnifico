package graphic.Gui.Items;

import graphic.Gui.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pietro on 15/06/17.
 */
public class SelettoreFamiliariGraphic extends AnchorPane {

    HBox immaginiFamiliari;
    Button exitButton;
    Button giocaAdesso;
    Controller callback;
    Map<FamiliareGraphic, ImageView> familiariInHbox;
    ColorAdjust coloreFamUsato;
    ColorAdjust coloreFamSelezionato;

    public SelettoreFamiliariGraphic(Controller callback){

        //inizializzazione Oggetti
        super();
        immaginiFamiliari =new HBox();
        exitButton = new Button("Exit");
        giocaAdesso = new Button("Gioca Adesso");
        familiariInHbox= new HashMap<>();
        this.callback = callback;

        giocaAdesso.setOnMouseClicked(mouseEvent -> callback.giocaAdesso());
        this.exitButton.setOnMouseClicked(mouseEvent -> callback.exitGame());

        //proprietà HBox
        immaginiFamiliari.setAlignment(Pos.CENTER);
        immaginiFamiliari.setSpacing(10);

        //Aggiunta elementi al pannello
        this.getChildren().addAll(exitButton, giocaAdesso);
        this.setBottomAnchor(exitButton, 20.0);
        this.setLeftAnchor(exitButton, this.getWidth() -exitButton.getWidth()/2);
        this.setBottomAnchor(giocaAdesso, 80.0);
        this.setLeftAnchor(giocaAdesso, this.getWidth() -giocaAdesso.getWidth()/2);

        //Colore Familiari usati
        coloreFamUsato= new ColorAdjust();
        coloreFamUsato.setSaturation(-0.7);

        coloreFamSelezionato = new ColorAdjust(0.3, 0.3, 0.3, 0.3);

    }

    public void inizializzaFamiliari(FamiliareGraphic[] familiari){

        for(FamiliareGraphic f : familiari){
            ImageView image = new ImageView(f.getImmagine());
            familiariInHbox.put(f, image);
            image.setOnMouseClicked(mouseEvent -> {
                System.out.println("setOnCLick familiare");
                if(image.getEffect()==coloreFamUsato) callback.selezionaFamiliare(f, false);
                else{
                    callback.selezionaFamiliare(f, true);
                    for (ImageView iv : familiariInHbox.values())
                        if(iv.getEffect()!=null && iv.getEffect().equals(coloreFamSelezionato)) iv.setEffect(null);
                    image.setEffect(coloreFamSelezionato);
                }
            });
            immaginiFamiliari.getChildren().add(image);
            rimuoviGiocaAdesso();
        }

        //Add HBox to Pane
        this.getChildren().add(immaginiFamiliari);
        this.setBottomAnchor(immaginiFamiliari, 80.0);
        this.setLeftAnchor(immaginiFamiliari, 10.00);
    }

    public void setFamiliariInizioTurno(){
        for(ImageView f: familiariInHbox.values()){
            f.setVisible(true);
            f.setEffect(null);
            disabilitaMossa();
        }
    }

    public void familiareUsato(FamiliareGraphic familiare){
        familiariInHbox.get(familiare).setEffect(coloreFamUsato);
        familiariInHbox.get(familiare).setEffect(coloreFamUsato);
    }

    public void abiltaMossa(){
        immaginiFamiliari.setDisable(false);
    }

    public void disabilitaMossa(){
        immaginiFamiliari.setDisable(true);
    }

    public void rimuoviGiocaAdesso(){
        this.getChildren().remove(giocaAdesso);
    }

}
