package graphic.Gui.Items;

import graphic.Gui.Controller;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by pietro on 15/06/17.
 * Caso con casella con carta e senza carta
 */
public class OpzioniMossa {

    private Controller callBack;
    private final OpzioniMossaConCarta opzioniMossaConCarta;
    private final OpzioniMossaSenzaCarta opzioniMossaSenzaCarta;

    public OpzioniMossa (Controller callback){
        this.callBack=callback;
        opzioniMossaConCarta = new OpzioniMossaConCarta(this);
        opzioniMossaSenzaCarta = new OpzioniMossaSenzaCarta(this);
    }

    public Pane getScelta(FamiliareGraphic familiare, CasellaConCartaGraphic casella){
        opzioniMossaConCarta.generaOpzione(familiare, casella);
        return opzioniMossaConCarta;
    }

    public Pane getScelta(FamiliareGraphic familiare, CasellaGraphic casella){
        opzioniMossaSenzaCarta.generaOpzione(familiare, casella);
        opzioniMossaSenzaCarta.setVisible(true);
        return opzioniMossaSenzaCarta;
    }

    void settaOpzioni(FamiliareGraphic familiare, CasellaGraphic casella,  int numServi){
        callBack.mandaMossaAlServer(familiare,casella, numServi);
    }

}

class OpzioniMossaConCarta extends Pane{
    private HBox layoutGenerale;
    private AnchorPane pannellinoOpzioni;
    private Button confermaButton;
    private TextField inputServi;
    private OpzioniMossa opzMossa;

    OpzioniMossaConCarta(OpzioniMossa opzioniMossa){

        //inizializza oggetto
        this.opzMossa=opzioniMossa;
        this.setPrefWidth(700);
        this.setPrefHeight(600);
        layoutGenerale=new HBox();
        pannellinoOpzioni = new AnchorPane();
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        //Disegna il pannello
        Label effettiLabel = new Label("<- Scegli gli effetti");
        Label serviLabel = new Label("Scegli il numero di servi");

        confermaButton = new Button("ConfermaScelta");
        inputServi = new TextField("0");

        //Posiziona ed ingrandisci elementi nel pannellino
        pannellinoOpzioni.setTopAnchor(effettiLabel, 120.0);
        pannellinoOpzioni.setLeftAnchor(effettiLabel, 80.0);

        pannellinoOpzioni.setTopAnchor(serviLabel, 350.0);
        pannellinoOpzioni.setLeftAnchor(serviLabel, 80.0);

        pannellinoOpzioni.setTopAnchor(inputServi, 400.0);
        pannellinoOpzioni.setLeftAnchor(inputServi, 80.0);

        pannellinoOpzioni.setBottomAnchor(confermaButton, 100.0);
        pannellinoOpzioni.setRightAnchor(confermaButton, 80.0);

        //Add elementi
        pannellinoOpzioni.getChildren().addAll(effettiLabel, serviLabel, confermaButton, inputServi);
        this.getChildren().add(layoutGenerale);

    }

    void generaOpzione(FamiliareGraphic familiare, CasellaConCartaGraphic casella){

        //Disegna le scelte multiple sulla carta
        casella.getCartaAssociata().generaSceltaImmediataeCosto();
        layoutGenerale.getChildren().clear();
        layoutGenerale.getChildren().add(casella.getCartaAssociata().getIngrandimento());
        layoutGenerale.getChildren().add(pannellinoOpzioni);
        inputServi.setText("0");

        //Azione sullaConferma
        confermaButton.setOnMouseClicked(mouseEvent -> {
            try{
                int numServi = Integer.parseInt(inputServi.getText());
                opzMossa.settaOpzioni(familiare, casella,  numServi);
            } catch (Exception e){
                System.out.println("Scelta servi non valida" +e);
            }
        });
    }
}

class OpzioniMossaSenzaCarta extends Pane{

    private AnchorPane pannellinoOpzioni;
    private Button confermaButton;
    private TextField inputServi;
    private OpzioniMossa opzMossa;

    OpzioniMossaSenzaCarta(OpzioniMossa opzioniMossa){

        //inizializza oggetto
        this.opzMossa = opzioniMossa;
        this.setWidth(300);
        this.setHeight(200);
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        pannellinoOpzioni = new AnchorPane();

        //Disegna il pannello
        Label effettiLabel = new Label("<- Scegli gli effetti");
        Label serviLabel = new Label("Scegli il numero di servi:");

        confermaButton = new Button("ConfermaScelta");
        inputServi = new TextField("0");

        pannellinoOpzioni.setTopAnchor(serviLabel, 50.0);
        pannellinoOpzioni.setLeftAnchor(serviLabel, 80.0);

        pannellinoOpzioni.setTopAnchor(inputServi, 120.0);
        pannellinoOpzioni.setLeftAnchor(inputServi, 80.0);

        pannellinoOpzioni.setBottomAnchor(confermaButton, 30.0);
        pannellinoOpzioni.setRightAnchor(confermaButton, 80.0);




        //Add elementi
        pannellinoOpzioni.getChildren().addAll(effettiLabel, serviLabel, confermaButton, inputServi);
        this.getChildren().add(pannellinoOpzioni);


    }

    void generaOpzione(FamiliareGraphic familiare, CasellaGraphic casella){

        inputServi.setText("0");

        //Azione sullaConferma
        confermaButton.setOnMouseClicked(mouseEvent -> {
            try{
                int numServi = Integer.parseInt(inputServi.getText());
                opzMossa.settaOpzioni(familiare, casella, numServi);
            } catch (Exception e){
                System.out.println("Scelta servi non valida");
            }
        });
    }
}