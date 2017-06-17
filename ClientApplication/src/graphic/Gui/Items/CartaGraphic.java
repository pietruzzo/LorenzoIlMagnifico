package graphic.Gui.Items;

import Domain.TipoCarta;
import graphic.Gui.ControllerCampoGioco;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import graphic.Gui.Controller;
import static java.io.File.separator;

/**
 * Created by pietro on 03/06/17.
 * Se ci fossero scelte multiple ricorrere a:
 *      setNumMaxScelte -> indica se c'è più di una scelta altrimenti ignorala
 *      generaSceltaImmediataeCosto
 *      generaSceltaPermanente
 *      rimuoviSceltaImmediataeCosto
 *      getIngrandimento -> per ottenere la carta ingrandita con le scelte
 */
public class CartaGraphic extends Group {

    private static final Point2D POSIZIONESCELTAIMMEDIATO = new Point2D(135, 225);
    private static final Point2D POSIZIONESCELTACOSTO = new Point2D(20, 60);
    private static final Point2D POSIZIONESCELTAPERMANENTE = new Point2D(122, 355);

    private static final Point2D DIMENSIONESCELTAIMMEDIATO = new Point2D(125, 105);
    private static final Point2D DIMENSIONESCELTACOSTO = new Point2D(100, 180);
    private static final Point2D DIMENSIONESCELTAPERMANENTE = new Point2D(175, 130);

    private Controller callBack;
    private TipoCarta tipoCarta;
    private String nome;
    private Group ingrandimento;
    private Image cartaIngrandita;
    private Dimension2D dimensioni;

    private int numSceltaCosto;
    private int numSceltaImmed;
    private int numSceltaPerm;

    private Rectangle[] costo;
    private Rectangle[] immediato;
    private Rectangle[] permanente;

    private Integer sceltaCorrenteCosto;
    private Integer sceltaCorrenteImmediato;

    private EventHandler<MouseEvent> ingrandisci;
    private EventHandler<MouseEvent> rmIngrandisci;

    CartaGraphic(String nome, TipoCarta tipoCarta, Image immagineCarta, Image cartaIngrandita, ControllerCampoGioco callback) {
        super(new ImageView(immagineCarta));
        this.tipoCarta = tipoCarta;
        this.nome = nome;
        this.cartaIngrandita=cartaIngrandita;
        this.callBack=callback;

        //setta ingrandimento
        ImageView cartaIngranditaView = new ImageView(cartaIngrandita);
        cartaIngranditaView.setFitWidth(cartaIngrandita.getWidth());
        cartaIngranditaView.setFitHeight(cartaIngrandita.getHeight());
        this.ingrandimento = new Group();
        ingrandimento.setTranslateX(this.getLayoutX());
        ingrandimento.setTranslateY(this.getLayoutY());
        this.ingrandimento.getChildren().add(cartaIngranditaView);
        this.getChildren().add(ingrandimento);
        ingrandimento.setVisible(false);
        generaSceltaImmediataeCosto();

        //setDimensioni
        this.dimensioni = new Dimension2D(immagineCarta.getWidth(), immagineCarta.getHeight());

        //inizializza le scelte
        numSceltaPerm = 1;
        numSceltaCosto = 1;
        numSceltaImmed = 1;

    }

    public void setNumeroMaxScelte(int costo, int immediato, int permanente) {
        this.numSceltaImmed = immediato;
        this.numSceltaCosto = costo;
        this.numSceltaPerm = permanente;
    }

    public Dimension2D getDimensioni(){
        return this.dimensioni;
    }

    public void generaSceltaImmediataeCosto() {

        ingrandimento.setVisible(true);
        //Inizializza le scelte
        sceltaCorrenteCosto=null;
        sceltaCorrenteImmediato=null;
        //crea rettangoli
        costo = new Rectangle[numSceltaCosto];
        setAree(costo, POSIZIONESCELTACOSTO, DIMENSIONESCELTACOSTO);

        //setonclick
        for (int i = 0; i < numSceltaCosto; i++) {
            int finalI = i;
            costo[i].setOnMouseClicked(mouseEvent -> {
                gestisciSceltaCosto(finalI);
            });
        }

        //crea rettangoli
        immediato = new Rectangle[numSceltaImmed];
        setAree(immediato, POSIZIONESCELTAIMMEDIATO, DIMENSIONESCELTAIMMEDIATO);

        //setonclick
        for (int i = 0; i < numSceltaImmed; i++) {
            int finalI = i;
            immediato[i].setOnMouseClicked(mouseEvent -> {
                gestisciSceltaImmediato(finalI);
            });
        }
    }

    public void rimuoviSceltaImmediataeCosto(){
        for (Rectangle scelta: costo){
            ingrandimento.getChildren().remove(scelta);
        }
        for (Rectangle scelta: immediato){
            ingrandimento.getChildren().remove(immediato);
        }
    }

    public void generaSceltaPermanente() {

        ingrandimento.setVisible(true);
        //crea rettangoli
        permanente = new Rectangle[numSceltaPerm];
        setAree(permanente, POSIZIONESCELTAPERMANENTE, DIMENSIONESCELTAPERMANENTE);

        //setonclick
        for (int i = 0; i < numSceltaPerm; i++) {
            int finalI = i;
            permanente[i].setOnMouseClicked(mouseEvent -> {
                gestisciSceltaPermanente(finalI);
            });
        }

    }

