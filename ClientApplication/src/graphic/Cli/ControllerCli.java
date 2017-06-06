package graphic.Cli;

import Domain.ColoreDado;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.TipoAzione;
import graphic.Ui;
import lorenzo.MainGame;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by pietro on 31/05/17.
 */
public class ControllerCli implements Ui {

    private MainGame mainGame;



    public ControllerCli(MainGame mainGame) {
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
}
