package graphic.Gui.Items;

import Domain.TipoCarta;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import javax.swing.border.StrokeBorder;

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

    private TipoCarta tipoCarta;
    private String nome;
    private Group ingrandimento;
    private Dimension2D dimensioni;

    private int numSceltaCosto;
    private int numSceltaImmed;
    private int numSceltaPerm;

    private Rectangle[] costo;
    private Rectangle[] immediato;
    private Rectangle[] permanente;

    private Integer sceltaCorrenteCosto;
    private Integer sceltaCorrenteImmediato;

    CartaGraphic(String nome, TipoCarta tipoCarta, Image immagineCarta, Image cartaIngrandita) {
        super(new ImageView(immagineCarta));
        this.tipoCarta = tipoCarta;
        this.nome = nome;

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

        //setDimensioni
        this.dimensioni = new Dimension2D(immagineCarta.getWidth(), immagineCarta.getHeight());

        //inizializza le scelte
        numSceltaPerm = 1;
        numSceltaCosto = 1;
        numSceltaImmed = 1;

        //ingrandisci Carta
        this.setOnMouseDragEntered(mouseDragEvent -> {
            ingrandimento.setVisible(true);
        });
        this.setOnMouseDragExited(mouseDragEvent -> {
            ingrandimento.setVisible(false);
        });
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

        tryNotifyServer();

    }

    private void gestisciSceltaImmediato(int i) {

        //Azzera tutte le scelte
        //TODO si può anche non scegliere l'effetto immediato?
        for (Rectangle rettangolo : immediato) {
            rettangolo.setStroke(Color.BLUE);
        }

        //Seleziona quella indicata
        immediato[i].setFill(Color.GREEN);
        //comunicala al server
        tryNotifyServer();

    }

    private void gestisciSceltaPermanente(int i) {

        if(permanente[i].getFill() == Color.GREEN){
            //TODO deseleziona Scelta (Notifica il server)
            permanente[i].setFill(Color.TRANSPARENT);
        } else {
            //Azzera tutte le scelte
            for (Rectangle rettangolo : permanente) {
                rettangolo.setStroke(Color.BLUE);
            }

            //Seleziona quella indicata
            permanente[i].setFill(Color.GREEN);
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

                vettoreAree[i].setFill(Color.TRANSPARENT);
                vettoreAree[i].setStroke(Color.BLUE);
                vettoreAree[i].setStrokeWidth(3);
                vettoreAree[i].setStrokeType(StrokeType.CENTERED);
                vettoreAree[i].setVisible(true);
                ingrandimento.getChildren().add(vettoreAree[i]);
            }
        }
    }

    private void tryNotifyServer(){

        if(this.sceltaCorrenteCosto!=null && this.sceltaCorrenteImmediato != null) {
            //TODO comunica scelta immediato + costo
        }
    }
}