    /**
     * @return il gruppo della carta ingrandita (imageview + rettangoli gestiti delle scelte)
     */
    public Group getIngrandimento(){
        return ingrandimento;
    }

    public ImageView getNewImmagineIngrandita() {return new ImageView(cartaIngrandita);}

    public TipoCarta getTipoCarta() {
        return tipoCarta;
    }

    public String getNome() {
        return nome;
    }


    private void gestisciSceltaCosto(int i) {

        //Azzera tutte le scelte
        for (Rectangle rettangolo : costo) {
            rettangolo.setStroke(Color.BLUE);
        }

        //Seleziona quella indicata
        costo[i].setFill(Color.GREEN);

        //TODO tryNotifyServer();

    }

    private void gestisciSceltaImmediato(int i) {

        //Azzera tutte le scelte
        for (Rectangle rettangolo : immediato) {
            rettangolo.setStroke(Color.BLUE);
        }

        //Seleziona quella indicata
        immediato[i].setFill(Color.GREEN);
        //comunicala al server
        //TODO tryNotifyServer();

    }

    private void gestisciSceltaPermanente(int i) {

        if(permanente[i].getStroke() == Color.GREEN){
            //TODO deseleziona Scelta (Notifica il server)
        } else {
            //Azzera tutte le scelte
            for (Rectangle rettangolo : permanente) {
                rettangolo.setStroke(Color.BLUE);
            }

            //Seleziona quella indicata
            permanente[i].setStroke(Color.GREEN);
            //comunicala al server
            //TODO comunica scelta permanente al server (SSE c'è più di una scelta nella carta)
        }
    }

    /**
     * Crea le aree che andranno sovrapposte alla Carta Ingrandita con i bordi e background
     *
     * @param vettoreAree
     * @param posizScelta
     * @param dimScelta
     */
    private void setAree(Rectangle[] vettoreAree, Point2D posizScelta, Point2D dimScelta) {
        int numScelta = vettoreAree.length;
        if (numScelta > 1) {
            for (int i = 0; i < numScelta; i++) {
                vettoreAree[i] = new Rectangle(dimScelta.getX(), dimScelta.getY() / numScelta);
                vettoreAree[i].setX(posizScelta.getX());
                vettoreAree[i].setX(posizScelta.getY() + dimScelta.getY() * i);

                vettoreAree[i].setFill(Color.BLACK);
                vettoreAree[i].setStroke(Color.BLUE);
                vettoreAree[i].setStrokeWidth(3);
                vettoreAree[i].setStrokeType(StrokeType.CENTERED);
                vettoreAree[i].setVisible(true);
                ingrandimento.getChildren().add(vettoreAree[i]);
            }
        }
    }


    /**
     * Aggiunge l'ingrandimento della carta sul pannello e ne gestisce posizione e visibilità
     * L'ingrandimento si posizionerà vicino alle coordinate di this
     * @param contenitore
     */
    public void setEventHandlerIngrandisci(Pane contenitore){
        final Group[] gruppo = new Group[1];

        this.ingrandisci = mouseEvent -> {
            ImageView iv = this.getNewImmagineIngrandita();
            iv.setFitWidth(iv.getImage().getWidth() * 0.7);
            iv.setFitHeight(iv.getImage().getHeight() * 0.7);
            gruppo[0] = new Group(iv);
            gruppo[0].setLayoutX(this.getLayoutX() + 70);
            gruppo[0].setLayoutY(this.getLayoutY() + 120);
            gruppo[0].setVisible(true);
            gruppo[0].toFront();
            contenitore.getChildren().add(gruppo[0]);
        };

        this.rmIngrandisci = mouseEvent -> {
            contenitore.getChildren().remove(gruppo[0]);
        };

        this.setOnMouseEntered(this.ingrandisci);
        this.setOnMouseExited(this.rmIngrandisci);
    }


    public void setEventHandlerPermanente(Pane contenitore){

        //Genero le scelte permanenti
        this.rimuoviSceltaImmediataeCosto();
        this.generaSceltaPermanente();
        //Rimuovo i vecchi mouseHandler
        this.removeEventHandler(MouseEvent.MOUSE_ENTERED, ingrandisci);
        this.removeEventHandler(MouseEvent.MOUSE_EXITED, rmIngrandisci);

        ingrandimento.setLayoutX((contenitore.getWidth()-this.cartaIngrandita.getWidth())/2);
        ingrandimento.setLayoutX((contenitore.getHeight()-this.cartaIngrandita.getHeight())/2);
        ingrandimento.setVisible(true);

        //Add Exit Button
        try {
            ImageView bottone = new ImageView(new Image("file:"+System.getProperty("user.dir")+separator+"ClientApplication"+separator+"Risorse"+separator+"buttonX.png", 50, 50, true, true, true));
            bottone.setLayoutX(cartaIngrandita.getWidth());
            bottone.setLayoutY(cartaIngrandita.getHeight()-bottone.getFitHeight());
            ingrandimento.getChildren().add(bottone);

            //mouseHandler per uscire dalla carta
            bottone.setOnMouseClicked(mouseEvent -> ingrandimento.setVisible(false));
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        contenitore.getChildren().add(ingrandimento);

        //mouseHandler per ingrandire la carta
        this.setOnMouseClicked(mouseEvent -> ingrandimento.setVisible(true));
    }
}
