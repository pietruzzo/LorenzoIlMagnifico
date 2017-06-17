package graphic.Gui.Items;

import Domain.Risorsa;
import Domain.TipoCarta;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;


import static java.io.File.separator;

/**
 * Created by pietro on 03/06/17.
 * Plancia laterale che mostra le risorse le giocatore
 */
public class PlanciaGiocatore extends Pane {

    //posizione immagine plancia
    private static final String pathPlancia = "file:"+System.getProperty("user.dir")+separator+"ClientApplication"+separator+ "Risorse" +separator;
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
    CarteFuoriPlanciaPane carteImpresa;
    CarteFuoriPlanciaPane cartePersonaggio;


    public PlanciaGiocatore(){

        ImageView planciaView = null;
        numCarteEdificio=0;
        numCarteTerritorio=0;
        carteImpresa = new CarteFuoriPlanciaPane();
        cartePersonaggio = new CarteFuoriPlanciaPane();
        ImageView areaCarteImpresaButton=null;
        ImageView areaCartePersonaggioButton = null;

        monete=new Label("Inizialize");
        legno= new Label("Inizialize");
        pietra=new Label("Inizialize");
        servi=new Label("Inizialize");

        //Scala e trasla la plancia
        this.setTranslateY(60);
        this.setTranslateX(180);
        this.setScaleX(0.7);
        this.setScaleY(0.7);

        this.setPrefSize(DIMPLANCIAX+150, DIMPLANCIAY);

        //Upload immaginePlancia
        try {
            planciaView = new ImageView(new Image(pathPlancia+"tavolaGiocatore.png", DIMPLANCIAX, DIMPLANCIAY, false, false, false));
            areaCarteImpresaButton= new ImageView(new Image(pathPlancia+"Carte"+separator+"carteSviluppo"+separator+"Impresa_1.jpg", 140, 240, true, true));
            areaCartePersonaggioButton= new ImageView(new Image(pathPlancia+"Carte"+separator+"carteSviluppo"+separator+"Personaggio_1.jpg", 140, 240, true, true));
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

        areaCarteImpresaButton.setLayoutX(PRIMACARTAX + 6.2 * OFFSETX);
        areaCartePersonaggioButton.setLayoutX(PRIMACARTAX + 6.2 * OFFSETX);
        areaCarteImpresaButton.setLayoutY(EDIFICIOY -areaCarteImpresaButton.getFitHeight()/2);
        areaCartePersonaggioButton.setLayoutY(TERRITORIOY -areaCartePersonaggioButton.getFitHeight()/2);

        //Set Effetti pannello carte Personaggio ed impresa
        ImageView finalAreaCartePersonaggioButton = areaCartePersonaggioButton;
        areaCartePersonaggioButton.setOnMouseClicked(mouseEvent -> {
            if(!cartePersonaggio.isVisible()) {
                cartePersonaggio.setVisible(true);
                cartePersonaggio.setTranslateX((finalAreaCartePersonaggioButton.getLayoutX()-cartePersonaggio.getWidth()) -30);
                cartePersonaggio.setTranslateY(finalAreaCartePersonaggioButton.getLayoutY());
                cartePersonaggio.toFront();
            }
            else cartePersonaggio.setVisible(false);
        });

        ImageView finalAreaCarteImpresaButton = areaCarteImpresaButton;
        areaCarteImpresaButton.setOnMouseClicked(mouseEvent -> {
            if(!carteImpresa.isVisible()) {
                carteImpresa.setVisible(true);
                carteImpresa.setTranslateX((finalAreaCarteImpresaButton.getLayoutX()-carteImpresa.getWidth()) -30);
                carteImpresa.setTranslateY(finalAreaCarteImpresaButton.getLayoutY());
                carteImpresa.toFront();
            }
            else carteImpresa.setVisible(false);
        });

        //Aggiungi gli oggetti grafici alla plancia
        this.getChildren().add(areaCarteImpresaButton);
        this.getChildren().add(areaCartePersonaggioButton);
        this.getChildren().add(carteImpresa);
        this.getChildren().add(cartePersonaggio);
        this.getChildren().add(monete);
        this.getChildren().add(legno);
        this.getChildren().add(pietra);
        this.getChildren().add(servi);

        settaRisorse(new Risorsa());

        this.toBack();
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

