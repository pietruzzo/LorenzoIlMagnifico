package graphic.Gui.Items;

import Domain.ColoreDado;
import Domain.Risorsa;
import Domain.TipoAzione;
import graphic.Gui.Controller;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
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
    private static final String URL = "file:"+System.getProperty("user.dir")+separator+ "ClientApplication"+ separator + "Risorse"+ separator;

    private static final Point2D[] POSIZIONE_SCOMUNICA = {new Point2D(195, 785), new Point2D(258, 792), new Point2D(325, 782)};


    private CaselleGioco caselle;
    private DadiGraphic dadi;
    private CasellePunti punti;
    private Controller callback;
    private Map<GiocatoreGraphic, Rectangle> scomuniche;
    private Integer[]caselleDisabAzSpec;



    public Tabellone(Controller callback){
        ImageView immagineTabellone;

        //setta il tabellone in attesa della partita
        immagineTabellone= new ImageView(caricaImmagineTabellone(2));
        immagineTabellone.setFitHeight(DIMY);
        immagineTabellone.setFitWidth(DIMX);
        immagineTabellone.setVisible(true);
        this.getChildren().add(immagineTabellone);


        //inizializza dadi
        dadi=new DadiGraphic();


    }

    /**
     * Sceglie il tabellone e crea elementi mancanti
     * @param listaGiocatori giocatori partecipanti
     */
    @NotNull
    public synchronized void settaTabelloneDefinitivo(List<GiocatoreGraphic> listaGiocatori, CartaGraphic[] tessereScomunica, Controller callback){

        this.callback=callback;

        if (listaGiocatori.size() > 2){
            ImageView immagineTabellone;
            this.getChildren().clear();
            immagineTabellone = new ImageView(caricaImmagineTabellone(listaGiocatori.size()));
            this.getChildren().add(immagineTabellone);
        }

        caselle = new CaselleGioco(listaGiocatori.size(), this, callback);

        punti = new CasellePunti(listaGiocatori);

        this.getChildren().addAll(punti, dadi);

        //Inizializza scomunica
        aggiungiCartaScomunica(tessereScomunica[0], 1);
        aggiungiCartaScomunica(tessereScomunica[1], 2);
        aggiungiCartaScomunica(tessereScomunica[2], 3);

        scomuniche= new HashMap<>();
        for(GiocatoreGraphic giocatore: listaGiocatori){
            Rectangle scomunica = new Rectangle(15, 15, giocatore.getColoreGiocatore().getColore());
            scomunica.setVisible(false);
            scomuniche.put(giocatore, scomunica);
        }
    }


    /**
     * Ottieni e rimuovi dal tabellone la carta associata allo spazio azione
     * @param idSpazioAzione con carta associata
     * @return carta associata allo spazio azione, null se non c'è carta associata
     */
    @Nullable
    public CartaGraphic rimuoviCartaSpazioAzione(int idSpazioAzione){
        CartaGraphic carta = null;
        if(caselle.getCasellabyId(idSpazioAzione) instanceof CasellaConCartaGraphic){
            carta= ((CasellaConCartaGraphic)caselle.getCasellabyId(idSpazioAzione)).getCartaAssociata();
            rimuoviCarta(carta);
        }
        return carta;
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
     * @param giocatore
     * @param familiare
     * @param idCasella
     */
    public void piazzaFamiliare( GiocatoreGraphic giocatore, FamiliareGraphic familiare, int idCasella){
        CasellaGraphic casella = caselle.getCasellabyId(idCasella);
        casella.aggiungiPedina(familiare);

        if(casella instanceof CasellaConCartaGraphic ){
            CasellaConCartaGraphic c = (CasellaConCartaGraphic) casella;
            if(c.getCartaAssociata()!=null){
                callback.cartaTabelloneToGiocatore(c.getCartaAssociata(), giocatore);
            }
        }
    }

    /**
     * Rimuovi il familiare dalla sul tabellone
     * @param familiare
     */
    public void rimuoviFamiliare( FamiliareGraphic familiare){
        for (CasellaGraphic c : caselle.getListaCaselle()) {
            if(c.getPedine().getChildren().contains(familiare))
                c.getPedine().getChildren().remove(familiare);
        }
    }

    /**
     * aggiungi la carta scomunica
     * @param carta carta da aggiungere
     * @param periodo periodo a cui aggiungere la carta {1, 2, 3}
     */
    private void aggiungiCartaScomunica(CartaGraphic carta, int periodo){

        carta.setLayoutX(POSIZIONE_SCOMUNICA[periodo-1].getX()-carta.getDimensioni().getWidth()/2);
        carta.setLayoutY(POSIZIONE_SCOMUNICA[periodo-1].getY()-carta.getDimensioni().getHeight()/2);
        this.getChildren().add(carta);
    }

    /**
     * Aggiungi la carta sul campo associandola alla casella indicata
     * @param idCasella
     * @param carta
     */
    public void aggiungiCartaAzione(@NotNull int idCasella,@NotNull CartaGraphic carta){

        CasellaConCartaGraphic casella = (CasellaConCartaGraphic) caselle.getCasellabyId(idCasella);
        //centra la carta
        carta.setLayoutX(casella.getSpazioCartaX() - carta.getDimensioni().getWidth()/2);
        carta.setLayoutY(casella.getSpazioCartaY() - carta.getDimensioni().getHeight()/2);

        //aggiungi la carta al tabellone
        carta.setVisible(true);
        this.getChildren().add(carta);

        //Aggiungi gli effetti per l'ingrandimento
        carta.setEventHandlerIngrandisci(this);

        //Aggiungi la carta alla casella
        casella.setCartaAssociata(carta);
    }

    public void aggiungiScomunicaGiocatori (GiocatoreGraphic[] giocatori, int periodo){//TODO aggiunta scomunica
         }

    public void rimuoviCarteTorre(){
        for (CasellaGraphic c :caselle.getListaCaselle()){
            if(c instanceof CasellaConCartaGraphic){
                CasellaConCartaGraphic casT = (CasellaConCartaGraphic) c;
                if(casT.getCartaAssociata()!=null){
                    this.getChildren().remove(casT.getCartaAssociata());
                    casT.setCartaAssociata(null);
                }
            }
        }
    }

    public void settaDadi(int nero, int bianco, int arancio){
        dadi.setFacce(nero, bianco, arancio);
    }


    public void disattivaCasella(int idCasella){
        caselle.getCasellabyId(idCasella).disabilita();
    }

    public void predisponiAzioneSpecifica(TipoAzione tipoAzione){
        caselleDisabAzSpec = caselle.abilitaAzioneSpecifica(tipoAzione);
    }

    public void riattivaCaselleDaAzioneSpecifica(){
        if(caselleDisabAzSpec!=null){
            for (int i = 0; i < caselleDisabAzSpec.length; i++)
                caselle.getCasellabyId(i).abilita();
        }
    }

    public void attivaCasella(int idCasella){
        caselle.getCasellabyId(idCasella).abilita();
    }

    public void isCasellaDisattiva(int idCasella){
        caselle.getCasellabyId(idCasella).isDisattiva();
    }

    public void aggiornaPunti(GiocatoreGraphic giocatore, Risorsa risorsa){
        punti.setPuntiFede(giocatore, risorsa.getPuntiFede());
        punti.setPuntiMilitari(giocatore, risorsa.getPuntiMilitari());
        punti.setPuntiVittoria(giocatore, risorsa.getPuntiVittoria());
    }

    public  void azzeraPuntiFede(GiocatoreGraphic giocatore){
        punti.setPuntiFede(giocatore, 0);
    }

    public void setOrdineTurno(List<GiocatoreGraphic> giocatori){
        punti.rimuoviOrdineGiro();
        for(GiocatoreGraphic g : giocatori){
            punti.updateOrdineGiro(g);
        }
    }

    @NotNull
    private Image caricaImmagineTabellone(int numGiocatori){
        return new Image(URL+"Tabellone"+numGiocatori+"Players.jpg", DIMX, DIMY, false, false, false);
    }

}

