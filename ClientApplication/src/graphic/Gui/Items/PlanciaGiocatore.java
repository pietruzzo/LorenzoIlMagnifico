package graphic.Gui.Items;

import Domain.Carta;
import Domain.Risorsa;
import Domain.TipoCarta;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;


import java.awt.*;

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
    private static final double SCALA=0.7;

    //Coordinate al centro
    private static final int PRIMACARTAX=148;
    private static final int OFFSETX=137;
    private static final int TERRITORIOY=390;
    private static final int EDIFICIOY=120;

    //Coord risorse plancia
    private static final Point2D COORD_MONETE = new Point2D(152, 547);
    private static final Point2D COORD_LEGNI = new Point2D(309, 548);
    private static final Point2D COORD_PIETRE = new Point2D(461, 545);
    private static final Point2D COORD_SERVI = new Point2D(595, 538);


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


    public PlanciaGiocatore(){

        ImageView planciaView = null;
        numCarteEdificio=0;
        numCarteTerritorio=0;
        carteImpresa = new CarteFuoriPlancia();
        cartePersonaggio = new CarteFuoriPlancia();
        Rectangle areaCarteImpresa = new Rectangle(140, 240);
        Rectangle areaCartePersonaggio = new Rectangle(140, 240);

        monete=new Label("Inizialize");
        legno= new Label("Inizialize");
        pietra=new Label("Inizialize");
        servi=new Label("Inizialize");

        //Scala e trasla la plancia
        this.setTranslateY(30);
        this.setTranslateX(250);
        this.setScaleX(0.7);
        this.setScaleY(0.7);

        this.setPrefSize(DIMPLANCIAX+150, DIMPLANCIAY);

        //Upload immaginePlancia
        try {
            planciaView = new ImageView(new Image(pathPlancia, DIMPLANCIAX, DIMPLANCIAY, false, false, false));
            this.getChildren().add(planciaView);
        } catch (NullPointerException e){
            System.err.println("Immagine della plancia Giocatore non travata in "+ pathPlancia);
        }

        //Inizializza oggetti grafici della plancia
        BackgroundFill backgroundtext = new BackgroundFill(Color.ALICEBLUE, new CornerRadii(20, false), new Insets(0,0,0,0));
        Background backgroundLabel = new Background(backgroundtext);
        monete.setBackground(backgroundLabel);
        legno.setBackground(backgroundLabel);
        pietra.setBackground(backgroundLabel);
        servi.setBackground(backgroundLabel);
        monete.setFont(new Font(60));
        legno.setFont(new Font(60));
        pietra.setFont(new Font(60));
        servi.setFont(new Font(60));

        monete.setLayoutX(COORD_MONETE.getX());
        legno.setLayoutX(COORD_LEGNI.getX());
        pietra.setLayoutX(COORD_PIETRE.getX());
        servi.setLayoutX(COORD_SERVI.getX());
        monete.setLayoutY(COORD_MONETE.getY());
        legno.setLayoutY(COORD_LEGNI.getY());
        pietra.setLayoutY(COORD_PIETRE.getY());
        servi.setLayoutY(COORD_SERVI.getY());

        areaCarteImpresa.setLayoutX(PRIMACARTAX + 6.2 * OFFSETX);
        areaCartePersonaggio.setLayoutX(PRIMACARTAX + 6.2 * OFFSETX);
        areaCarteImpresa.setLayoutY(EDIFICIOY -areaCarteImpresa.getHeight()/2);
        areaCartePersonaggio.setLayoutY(TERRITORIOY -areaCartePersonaggio.getHeight()/2);

        //Set Effetti pannello carte Personaggio ed impresa
        areaCartePersonaggio.setOnMouseClicked(mouseEvent -> {
            if(!cartePersonaggio.isVisible()) {
                cartePersonaggio.setVisible(true);
                cartePersonaggio.setTranslateX((areaCartePersonaggio.getLayoutX()-cartePersonaggio.getWidth()) -30);
                cartePersonaggio.setTranslateY(areaCartePersonaggio.getLayoutY());
                cartePersonaggio.toFront();
            }
            else cartePersonaggio.setVisible(false);
        });

        areaCarteImpresa.setOnMouseClicked(mouseEvent -> {
            if(!carteImpresa.isVisible()) {
                carteImpresa.setVisible(true);
                carteImpresa.setTranslateX((areaCarteImpresa.getLayoutX()-carteImpresa.getWidth()) -30);
                carteImpresa.setTranslateY(areaCarteImpresa.getLayoutY());
                carteImpresa.toFront();
            }
            else carteImpresa.setVisible(false);
        });

        //Aggiungi gli oggetti grafici alla plancia
        this.getChildren().add(areaCarteImpresa);
        this.getChildren().add(areaCartePersonaggio);
        this.getChildren().add(carteImpresa);
        this.getChildren().add(cartePersonaggio);
        this.getChildren().add(monete);
        this.getChildren().add(legno);
        this.getChildren().add(pietra);
        this.getChildren().add(servi);

        settaRisorse(new Risorsa());

        //CreaGruppo con immaginePlancia ed altri elementi del giocatore
        //TODO implementa le carte fuori plancia
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

        //Riscala carta
        carta.setScaleX(1/SCALA);
        carta.setScaleY(1/SCALA);

        //Trasla carta
        if (carta.getTipoCarta() == TipoCarta.Edificio) {
            carta.setLayoutX(PRIMACARTAX + numCarteEdificio * OFFSETX -carta.getDimensioni().getWidth()*this.getScaleX()/2);
            carta.setLayoutY(EDIFICIOY -carta.getDimensioni().getHeight()/2);
            numCarteEdificio = numCarteEdificio + 1;
            this.getChildren().add(carta);
        } else if (carta.getTipoCarta() == TipoCarta.Territorio) {
            carta.setLayoutX(PRIMACARTAX + numCarteTerritorio * OFFSETX -carta.getDimensioni().getWidth()*this.getScaleX()/2);
            numCarteTerritorio = numCarteTerritorio + 1;
            carta.setLayoutY(TERRITORIOY -carta.getDimensioni().getHeight()/2);
            this.getChildren().add(carta);
        } else {
            if (carta.getTipoCarta() == TipoCarta.Impresa) carteImpresa.aggiungiCarta(carta);
            else cartePersonaggio.aggiungiCarta(carta);
            }

    }
}

class CarteFuoriPlancia extends Pane{

    HBox boxCarte;

    CarteFuoriPlancia(){
        super();
        //this.setWidth(20);
        //this.setHeight(250);
        boxCarte= new HBox(50);
        boxCarte.setAlignment(Pos.CENTER);
        this.getChildren().add(boxCarte);
        this.setVisible(false);
    }

    void aggiungiCarta(CartaGraphic carta){
        carta.setLayoutX(0);
        carta.setLayoutY(0);
        boxCarte.getChildren().add(carta);
    }
}
