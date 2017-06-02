package graphic.Gui;

import Domain.ColoreDado;
import Domain.Risorsa;
import Domain.Tabellone;
import graphic.Ui;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lorenzo.Applicazione;
import lorenzo.MainGame;

import java.util.Map;

/**
 * Created by pietro on 31/05/17.
 */
public class ControllerCampoGioco implements Ui, Controller {

    @FXML AnchorPane planciaGiocatore;
    @FXML GridPane infoPlayersGridPane;
    @FXML HBox pedineDisponibiliHBox;
    @FXML AnchorPane bottomAnchorPane;
    @FXML Text messageText;

    private MainGame mainGame;

    @FXML private void initialize(){}

    @Override
    public void setArgApplicationGui(Applicazione applicazione) {
        this.mainGame=mainGame;
    }

    @Override
    public void disabilitaCaselle(int idSpazioAzione) {

    }

    @Override
    public void visualizzaPrivilegioConsiglio(int numeroPergamene) {

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
    public void spostaFamiliare(int idGiocatore, ColoreDado coloreDado, int idSpazioAzione) {

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
    public void printaResoconto() {

    }
}