class DadiGraphic extends Group{

    private static final int LATODADO=50;
    private static final int[] DADIX= {473, 552, 631};
    private static final int DADIY=1110;

    private Image[] facceImg;
    private ImageView arancioIm;
    private ImageView neroIm;
    private ImageView biancoIm;

    DadiGraphic(){

        super();
        Rectangle arancio = new Rectangle(LATODADO, LATODADO, ColoreDado.ARANCIO.getColore());
        Rectangle nero = new Rectangle(LATODADO, LATODADO, ColoreDado.NERO.getColore());
        Rectangle bianco = new Rectangle(LATODADO, LATODADO, ColoreDado.BIANCO.getColore());

        nero.setX(DADIX[0]- LATODADO/2);
        bianco.setX(DADIX[1]- LATODADO/2);
        arancio.setX(DADIX[2]- LATODADO/2);

        nero.setY(DADIY- LATODADO/2);
        bianco.setY(DADIY- LATODADO/2);
        arancio.setY(DADIY- LATODADO/2);

        this.getChildren().addAll(nero, bianco, arancio);

        arancioIm= new ImageView();
        neroIm= new ImageView();
        biancoIm= new ImageView();

        neroIm.setX(DADIX[0]- LATODADO/2);
        biancoIm.setX(DADIX[1]- LATODADO/2);
        arancioIm.setX(DADIX[2]- LATODADO/2);

        neroIm.setY(DADIY- LATODADO/2);
        biancoIm.setY(DADIY- LATODADO/2);
        arancioIm.setY(DADIY- LATODADO/2);

        arancioIm.setFitHeight(LATODADO);
        neroIm.setFitHeight(LATODADO);
        biancoIm.setFitHeight(LATODADO);

        arancioIm.setFitWidth(LATODADO);
        neroIm.setFitWidth(LATODADO);
        biancoIm.setFitWidth(LATODADO);

        this.getChildren().addAll(neroIm, biancoIm, arancioIm);
        this.setVisible(false);

        uploadFacce();
    }

