package graphic.Gui.Items;

import Domain.TipoCarta;
import graphic.Gui.ControllerCallBack;
import graphic.Gui.GameController;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    private ControllerCallBack callBack;
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

    private Color selectedColor;

    CartaGraphic(String nome, TipoCarta tipoCarta, Image immagineCarta, Image cartaIngrandita, ControllerCallBack callback) {
        super(new ImageView(immagineCarta));
        this.tipoCarta = tipoCarta;
        this.nome = nome;
        this.cartaIngrandita = cartaIngrandita;
        this.callBack = callback;
        this.costo = null;
        this.immediato = null;
        this.permanente = null;
        this.selectedColor = new Color(0, 1, 0, 0.3);

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

    }

    public void setNumeroMaxScelte(int costo, int immediato, int permanente) {
        this.numSceltaImmed = immediato;
        this.numSceltaCosto = costo;
        this.numSceltaPerm = permanente;
    }

    public Dimension2D getDimensioni() {
        return this.dimensioni;
    }

    public void generaSceltaImmediataeCosto() {

        if (numSceltaCosto > 1) {
            //Inizializza le scelte
            sceltaCorrenteCosto = null;
            sceltaCorrenteImmediato = null;
            //crea rettangoli
            costo = new Rectangle[numSceltaCosto];
            setAree(costo, POSIZIONESCELTACOSTO, DIMENSIONESCELTACOSTO);
            costo[0].setStroke(Color.GREEN);

            //setonclick
            for (int i = 0; i < numSceltaCosto; i++) {
                int finalI = i;
                costo[i].setOnMouseClicked(mouseEvent -> {
                    gestisciSceltaCosto(finalI);
                });
            }
        }

        if (numSceltaImmed > 1) {
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
    }

    public void rimuoviSceltaImmediataeCosto() {
        if (costo != null) {
            for (Rectangle scelta : costo) {
                ingrandimento.getChildren().remove(scelta);
            }
        }
        if (immediato != null) {
            for (Rectangle scelta : immediato) {
                ingrandimento.getChildren().remove(immediato);
            }
        }
    }

    public void generaSceltaPermanente() {


        //crea rettangoli
        permanente = new Rectangle[numSceltaPerm];
        setAree(permanente, POSIZIONESCELTAPERMANENTE, DIMENSIONESCELTAPERMANENTE);

        if (numSceltaPerm > 1) {
            ingrandimento.setVisible(true);
            //setonclick
            for (int i = 0; i < numSceltaPerm; i++) {
                int finalI = i;
                permanente[i].setOnMouseClicked(mouseEvent -> {
                    gestisciSceltaPermanente(finalI);
                });
            }
        }
    }

    /**
     * @return il gruppo della carta ingrandita (imageview + rettangoli gestiti delle scelte)
     */
    public Group getIngrandimento() {
        return ingrandimento;
    }

    public ImageView getNewImmagineIngrandita() {
        return new ImageView(cartaIngrandita);
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
            rettangolo.setFill(Color.TRANSPARENT);
        }

        //Seleziona quella indicata
        costo[i].setFill(selectedColor);
        costo[i].setStroke(Color.GREEN);

        callBack.scegliEffetto(getNome(), i);

    }

    private void gestisciSceltaImmediato(int i) {

        //Azzera tutte le scelte
        for (Rectangle rettangolo : immediato) {
            rettangolo.setStroke(Color.BLUE);
            rettangolo.setFill(Color.TRANSPARENT);
        }

        //Seleziona quella indicata
        immediato[i].setFill(selectedColor);
        immediato[i].setStroke(Color.GREEN);
        //comunicala al server
        callBack.scegliEffetto(getNome(), i);

    }

    private void gestisciSceltaPermanente(int i) {

        //Azzera tutte le scelte
        for (Rectangle rettangolo : permanente) {
            rettangolo.setStroke(Color.BLUE);
            rettangolo.setFill(Color.TRANSPARENT);
        }

        //Seleziona quella indicata
        permanente[i].setStroke(Color.GREEN);
        permanente[i].setFill(selectedColor);
        //comunicala al server
        callBack.scegliEffetto(getNome(), i);
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
                vettoreAree[i].setY(posizScelta.getY() + (dimScelta.getY() * i) / numScelta);

                vettoreAree[i].setFill(Color.TRANSPARENT);
                vettoreAree[i].setStroke(Color.BLUE);
                vettoreAree[i].setStrokeWidth(2);
                vettoreAree[i].setVisible(true);
                vettoreAree[i].toFront();
                ingrandimento.getChildren().add(vettoreAree[i]);
            }
        }
    }


    /**
     * Aggiunge l'ingrandimento della carta sul pannello e ne gestisce posizione e visibilità
     * L'ingrandimento si posizionerà vicino alle coordinate di this
     *
     * @param contenitore
     */
    public void setEventHandlerIngrandisci(Pane contenitore) {
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


    public void setEventHandlerPermanente(Pane contenitore) {

        //Genero le scelte permanenti
        ingrandimento.setVisible(false);
        this.rimuoviSceltaImmediataeCosto();
        this.generaSceltaPermanente();
        //Rimuovo i vecchi mouseHandler
        this.removeEventHandler(MouseEvent.MOUSE_ENTERED, ingrandisci);
        this.removeEventHandler(MouseEvent.MOUSE_EXITED, rmIngrandisci);
        this.setOnMouseEntered(null);
        this.setOnMouseExited(null);


        //Add Exit Button
        try {
            Group bottone = new Group();
            Rectangle areaAttiva = new Rectangle(60, 60);
            areaAttiva.setFill(Color.TRANSPARENT);
            ImageView bottoneIm = new ImageView(new Image("file:" + System.getProperty("user.dir") + separator + "ClientApplication" + separator + "Risorse" + separator + "buttonX.png", 50, 50, true, true, true));
            bottone.setLayoutX(cartaIngrandita.getWidth());
            bottone.setLayoutY(cartaIngrandita.getHeight());
            bottone.getChildren().addAll(bottoneIm, areaAttiva);
            ingrandimento.getChildren().add(bottone);

            EventHandler<MouseEvent> eventoMouseOn = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    ingrandimento.setLayoutX(1230);
                    ingrandimento.setLayoutY(257);
                    ingrandimento.setScaleX(0.6);
                    ingrandimento.setScaleY(0.6);
                    ingrandimento.setVisible(true);
                    bottone.setVisible(false);
                }
            };
            this.setOnMouseEntered(eventoMouseOn);
            this.setOnMouseExited(mouseEvent1 -> ingrandimento.setVisible(false));

            //mouseHandler per uscire dalla carta
            areaAttiva.setOnMouseClicked(mouseEvent -> {
                ingrandimento.setVisible(false);
                this.setOnMouseEntered(eventoMouseOn);
                this.setOnMouseExited(mouseEvent1 -> ingrandimento.setVisible(false));
            });

            contenitore.getChildren().add(ingrandimento);

            //mouseHandler per ingrandire la carta
            this.setOnMouseClicked(mouseEvent -> {
                ingrandimento.setScaleX(1);
                ingrandimento.setScaleY(1);
                ingrandimento.setLayoutX((contenitore.getWidth() - this.cartaIngrandita.getWidth()) / 2);
                ingrandimento.setLayoutY((contenitore.getHeight() - this.cartaIngrandita.getHeight()) / 2);
                ingrandimento.setVisible(true);
                this.setOnMouseEntered(null);
                this.setOnMouseExited(null);
                bottone.setVisible(true);
            });

        } catch (NullPointerException e) {
            System.out.println(e.toString());
        }
    }
}
