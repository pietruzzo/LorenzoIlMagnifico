package Domain.Effetti;

import Domain.Carta;
import Domain.Risorsa;
import Domain.SpazioAzione;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class GestoreEffetti {
    private static Risorsa malusRisorsaScomunica;
    private static List<Effetto> listaEffettiValidati;


    /**
     * @param carteGiocatore Tutte le carte del giocatore
     * @param azione il valore dell'azione senza effettiCarte
     * //@param bonusSpazioAzione informazioni di dominio (basta avere la casella)
     * @param casella
     * @param serviAllocati (Risorse
     * @return Costo generato dagli effetti delle carte
     */
    public static Risorsa validaCarte(List<Carta> carteGiocatore, int azione, SpazioAzione casella, int serviAllocati){
        return new Risorsa();
    }

    public static Risorsa effettuaAzione(List<Carta> carteGiocatore, int azione,SpazioAzione casella, int serviAllocati){
       return new Risorsa();
    }

    public static void inizioTurno(List<Carta> carteGiocatore){}

    public static void endGame(List<Carta> carteGiocatore, Risorsa risorseGiocatore){}

    private static void eliminaImmediati(List<Carta> carteGiocatore){}
    private static void fineMossa(){}

}
