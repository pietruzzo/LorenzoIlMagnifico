package graphic.Gui;

import Domain.*;
import graphic.Gui.Items.*;
import graphic.Gui.Items.Tabellone;
import graphic.Ui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import lorenzo.MainGame;

import java.awt.*;
import java.io.IOException;
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
    @FXML AnchorPane infoGiocatori;
    @FXML AnchorPane tabellonePane;
    @FXML TextArea messaggi;

    private MainGame mainGame;
    private Tabellone tabelloneController;
    private CarteGioco mazzo;
    private List<GiocatoreGraphic> giocatori;
    private PlanciaGiocatore plancia;
    private AltriGiocatoriHBox infoGiocatoriController;
    private int idGiocatoreClient;
    private FamiliareGraphic familiareSelezionato;
    private SelettoreFamiliariGraphic selettoreFamiliari;
    //private OpzioniMossa opzioniMossa;

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


        //Inizializza la plancia del giocatore
        plancia = new PlanciaGiocatore();
        planciaGiocatorePane.getChildren().add(plancia);

        //Inizializza il pannello Selettore Familiari
        selettoreFamiliari= new SelettoreFamiliariGraphic(this);
        planciaGiocatorePane.getChildren().add(selettoreFamiliari);
        selettoreFamiliari.setLayoutY(200);
        selettoreFamiliari.setPrefWidth(400);
        selettoreFamiliari.setPrefHeight(300);

        //Inizializza OpzioniMossa
        //opzioniMossa=new OpzioniMossa(this);

    }

    @Override
    public void setArgApplicationGui(MainGame mainGame) {
        this.mainGame=mainGame;
    }

    @Override
    public void cartaTabelloneToGiocatore(CartaGraphic carta, GiocatoreGraphic giocatoreGraphic) {
        tabelloneController.rimuoviCarta(carta);

        if(giocatoreGraphic.getIdGiocatore()==idGiocatoreClient){
            plancia.aggiungiCarta(carta);
        } else {
            infoGiocatoriController.addCarta(giocatoreGraphic, carta);
        }
    }

    @Override
    public void riscossionePrivilegio(Risorsa risorse) {
        mainGame.RiscuotiPrivilegiDelConsiglio(risorse);
        this.stampaMessaggio("Hai riscosso il privilegio del consiglio");
    }

    @Override
    public void exitGame() {
        mainGame.closeClient();
    }

    @Override
    public void giocaAdesso() {
        mainGame.IniziaPartita();
    }

    @Override
    public void selezionaFamiliare(FamiliareGraphic familiare, boolean selezionePossibile) {
        if(selezionePossibile) familiareSelezionato=familiare;
        else stampaMessaggio("Il familiare è già stato utilizzato");
    }

    @Override
    public void casellaSelezionata(CasellaGraphic casella) {
        //Pane pannelloScelta=null;
        if(casella.isDisattiva()){
            stampaMessaggio("Non puoi piazzare il familiare in questa casella");
        } else if(familiareSelezionato==null){
            stampaMessaggio("Non hai selezionato il familiare");
        } /*else if(!(casella instanceof CasellaConCartaGraphic)){
            pannelloScelta = opzioniMossa.getScelta(familiareSelezionato, (CasellaConCartaGraphic)casella);
        } else if(casella instanceof CasellaConCartaGraphic && ((CasellaConCartaGraphic) casella).getCartaAssociata()==null){
            stampaMessaggio("Non puoi piazzare il familiare in questa casella");
        } else{
            pannelloScelta = opzioniMossa.getScelta(familiareSelezionato, casella);
        }*/
        else if(casella instanceof CasellaConCartaGraphic && ((CasellaConCartaGraphic) casella).getCartaAssociata()==null){
            stampaMessaggio("Non puoi piazzare il familiare in questa casella");
        }else {
            try {
                new OpzioniMossaAlternative(this, casella, familiareSelezionato, mainGame.getApplicazione()).setSubScene(pannello);
            }catch (IOException e){
                System.out.println("opzioni non caricate");
            }
        }
/*
        if(pannelloScelta!=null){
            //Aggiungo il pannello se non presente, lo centro e lo mostro
            pannelloScelta.setLayoutX(pannello.getWidth()/2 - pannelloScelta.getWidth()/2);
            pannelloScelta.setLayoutY(pannello.getHeight()/2 - pannelloScelta.getHeight()/2);
            pannelloScelta.setVisible(true);
            if(! pannello.getChildren().contains(pannelloScelta)){
                pannello.getChildren().add(pannelloScelta);
            }
            pannelloScelta.toFront();
        }*/
    }

    @Override
    public void mandaMossaAlServer(FamiliareGraphic f, CasellaGraphic casella, int servitori) {
        mainGame.PiazzaFamiliare((short)idGiocatoreClient, f.getColore(), casella.getCasellaId(), servitori);
    }

    @Override
    public void disabilitaCaselle(int idSpazioAzione) {
        Platform.runLater(() -> {
            tabelloneController.disattivaCasella(idSpazioAzione);
        });

    }

    @Override
    public void visualizzaPrivilegioConsiglio(int numeroPergamene) {
        Platform.runLater(() -> {
            try {
                new PrivilegioConsiglioController(numeroPergamene, pannello, this, mainGame.getApplicazione());
            } catch (IOException e) {
                stampaMessaggio("Errore nella generazione del privilegio del consiglio");
            }
        });
    }

    @Override
    public void effettuaAzioneBonus(TipoAzione tipoAzione, int valoreAzione, Risorsa bonusRisorse) {
        //TODO
    }

    @Override
    public void stampaMessaggio(String stringa) {
        Platform.runLater(() -> messaggi.appendText(stringa+"\n"));
    }

    @Override
    public void inizializzaPartita(Domain.Tabellone tabellone) {
        //Inizializza i giocatori
        giocatori=new ArrayList<>();
        List<GiocatoreGraphic> giocatoriEsclusoCorrente = new ArrayList<>();

        for (Giocatore giocatore : tabellone.getGiocatori())
        {
            GiocatoreGraphic gg = new GiocatoreGraphic(giocatore);
            giocatori.add(gg);

            if(giocatore.getNome().equals(mainGame.getNomeGiocatore()))
            {
                Platform.runLater(() -> plancia.settaRisorse(giocatore.getRisorse()));
                idGiocatoreClient =giocatore.getIdGiocatore();
            } else giocatoriEsclusoCorrente.add(gg);
        }

        //Genera il mazzo di carte
        mazzo= new CarteGioco(tabellone.getMazzoCarte(), tabellone.getCarteScomunica());
        for (CartaGraphic cg : mazzo.getCarte()){
            final Group[] ingrandimento = new Group[1];
            cg.setOnMouseEntered(mouseEvent -> {
                ImageView iv  = cg.getNewImmagineIngrandita();
                iv.setFitWidth(iv.getImage().getWidth()*0.7);
                iv.setFitHeight(iv.getImage().getHeight()*0.7);
                ingrandimento[0] = new Group(iv);
                ingrandimento[0].setLayoutX(cg.getLayoutX()+70);
                ingrandimento[0].setLayoutY(cg.getLayoutY()+120);
                ingrandimento[0].setVisible(true);
                ingrandimento[0].toFront();
                tabelloneController.getChildren().add(ingrandimento[0]);
            });
            cg.setOnMouseExited(mouseEvent -> tabelloneController.getChildren().remove(ingrandimento[0]));
        }


        //Ottieni tessere Scomunica
        CartaGraphic[] carteScom = new CartaGraphic[3];
        carteScom[0] = mazzo.getCarta(tabellone.getCarteScomunica().get(0).getNome());
        carteScom[1] = mazzo.getCarta(tabellone.getCarteScomunica().get(1).getNome());
        carteScom[2] = mazzo.getCarta(tabellone.getCarteScomunica().get(2).getNome());

        Platform.runLater(() -> {
            tabelloneController.settaTabelloneDefinitivo(giocatori, carteScom, this);

            //Genero infoGiocatoriController e lo inizializzo
            infoGiocatoriController =new AltriGiocatoriHBox(giocatoriEsclusoCorrente, infoGiocatori);
            for(Giocatore g : tabellone.getGiocatori()){
                if(g.getIdGiocatore()!=this.idGiocatoreClient){
                    infoGiocatoriController.setRisorse(getGiocatorebyId(g.getIdGiocatore()), g.getRisorse());
                }
            }

            //Rimuovo bottone "AvviaPartita" e dispongo i familiari
            selettoreFamiliari.inizializzaFamiliari(getGiocatorebyId(idGiocatoreClient).getFamiliari());
        });

    }


    @Override
    public void aggiornaRisorse(int idGiocatore, Risorsa risorsa) {
        Platform.runLater(() -> {
            tabelloneController.aggiornaPunti(getGiocatorebyId(idGiocatore), risorsa);
            if(idGiocatore== idGiocatoreClient) {
                plancia.settaRisorse(risorsa);
            } else {
                infoGiocatoriController.setRisorse(getGiocatorebyId(idGiocatore), risorsa);
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
            if(idGiocatore==idGiocatoreClient)
                selettoreFamiliari.familiareUsato(getGiocatorebyId(idGiocatore).getFamiliare(coloreDado));
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

            //Set ordine Turno
            List<GiocatoreGraphic> ordineTurno = new ArrayList<>();
            for(int i : ordineGiocatori){
                ordineTurno.add(getGiocatorebyId(i));
            }
            tabelloneController.setOrdineTurno(ordineTurno);

            //Disponi i familiari
            selettoreFamiliari.setFamiliariInizioTurno();

        });
    }

    @Override
    public void iniziaMossa(int idGiocatore) {
        if(idGiocatore==idGiocatoreClient){
            selettoreFamiliari.abiltaMossa();
            this.stampaMessaggio("e' il TUO TURNO");
            selettoreFamiliari.toFront();
        } else {
            this.stampaMessaggio("e' il turno di "+getGiocatorebyId(idGiocatore).getNome());
            selettoreFamiliari.disabilitaMossa();
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
        //TODO
    }

    @Override
    public void GiocatoreDisconnesso(int idGiocatoreDisconnesso) {
        //TODO
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

}
