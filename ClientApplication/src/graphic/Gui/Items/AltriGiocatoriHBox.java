package graphic.Gui.Items;

import Domain.Risorsa;
import Domain.TipoCarta;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.io.File.separator;


/**
 * Created by pietro on 14/06/17.
 */
public class AltriGiocatoriHBox extends HBox{

    private final static String percorsoFileDescrizione = "file:"+System.getProperty("user.dir")+separator+"ClientApplication"+separator+ "Risorse"+separator;
    private final int DIMX_STANDARD = 50;
    private final int DIMY_STANDARD = 50;

    private Image moneta;
    private Image servo;
    private Image pietra;
    private Image legno;
    private Image carteImpresa;
    private Image cartePersonaggio;
    private Image carteTerritorio;
    private Image carteEdificio;

    private Map<GiocatoreGraphic, AltroGiocatoreVBox> tabelle;

    public AltriGiocatoriHBox (List<GiocatoreGraphic> giocatori, AnchorPane pannello){

        super();
        pannello.getChildren().add(this);
        inizializzaImmagini();

        tabelle = new HashMap<>();
        for(GiocatoreGraphic g : giocatori){
            AltroGiocatoreVBox tabellaGiocatore = new AltroGiocatoreVBox(g.getNome(), g.getColoreGiocatore().getColore(), this);
            tabelle.put(g, tabellaGiocatore);
            this.getChildren().add(tabellaGiocatore);

            //AggiungiCarte fuori Plancia
            pannello.getChildren().add(tabellaGiocatore.territorio);
            pannello.getChildren().add(tabellaGiocatore.edificio);
            pannello.getChildren().add(tabellaGiocatore.personaggio);
            pannello.getChildren().add(tabellaGiocatore.impresa);
        }

        //Set proprietà per this
        this.setAlignment(Pos.CENTER);
        pannello.setTopAnchor(this, 30.0);
        pannello.setLeftAnchor(this, 50.0);
        pannello.setRightAnchor(this, 50.0);

    }

    public void setRisorse(GiocatoreGraphic giocatore, Risorsa risorsa){
        tabelle.get(giocatore).settaRisorse(risorsa);
    }

    public void addCarta(GiocatoreGraphic giocatore, CartaGraphic carta){
        tabelle.get(giocatore).aggiungiCarta(carta);
    }

    Image getMoneta() {
        return moneta;
    }

    Image getServo() {
        return servo;
    }

    Image getPietra() {
        return pietra;
    }

    Image getLegno() {
        return legno;
    }

    Image getCarteImpresa() {
        return carteImpresa;
    }

    Image getCartePersonaggio() {
        return cartePersonaggio;
    }

    Image getCarteTerritorio() {
        return carteTerritorio;
    }

    Image getCarteEdificio() {
        return carteEdificio;
    }

    private void  inizializzaImmagini(){

        this.moneta= uploadImage("moneta.png");
        this.legno= uploadImage("legno.png");
        this.pietra = uploadImage("pietra.png");
        this.servo = uploadImage("servitore.png");
        this.carteTerritorio = uploadImage("Territorio.png");
        this.carteEdificio = uploadImage("Edificio.png");
        this.cartePersonaggio = uploadImage("Personaggio.png");
        this.carteImpresa = uploadImage("Impresa.png");

    }

    private Image uploadImage(String nomeFile){
        return new Image(percorsoFileDescrizione+nomeFile, DIMX_STANDARD, DIMY_STANDARD, true, false);
    }
}

class AltroGiocatoreVBox extends VBox{

    private static final double FATTORE_CARTE = 0.5;
    private Label nomeGiocatore;
    private Label monete;
    private Label legno;
    private Label pietra;
    private Label servi;

    CarteFuoriPlanciaPane territorio;
    CarteFuoriPlanciaPane edificio;
    CarteFuoriPlanciaPane personaggio;
    CarteFuoriPlanciaPane impresa;

