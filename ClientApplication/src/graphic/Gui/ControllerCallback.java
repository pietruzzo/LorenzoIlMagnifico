package graphic.Gui;

import Domain.Risorsa;
import graphic.Gui.Items.CartaGraphic;
import graphic.Gui.Items.CasellaGraphic;
import graphic.Gui.Items.FamiliareGraphic;
import graphic.Gui.Items.GiocatoreGraphic;
import lorenzo.Applicazione;
import lorenzo.MainGame;

/**
 * Created by pietro on 31/05/17.
 * Interfaccia utilizzata per passare argomenti ai ControllerCallback quando inizializzati
 */
public interface ControllerCallback {

    /**
     * Setta la classe che fa da proxy tra client e server
     * @param mainGame
     */
    void setArgApplicationGui(MainGame mainGame);

    /**
     * Passa la carta dal tabellone alle informazioni giocatori
     * @param carta
     * @param giocatoreGraphic
     */
    void cartaTabelloneToGiocatore(CartaGraphic carta, GiocatoreGraphic giocatoreGraphic);

    /**
     * Risquoti il privilegio del consiglio
     * @param risorse
     */
    void riscossionePrivilegio(Risorsa risorse);

    /**
     * Gestisci l'uscita del client
     */
    void exitGame();

    /**
     * Tenta di iniziare la partita
     */
    void giocaAdesso();
    void selezionaFamiliare(FamiliareGraphic familiare, boolean piazzamentoPossibile);
    void casellaSelezionata(CasellaGraphic casella);
    void mandaMossaAlServer(FamiliareGraphic f, CasellaGraphic casella, int servitori);
    void scegliEffetto(String nomeCarta, Integer codiceScelta);
    void rispondiScomunica(boolean risposta);
    void saltaAzioneBonus();
}
