package graphic.Gui;

import Domain.*;
import graphic.Gui.Items.*;
import graphic.Gui.Items.Tabellone;
import graphic.Ui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import lorenzo.MainGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pietro on 31/05/17.
 */
public class ControllerCampoGioco implements Ui, Controller {

    @FXML Pane vedononvedo;
    @FXML AnchorPane pannello;
    @FXML AnchorPane planciaGiocatorePane;
    @FXML GridPane infoGiocatoriPane;
    @FXML AnchorPane familiariPane;
    @FXML AnchorPane tabellonePane;
    @FXML javafx.scene.control.Label messaggi;

    private MainGame mainGame;
    private Tabellone tabelloneController;
    private CarteGioco mazzo;
    private List<GiocatoreGraphic> giocatori;
    private PlanciaGiocatore plancia;
    private int idGiocatoreClient;
    private HBox familiariDisponibili;
    private javafx.scene.control.Button start;

    @FXML private void initialize(){

        //Setta le dimensioni della pannello principale 'vedononvedo'
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        setPannelloDim(vedononvedo, width, height);

        //riscala il campo per adattarsi al pannello principale
        double pannellow = pannello.getPrefWidth();
        double pannelloh= pannello.getPrefHeight();
        double fattoreScala = width/pannellow;
        if (fattoreScala> (height/pannelloh)) fattoreScala = height/pannelloh;
        riscala(pannello, fattoreScala);

        //Inizializza il tabellone
        tabelloneController = new Tabellone(this);
        tabellonePane.getChildren().add(tabelloneController);

        //Bottone "Comincia subito"
        start = new javafx.scene.control.Button("Comincia adesso");
        start.setDisable(false);
        start.setOnMouseClicked(mouseEvent -> {
            mainGame.IniziaPartita();
            familiariPane.getChildren().remove(start);
        });
        familiariPane.getChildren().add(start);


/*      TODO crea problemi
        //Inizializza la plancia del giocatore
        plancia = new PlanciaGiocatore();
        planciaGiocatorePane.getChildren().add(plancia);

        //Aggiungi i messaggi all'area dei familiari
        familiariPane.getChildren().add(messaggi);*/
    }

    @Override
    public void setArgApplicationGui(MainGame mainGame) {
        this.mainGame=mainGame;
    }

    @Override
    public void cartaTabelloneToGiocatore(CartaGraphic carta) {
        tabelloneController.rimuoviCarta(carta);
        //TODO aggiungi alla plancia o alle info dei giocatori
    }

    @Override
    public void riscossionePrivilegio(Risorsa risorse) {
        mainGame.RiscuotiPrivilegiDelConsiglio(risorse);
    }

    @Override
    public void disabilitaCaselle(int idSpazioAzione) {
        Platform.runLater(() -> {
            tabelloneController.disabilitaCasella(idSpazioAzione);
        });

    }

    @Override
    public void visualizzaPrivilegioConsiglio(int numeroPergamene) {
        Platform.runLater(() -> PrivilegioDelConsiglio.generaPrivilegioDelConsiglio(numeroPergamene, tabellonePane, this));
    }

    @Override
    public void effettuaAzioneBonus(TipoAzione tipoAzione, int valoreAzione, Risorsa bonusRisorse) {
        //TODO
    }

    @Override
    public void stampaMessaggio(String stringa) {
        Platform.runLater(() -> messaggi.setText(stringa));
    }

    @Override
    public void inizializzaPartita(Domain.Tabellone tabellone) {
        //Inizializza i giocatori
        giocatori=new ArrayList<>();

        for (Giocatore giocatore : tabellone.getGiocatori())
        {
            if(giocatore.getNome().equals(mainGame.getNomeGiocatore()))
            {
                giocatori.add(new GiocatoreGraphic(giocatore));
                //plancia.settaRisorse(giocatore.getRisorse());
                idGiocatoreClient =giocatore.getIdGiocatore();
            }
            else giocatori.add(new GiocatoreGraphic(giocatore));
        }

        //Genera il mazzo di carte
        mazzo= new CarteGioco(tabellone.getMazzoCarte(), tabellone.getCarteScomunica());

        //Ottieni tessere Scomunica
        CartaGraphic[] carteScom = new CartaGraphic[3];
        carteScom[0] = mazzo.getCarta(tabellone.getCarteScomunica().get(0).getNome());
        carteScom[1] = mazzo.getCarta(tabellone.getCarteScomunica().get(1).getNome());
        carteScom[2] = mazzo.getCarta(tabellone.getCarteScomunica().get(2).getNome());

        Platform.runLater(() -> {
            familiariPane.getChildren().remove(start);

            tabelloneController.settaTabelloneDefinitivo(giocatori, carteScom);

            //Disponi i Familiari disponibili
            familiariDisponibili = new HBox();
            familiariDisponibili.setSpacing(10);
            familiariDisponibili.setLayoutX(920);
            familiariDisponibili.setLayoutY(1000);
            disponiFamiliari();
        });
    }


    @Override
    public void aggiornaRisorse(int idGiocatore, Risorsa risorsa) {
        Platform.runLater(() -> {
            tabelloneController.aggiornaPunti(getGiocatorebyId(idGiocatore), risorsa);
            if(idGiocatore== idGiocatoreClient) {
                plancia.settaRisorse(risorsa);
            } else {
                //TODO Aggiorna informazioni giocatori
            }
        });
    }

    @Override
    public void aggiornaDaAzioneBonus(int idGiocatore, Risorsa risorsa, int idSpazioAzione) {
        //TODO
    }

