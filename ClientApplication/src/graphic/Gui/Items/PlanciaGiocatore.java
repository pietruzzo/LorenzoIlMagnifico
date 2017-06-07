package graphic.Gui.Items;

import Domain.Risorsa;
import Domain.TipoCarta;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import static java.io.File.separator;

/**
 * Created by pietro on 03/06/17.
 * Plancia laterale che mostra le risorse le giocatore
 */
public class PlanciaGiocatore extends Pane {

    //posizione immagine plancia
    private static final String pathPlancia = "file:"+System.getProperty("user.dir")+separator+"ClientApplication"+separator+ "Risorse" +separator+"tavolaGiocatore.png";
    //dimensioni della plancia giocatore
    private static final int DIMPLANCIAX=977;
    private static final int DIMPLANCIAY=622;

    //Coordinate al centro
    private static final int PRIMACARTAX=148;
    private static final int OFFSETX=622;
    private static final int TERRITORIOY=433;
    private static final int EDIFICIOY=143;

    private int numCarteTerritorio;
    private int numCarteEdificio;

    //risorse visualizzate sulla plancia
    private Label monete;
    private Label legno;
    private Label pietra;
    private Label servi;

    //pannello con le carte impresa ed edificio
    CarteFuoriPlancia carteImpresa;
    CarteFuoriPlancia cartePersonaggio;


    PlanciaGiocatore(){

        numCarteEdificio=0;
        numCarteTerritorio=0;
        carteImpresa = new CarteFuoriPlancia();

        this.setPrefSize(DIMPLANCIAX+150, DIMPLANCIAY);
        this.getChildren().add(carteImpresa);

        //Upload immaginePlancia
        try {
            ImageView planciaView = new ImageView(new Image(pathPlancia, DIMPLANCIAX, DIMPLANCIAY, false, false, false));
            this.getChildren().add(planciaView);
        } catch (NullPointerException e){
            System.err.println("Immagine della plancia Giocatore non travata in "+ pathPlancia);
        }

        //Inizializza oggetti grafici della plancia
        BackgroundFill backgroundtext = new BackgroundFill(Color.ANTIQUEWHITE, new CornerRadii(5, false), new Insets(5,5,5,5));
        Background backgroundLabel = new Background(backgroundtext);
        monete.setBackground(backgroundLabel);
        legno.setBackground(backgroundLabel);
        pietra.setBackground(backgroundLabel);
        servi.setBackground(backgroundLabel);
        settaRisorse(new Risorsa());

        //CreaGruppo con immaginePlancia ed altri elementi del giocatore
    }

    /**
     * setta le risorse nella plancia giocatore
     * @param risorsa
     */
    public void settaRisorse(Risorsa risorsa){
        monete.setText(String.valueOf(risorsa.getMonete()));
        legno.setText(String.valueOf(risorsa.getLegno()));
        pietra.setText(String.valueOf(risorsa.getPietra()));
        servi.setText(String.valueOf(risorsa.getServi()));
    };

    /**
     * Aumenta le risorse nella plancia giocatore
     * @param risorsa
     */
    public void aggiungiRisorsa(Risorsa risorsa){
        monete.setText(String.valueOf(Integer.parseInt(monete.getText())+risorsa.getMonete()));
        legno.setText(String.valueOf(Integer.parseInt(legno.getText())+risorsa.getLegno()));
        pietra.setText(String.valueOf(Integer.parseInt(pietra.getText())+risorsa.getLegno()));
        servi.setText(String.valueOf(Integer.parseInt(servi.getText())+risorsa.getLegno()));
    };

    public void aggiungiCarta(CartaGraphic carta) {
        if (carta.getTipoCarta() == TipoCarta.Edificio) {
            carta.setX(PRIMACARTAX + numCarteEdificio * OFFSETX -carta.getImage().getWidth()/2);
            carta.setY(EDIFICIOY -carta.getImage().getHeight()/2);
            numCarteEdificio = numCarteEdificio + 1;
        } else if (carta.getTipoCarta() == TipoCarta.Territorio) {
            carta.setX(PRIMACARTAX + numCarteTerritorio * OFFSETX -carta.getImage().getWidth()/2);
            numCarteTerritorio = numCarteTerritorio + 1;
            carta.setY(TERRITORIOY -carta.getImage().getHeight()/2);
        } else {
            carta.setX(PRIMACARTAX + 7 * OFFSETX);
            if (carta.getTipoCarta() == TipoCarta.Impresa) {
                carta.setY(EDIFICIOY);
                carta.setOnMouseClicked(mouseEvent -> {
                    carteImpresa.setVisible(true);});
                carta.setOnDragExited(mouseEvent ->{
                    carteImpresa.setVisible(false);});
            } else {
                carta.setY(TERRITORIOY);
                carta.setOnMouseClicked(mouseEvent -> {
                    cartePersonaggio.setVisible(true);});
                carta.setOnDragExited(mouseEvent ->{
                    cartePersonaggio.setVisible(false);});
            }

        }
    }
}

class CarteFuoriPlancia extends Pane{

    HBox boxCarte;

    CarteFuoriPlancia(){
        super();
        boxCarte= new HBox(10);
        this.setVisible(false);
    }

    void aggiungiCarta(CartaGraphic carta){
        CartaGraphic cartaAggiunta = carta.clone();
        cartaAggiunta.setX(0);
        cartaAggiunta.setY(0);
        boxCarte.getChildren().add(cartaAggiunta);
    }
}