    public void setFacce(int nero, int bianco, int arancio){
        biancoIm.setImage(facceImg[bianco-1]);
        neroIm.setImage(facceImg[nero-1]);
        arancioIm.setImage(facceImg[arancio-1]);
        this.setVisible(true);
    }

    private void uploadFacce(){
        facceImg =new Image[6];
        String percorsoFaccia="";
        for (int i = 0; i< facceImg.length; i++) {
                percorsoFaccia = "file:"+System.getProperty("user.dir")+separator+"ClientApplication"+separator+"Risorse"+separator+"dado"+String.valueOf(i+1)+".png";
                facceImg[i]= new Image(percorsoFaccia, LATODADO, LATODADO, true, true);
                if (facceImg[i]==null){
                    System.err.println("Fallito upload faccia dado");
                    System.err.println("percorso= " +percorsoFaccia);
            }

        }
    }
}

class CasellePunti extends Group{


    //region punti notevoli per punteggio
    private static final Point2D PVITTORIA_A = new Point2D(35, 20);
    private static final Point2D PVITTORIA_B = new Point2D(822,20);
    private static final Point2D PVITTORIA_C = new Point2D(841, 25);
    private static final Point2D PVITTORIA_D = new Point2D(828, 1181);
    private static final Point2D PVITTORIA_E = new Point2D(821, 1181);
    private static final Point2D PVITTORIA_F = new Point2D(35, 1168);
    private static final Point2D PVITTORIA_G = new Point2D(20,1168);
    private static final Point2D PVITTORIA_H = new Point2D(20, 32);

    private static final int NCASELLE_AB_EF = 21;
    private static final int NCASELLE_CD_GH = 31;


    private static final Point2D INIZIOPMILITARI = new Point2D(764, 1086);
    private static final Point2D FINEPMILITARI = new Point2D(764, 92);

    private static final int NCASELLE_MILITARI = 26;


    private static final int PUNTIFEDEY=893;
    private static final int[] PUNTIFEDEX= {73, 110, 150, 200, 260, 322, 370, 407, 444, 482, 518, 556, 594, 632, 668, 706};

    private static final int NCASELLE_FEDE = 16;

