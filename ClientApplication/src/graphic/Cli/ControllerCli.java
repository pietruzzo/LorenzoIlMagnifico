package graphic.Cli;

import Domain.ColoreDado;
import Domain.Risorsa;
import Domain.Tabellone;
import graphic.Ui;
import lorenzo.MainGame;

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
    public void aggiungiScomunica(int idGiocatore, int periodo) {

    }

    @Override
    public void iniziaTurno(int[] dadi, Map<Integer, String> carte) {

    }

    @Override
    public void iniziaMossa(int idGiocatore) {

    }

    @Override
    public void finePeriodo() {

    }

    @Override
    public void printaResoconto() {

    }
}