    AltroGiocatoreVBox(String nomeGiocatore, Color coloreGiocatore, AltriGiocatoriHBox risorse){

        super();
        GridPane dettagliGiocatore = new GridPane();

        this.nomeGiocatore= new Label(nomeGiocatore);

        this.monete = new Label();
        this.legno = new Label();
        this.pietra = new Label();
        this.servi = new Label();

        //proprietà label
        setProprietaLabel(this.monete);
        setProprietaLabel(this.legno);
        setProprietaLabel(this.servi);
        setProprietaLabel(this.pietra);

        //Creazione ImageView
        ImageView moneteIm = new ImageView(risorse.getMoneta());
        ImageView legnoIm = new ImageView(risorse.getLegno());
        ImageView pietraIm = new ImageView(risorse.getPietra());
        ImageView serviIm = new ImageView(risorse.getServo());
        ImageView carteTerritorioIm = new ImageView(risorse.getCarteTerritorio());
        ImageView carteEdificioIm = new ImageView(risorse.getCarteEdificio());
        ImageView cartePersonaggioIm = new ImageView(risorse.getCartePersonaggio());
        ImageView carteImpresaIm = new ImageView(risorse.getCarteImpresa());

        //Inizializza CarteFuoriPlancia
        territorio= new CarteFuoriPlanciaPane();
        edificio  =new CarteFuoriPlanciaPane();
        personaggio =new CarteFuoriPlanciaPane();
        impresa = new CarteFuoriPlanciaPane();

        //Add dettagli To GridPane
        dettagliGiocatore.add(legnoIm, 0, 0);
        dettagliGiocatore.add(pietraIm, 0, 1);
        dettagliGiocatore.add(serviIm, 0, 2);
        dettagliGiocatore.add(moneteIm, 0, 3);

        dettagliGiocatore.add(legno, 1, 0);
        dettagliGiocatore.add(pietra, 1, 1);
        dettagliGiocatore.add(servi, 1, 2);
        dettagliGiocatore.add(monete, 1, 3);

        dettagliGiocatore.add(carteTerritorioIm, 3, 0);
        dettagliGiocatore.add(carteEdificioIm, 3, 1);
        dettagliGiocatore.add(cartePersonaggioIm, 3, 2);
        dettagliGiocatore.add(carteImpresaIm, 3, 3);

        //proprietà nomeGiocatore
        this.nomeGiocatore.setTextFill(coloreGiocatore);
        this.nomeGiocatore.setAlignment(Pos.CENTER);
        this.nomeGiocatore.setFont(new Font(40));


        //Proprietà this
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.fillWidthProperty();

        //proprietà dettagliGiocatore
        dettagliGiocatore.setAlignment(Pos.CENTER_LEFT);
        dettagliGiocatore.setHgap(30);
        dettagliGiocatore.setVgap(15);

        //Proprietà SetOnClick carteFuoriPlancia
        setProprietaCarteFuoriPlancia(territorio, carteTerritorioIm);
        setProprietaCarteFuoriPlancia(edificio, carteEdificioIm);
        setProprietaCarteFuoriPlancia(personaggio, cartePersonaggioIm);
        setProprietaCarteFuoriPlancia(impresa, carteImpresaIm);

        //Includi descrizione e dettagli in this
        this.getChildren().addAll(this.nomeGiocatore, dettagliGiocatore);
    }

    void settaRisorse(Risorsa risorse){
        monete.setText(String.valueOf(risorse.getMonete()));
        legno.setText(String.valueOf(risorse.getLegno()));
        pietra.setText(String.valueOf(risorse.getPietra()));
        servi.setText(String.valueOf(risorse.getServi()));
    }

    void aggiungiCarta(CartaGraphic carta){

        ImageView immagineCarta = carta.getNewImmagineIngrandita();
        immagineCarta.setFitWidth(immagineCarta.getImage().getWidth()*FATTORE_CARTE);
        immagineCarta.setFitHeight(immagineCarta.getImage().getHeight()*FATTORE_CARTE);
        immagineCarta.setPreserveRatio(true);

        if(carta.getTipoCarta()== TipoCarta.Territorio) territorio.aggiungiCarta(immagineCarta);
        if(carta.getTipoCarta()== TipoCarta.Edificio) edificio.aggiungiCarta(immagineCarta);
        if(carta.getTipoCarta()== TipoCarta.Personaggio) personaggio.aggiungiCarta(immagineCarta);
        if(carta.getTipoCarta()== TipoCarta.Impresa) impresa.aggiungiCarta(immagineCarta);
    }

    private void setProprietaCarteFuoriPlancia(CarteFuoriPlanciaPane carte, ImageView icona){

        icona.setOnMouseEntered(mouseEvent -> {
            carte.setLayoutX(500- carte.getWidth()/2);
            carte.setLayoutY(300);
            carte.setVisible(true);
            carte.toFront();
        });
        icona.setOnMouseExited(mouseEvent -> carte.setVisible(false));
    }

    private void setProprietaLabel(Label label){
        label.setFont(new Font(25));
        label.setTextFill(Color.BLUE);
    }
}