    private static final Point2D[] ORDINETURNO= {new Point2D(679, 654), new Point2D(679,697), new Point2D(679,739), new Point2D(679,782)};

    private Group[] puntiVittoria;
    private Group[] puntiFede;
    private Group[] puntiMilitari;
    private int numTokenPosPiazzati;

    /**
     * Circle[] usa le posizioni:
     *      0: punti vittoria
     *      1: punti Fede
     *      2: punti militari
     *      3: ordine turno
     */
    Map<GiocatoreGraphic, Circle[]> tokenAssociati;

    /**
     * Per ogni giocatore inizializzo un vettore di Token per segnare i punti
     * @param giocatori
     */
    CasellePunti(List<GiocatoreGraphic> giocatori){
        super();

        tokenAssociati=new HashMap();
        numTokenPosPiazzati = 0;

        for(GiocatoreGraphic giocatore: giocatori){
            Circle[] tokenGiocatore = new Circle[4];
            for (int i = 0; i < tokenGiocatore.length; i++) {
                tokenGiocatore[i] =new Circle(15, giocatore.getColoreGiocatore().getColore());
            }
            tokenAssociati.put(giocatore, tokenGiocatore);
        }

        //per ogni casella individuo le coordinate e traslo di conseguenza i Group
        inizializzaPuntiFede();
        inizializzaPuntiMilitari();
        inizializzaPuntiVittoria();


        //Piazza Giocatori nella casella 0 ed aggiungi il token nascosto per l'ordine del turno
        for (GiocatoreGraphic giocatore: giocatori) {

            tokenAssociati.get(giocatore)[0].setLayoutY(puntiVittoria[0].getChildren().size()*5);
            tokenAssociati.get(giocatore)[1].setLayoutY(puntiMilitari[0].getChildren().size()*5);
            tokenAssociati.get(giocatore)[2].setLayoutY(puntiFede[0].getChildren().size()*5);

            puntiVittoria[0].getChildren().add(tokenAssociati.get(giocatore)[0]);
            puntiFede[0].getChildren().add(tokenAssociati.get(giocatore)[1]);
            puntiMilitari[0].getChildren().add(tokenAssociati.get(giocatore)[2]);

            tokenAssociati.get(giocatore)[3].setVisible(false);
            this.getChildren().add(tokenAssociati.get(giocatore)[3]);
        }
    }

    public void setPuntiMilitari(GiocatoreGraphic giocatore,  int puntiMilitari){
        traslaToken(tokenAssociati.get(giocatore)[2], this.puntiMilitari[puntiMilitari], this.puntiMilitari);
    }

    public void setPuntiFede(GiocatoreGraphic giocatore,  int puntiFede){
        traslaToken(tokenAssociati.get(giocatore)[1], this.puntiFede[puntiFede], this.puntiFede);
    }

    public void setPuntiVittoria(GiocatoreGraphic giocatore,  int puntiVittoria){
        traslaToken(tokenAssociati.get(giocatore)[0], this.puntiVittoria[puntiVittoria], this.puntiVittoria);
    }

    /**
     * Inserisce il token "posizione" nella prima posizione libera se non ancora presente
     * @param giocatore
     * @return true se il token è stato aggiunto, altrimenti false
     */
    public boolean updateOrdineGiro(GiocatoreGraphic giocatore){

        Circle token = tokenAssociati.get(giocatore)[3];

        if (!token.isVisible()) {

            tokenAssociati.get(giocatore)[3].setCenterX(ORDINETURNO[numTokenPosPiazzati].getX());
            tokenAssociati.get(giocatore)[3].setCenterY(ORDINETURNO[numTokenPosPiazzati].getY());
            tokenAssociati.get(giocatore)[3].setVisible(true);
            numTokenPosPiazzati= numTokenPosPiazzati+1;
            return true;
        }
        return false;
    }


    /**
     * Azzera i token posizione rimuovendoli dalla scena
     */
    public void rimuoviOrdineGiro(){
        numTokenPosPiazzati= 0;
        for(Circle[] pedine : tokenAssociati.values()){
            pedine[3].setVisible(false);
        }
    }