    @Override
    public void aggiornaGiocatore(int idGiocatore, Risorsa risorsa, ColoreDado coloreDado, int idSpazioAzione) {
        Platform.runLater(()->{
            //Aggiorna il giocatore sul tabellone
            GiocatoreGraphic update = getGiocatorebyId(idGiocatore);
            tabelloneController.piazzaFamiliare(update, update.getFamiliare(coloreDado), idSpazioAzione);
            //Aggiorna le risorse del giocatore
            this.aggiornaRisorse(idGiocatore, risorsa);
        });
    }

    @Override
    public void aggiungiScomunica(int[] idGiocatoriScomunicati, int periodo) {
        Platform.runLater(()->{
            GiocatoreGraphic[] giocatori = new GiocatoreGraphic[idGiocatoriScomunicati.length];
            for (int i = 0; i < idGiocatoriScomunicati.length; i++) {
                giocatori[i]=getGiocatorebyId(idGiocatoriScomunicati[i]);
            }
            tabelloneController.aggiungiScomunicaGiocatori(giocatori, periodo);
        });
    }

    @Override
    public void iniziaTurno(int[] ordineGiocatori, int[] dadi, Map<Integer, String> carte) {
        Platform.runLater(()-> {
            //Rimuovi tutte le carte rimaste associate alle caselle
            tabelloneController.rimuoviCarteTorre();
            //Rimuovi tutti i familiari
            for (GiocatoreGraphic g : giocatori) {
                for (FamiliareGraphic f : g.getFamiliari()) {
                    tabelloneController.rimuoviFamiliare(f);
                }
            }
            //Imposta i valori dei dadi
            tabelloneController.settaDadi(dadi[0], dadi[1], dadi[2]);
            //Aggiungi carte azione del nuovo turno
            for (Integer i : carte.keySet()) {
                tabelloneController.aggiungiCartaAzione(i, mazzo.getCarta(carte.get(i)));
            }

            //Disponi i familiari
            disponiFamiliari();
        });
    }

    @Override
    public void iniziaMossa(int idGiocatore) {
        if(idGiocatore==idGiocatoreClient){
            abilitaFamiliari();
        } else {
            this.stampaMessaggio("e' il turno di "+getGiocatorebyId(idGiocatore).getNome());
        }
    }

    @Override
    public void sceltaSostegnoChiesa() {
        Platform.runLater(()->{
            Pane scelta = new Pane();
            Text domanda = new Text("Vuoi sostenere la chiesa?");
            javafx.scene.control.Button b1, b2;
            b1= new javafx.scene.control.Button("Si");
            b2= new javafx.scene.control.Button("No");
            HBox hbox = new HBox(b1, b2);
            VBox vbox =new VBox(domanda, hbox);
            hbox.setAlignment(Pos.CENTER);
            vbox.setAlignment(Pos.CENTER);
            scelta.setPrefWidth(300);
            scelta.setPrefHeight(200);
            scelta.setTranslateX(700);
            scelta.setTranslateY(700);
            pannello.getChildren().add(scelta);
            b1.setOnMouseClicked(mouseEvent -> {
                mainGame.SceltaSostegnoChiesa();
                pannello.getChildren().remove(scelta);
            });
            b1.setOnMouseClicked(mouseEvent -> {
                pannello.getChildren().remove(scelta);
            });
        });
    }

    @Override
    public void finePartita(LinkedHashMap<Short, Integer> mappaRisultati) {

    }


    /**
     * Riscala il pannello
     * @param pannello
     * @param coeff
     */
    private void riscala(Pane pannello, double coeff){
        pannello.setScaleX(coeff);
        pannello.setScaleY(coeff);
        pannello.setTranslateX(- pannello.getPrefWidth()*((1-coeff)/2));
        pannello.setTranslateY(- pannello.getPrefHeight()*((1-coeff)/2));
        pannello.maxWidth(pannello.getWidth()*coeff);
        pannello.maxHeight(pannello.getHeight()*coeff);
        pannello.minWidth(pannello.getWidth()*coeff);
        pannello.minHeight(pannello.getHeight()*coeff);
        pannello.setPrefWidth(pannello.getWidth()*coeff);
        pannello.setPrefHeight(pannello.getHeight()*coeff);
    }

    private void setPannelloDim(Pane pannello, double w, double h){
        pannello.maxWidth(w);
        pannello.maxHeight(h);
        pannello.minWidth(w);
        pannello.minHeight(h);
        pannello.setPrefHeight(h);
        pannello.setPrefWidth(w);
    }

    /**
     * @param idGiocatore
     * @return il GiocatoreGraphic corrispondente dalla lista giocatori
     * @throws NullPointerException non c'è giocatore con idGiocatore
     */
    private GiocatoreGraphic getGiocatorebyId(int idGiocatore){
        for( GiocatoreGraphic giocatore : giocatori){
            if(giocatore.getIdGiocatore()==idGiocatore) return giocatore;
        }
        throw new NullPointerException(idGiocatore+ " non è presente nella lista GiocatoriGraphic");
    }

    private void disponiFamiliari(){
        familiariDisponibili.getChildren().clear();
        for(FamiliareGraphic f : getGiocatorebyId(idGiocatoreClient).getFamiliari()) {
            familiariDisponibili.getChildren().add(f);
        }
        abilitaFamiliari();
    }

    private void disabilitaFamiliari(){
        for(FamiliareGraphic f : getGiocatorebyId(idGiocatoreClient).getFamiliari()) {
            familiariDisponibili.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.GRAY, null, null)));

        }
        familiariDisponibili.setDisable(true);
    }

    private void abilitaFamiliari(){
        for(FamiliareGraphic f : getGiocatorebyId(idGiocatoreClient).getFamiliari()) {
            familiariDisponibili.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.GRAY, null, null)));

        }
        familiariDisponibili.setDisable(false);
    }
}
