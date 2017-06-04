package graphic.Gui;

import Domain.ColoreDado;
import graphic.Gui.Items.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static java.io.File.separator;

/**
 * Created by pietro on 04/06/17.
 */
public class Tabellone extends AnchorPane{

    //Dimensioni del tabellone
    private static final int DIMX = 863;
    private static final int DIMY = 1201;
    //Percorso dei tabelloni
    private static String URL = "file:"+System.getProperty("user.dir")+separator+"src"+separator+ "Risorse";

    //region punti notevoli per punteggio
    private static final Point2D PVITTORIA_A = new Point2D(35, 20);
    private static final Point2D PVITTORIA_B = new Point2D(822,20);
    private static final Point2D PVITTORIA_C = new Point2D(841, 1165);
    private static final Point2D PVITTORIA_D = new Point2D(828, 1181);
    private static final Point2D PVITTORIA_E = new Point2D(35, 1181);
    private static final Point2D PVITTORIA_F = new Point2D(35, 1168);
    private static final Point2D PVITTORIA_G = new Point2D(20,1168);
    private static final Point2D PVITTORIA_H = new Point2D(20, 32);

    private static final Point2D INIZIOPMILITARI = new Point2D(764, 1086);
    private static final Point2D FINEPMILITARI = new Point2D(764, 92);

    private static final Point2D INIZIOTOKENORDINE = new Point2D(679, 654);
    private static final Point2D FINETOKENORDINE = new Point2D(679, 781);

    private static final int PUNTIFEDEY=893;
    private static final int[] PUNTIFEDEX= {73, 110, 150, 200, 260, 322, 370, 407, 444, 482, 518, 556, 594, 632, 668, 706};

    private static final int LATODADO=50;
    private static final int[] DADIX= {473, 552, 631};
    private static final int DADIY=1110;


    private CaselleGioco caselle;
    private ImageView immagineTabellone;
    private ImageView[] ordineTurno;
    private Group[] puntiVittoria;
    private Group[] puntiFede;
    private Group[] puntiMilitari;
    private Map<ColoreDado, ImageView> caselleDado;




    Tabellone(){

        //setta il tabellone in attesa della partita
        immagineTabellone= new ImageView(caricaImmagineTabellone(2));
        immagineTabellone.setFitHeight(DIMY);
        immagineTabellone.setFitWidth(DIMX);

        //inizializza dadi
        ImageView[] dadi = new ImageView[3];
        for (int i = 0; i< dadi.length; i++){
            ImageView dado= new ImageView();
            dado.setFitWidth(LATODADO);
            dado.setFitHeight(LATODADO);
            dado.setX(DADIX[i]);
            dado.setY(DADIY);
            dadi[i]=dado;
        }

        caselleDado.put(ColoreDado.ARANCIO, dadi[2]);
        caselleDado.put(ColoreDado.BIANCO, dadi[1]);
        caselleDado.put(ColoreDado.NERO, dadi[0]);

        //inizializza caselle punti

        //inizializza spazi per tessere scomunica

    }

    /**
     * Sceglie il tabellone ed aggiunge le caselle
     * @param numPlayers numero definitivo di giocatori

     */
    @NotNull
    public void settaTabelloneDefinitivo(int numPlayers){
        if (numPlayers>2){
            immagineTabellone.setImage(caricaImmagineTabellone(numPlayers));
        }
        caselle = new CaselleGioco(numPlayers);
        caselle.printOnAnchorPane(this);
    }

    public void aggiungiCarta(CartaGraphic carta, CasellaConCartaGraphic casella){

        //Centra la carta nella casella
        carta.setX(casella.getSpazioCartaX()-carta.getImage().getWidth()/2);
        carta.setY(casella.getSpazioCartaY()-carta.getImage().getHeight()/2);

        //Aggiungi la carta al Tabellone
        this.getChildren().add(carta);
    }

    /**
     * Rimuovi la carta indicata dal Tabellone
     * @param carta
     */
    public void rimuoviCarta(CartaGraphic carta){
        this.getChildren().remove(carta);
    }

    /**
     * Piazza il familiare nella casella indicata sul tabellone
     * Se il familiare viene piazzato nel Palazzo del consiglio aggiorna i token di inizio turno
     * @param familiare
     * @param idCasella
     */
    public void piazzaFamiliare( FamiliareGraphic familiare, int idCasella){
        CasellaGraphic casella = caselle.getCasellabyId(idCasella);
        casella.aggiungiPedina(familiare);
        //TODO aggiorna token inizio turno
    }

    /**
     * Rimuovi il familiare dalla casella indicata sul tabellone
     * @param familiare
     * @param idCasella
     */
    public void rimuoviFamiliare( FamiliareGraphic familiare, int idCasella){
        CasellaGraphic casella = caselle.getCasellabyId(idCasella);
        casella.rimuoviFamiliare(familiare);
    }

    /**
     * aggiungi la carta scomunica
     * @param carta
     */
    public void aggiungiCartaScomunica(CartaGraphic carta){
        //TODO gestione carta scomunica
    }

    public void settaDadi(int arancio, int nero, int biano){
        //carica ed assegna immagini
    }


    @NotNull
    private Image caricaImmagineTabellone(int numGiocatori){
        return new Image(URL+"Tabellone"+numGiocatori+"Players.jpg", DIMX, DIMY, true, true, true);
    }

    /**
     * Calcola le coordinate del punto intermedio desiderato
     * @param puntoInizio coordinate della prima casella
     * @param puntoFine coordinate dell'ultima casella
     * @param numCasellaDesiderata numero della casella desiderata (0, 1, 2, 3 ...n-1)
     * @param numCaselleTotali numero totali di caselle tra i due punti (estremi inclusi)
     * @return punto intermedio
     */
    @NonNls
    private Point2D calcolaPunto(Point2D puntoInizio, Point2D puntoFine, int numCasellaDesiderata, int numCaselleTotali){

        //calcolo incremento tra un casella e la successiva
        Point2D incremento = puntoFine.subtract(puntoInizio).multiply(1/numCaselleTotali);

        //ritorno punto cercato
        return puntoInizio.add(incremento.multiply(numCasellaDesiderata));
    }
}
