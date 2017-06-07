package graphic.Gui;

import Domain.ColoreDado;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.TipoAzione;
import graphic.Ui;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lorenzo.Applicazione;
import lorenzo.MainGame;

import java.awt.*;
import java.util.LinkedHashMap;
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

    private MainGame mainGame;

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
    }

    @Override
    public void setArgApplicationGui(MainGame mainGame) {
        this.mainGame=mainGame;
    }

    @Override
    public void disabilitaCaselle(int idSpazioAzione) {

    }

    @Override
    public void visualizzaPrivilegioConsiglio(int numeroPergamene) {

    }

    @Override
    public void effettuaAzioneBonus(TipoAzione tipoAzione, int valoreAzione, Risorsa bonusRisorse) {

    }

    @Override
    public void stampaMessaggio(String stringa) {

    }

    @Override
    public void inizializzaPartita(Tabellone tabellone) {

    }

    @Override
    public void aggiornaRisorse(int idGiocatore, Risorsa risorsa) {

    }

    @Override
    public void aggiornaDaAzioneBonus(int idGiocatore, Risorsa risorsa, int idSpazioAzione) {

    }

    @Override
    public void aggiornaGiocatore(int idGiocatore, Risorsa risorsa, ColoreDado coloreDado, int idSpazioAzione) {

    }

    @Override
    public void aggiungiScomunica(int[] idGiocatoriScomunicati, int periodo) {

    }

    @Override
    public void iniziaTurno(int[] ordineGiocatori, int[] dadi, Map<Integer, String> carte) {

    }

    @Override
    public void iniziaMossa(int idGiocatore) {

    }

    @Override
    public void sceltaSostegnoChiesa() {

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
}