    /**
     * @param token
     * @param destinazione
     * @param gruppoAppartenenza puntiVittoria, PuntiMilitari, puntiFede
     */
    private void traslaToken(Circle token, Group destinazione, Group[] gruppoAppartenenza){

        Group cellaOrigine = getGruppodaToken(gruppoAppartenenza, token);

        if(destinazione!=cellaOrigine) {

            //inizializza la traslazione
            TranslateTransition animazione = new TranslateTransition(Duration.millis(2000), token);

            //imposta destinazione
            animazione.setToX(destinazione.getLayoutX() - cellaOrigine.getLayoutX());
            animazione.setToY(destinazione.getLayoutY() - cellaOrigine.getLayoutY());

            //Imposta azioni al termine della traslazione
            animazione.setOnFinished(actionEvent -> {

                //individua "cella" di origine,  rimuovi l'elemento dalla sergente, azzera le transizioni, rilocalo nel nuovo gruppo
                Group cella = getGruppodaToken(gruppoAppartenenza, token);
                cella.getChildren().remove(token);
                token.setCenterX(0);
                token.setCenterY(0);
                token.setTranslateX(destinazione.getChildren().size() * 5 - 5);
                token.setTranslateY(destinazione.getChildren().size() * 5 - 5);
                destinazione.getChildren().add(token);
            });

            animazione.play();
        }
    }

    private void inizializzaPuntiMilitari(){

        puntiMilitari= new Group[NCASELLE_MILITARI];

        for (int i = 0; i <puntiMilitari.length ; i++) {
            Point2D coordinate = calcolaPunto(INIZIOPMILITARI, FINEPMILITARI, i, puntiMilitari.length);
            puntiMilitari[i] = new Group();
            puntiMilitari[i].setLayoutX(coordinate.getX());
            puntiMilitari[i].setLayoutY(coordinate.getY());
            this.getChildren().add(puntiMilitari[i]);
        }
    }

    private void inizializzaPuntiFede(){

        puntiFede = new Group[NCASELLE_FEDE];

        for (int i = 0; i <puntiFede.length ; i++) {
            puntiFede[i] = new Group();
            puntiFede[i].setLayoutX(PUNTIFEDEX[i]);
            puntiFede[i].setLayoutY(PUNTIFEDEY);
            this.getChildren().add(puntiFede[i]);
        }
    }

    private void inizializzaPuntiVittoria(){

        puntiVittoria = new Group[2* (NCASELLE_AB_EF+NCASELLE_CD_GH) -4];

        for (int i = 0; i <puntiVittoria.length ; i++) {

            puntiVittoria[i] = new Group();
            Point2D coordinate;

            if(i<NCASELLE_AB_EF-1){
                coordinate= calcolaPunto(PVITTORIA_A, PVITTORIA_B, i, NCASELLE_AB_EF-1);
            } else if(i>=20 && i<50){
                coordinate= calcolaPunto(PVITTORIA_C, PVITTORIA_D, i-20, NCASELLE_CD_GH-1);
            } else if (i>=50 && i<70){
                coordinate= calcolaPunto(PVITTORIA_E, PVITTORIA_F, i-50, NCASELLE_AB_EF-1);
            } else{
                coordinate= calcolaPunto(PVITTORIA_G, PVITTORIA_H, i-70, NCASELLE_CD_GH-1);
            }

            puntiVittoria[i].setLayoutX(coordinate.getX());
            puntiVittoria[i].setLayoutY(coordinate.getY());
            this.getChildren().add(puntiVittoria[i]);
        }
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

        //calcolo incremento tra una casella e la successiva
        Point2D incremento = puntoFine.subtract(puntoInizio).multiply(1/Double.valueOf(numCaselleTotali));

        //ritorno punto intermedio cercato
        return puntoInizio.add(incremento.multiply(numCasellaDesiderata));
    }

    private Group getGruppodaToken(Group[] gruppoAppartenenza, Circle token){
        for (Group cella : gruppoAppartenenza){
            if (cella.getChildren().contains(token)) {
                return cella;
            }
        }
        return null;
    }
}
