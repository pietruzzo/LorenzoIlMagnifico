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
 * Interfaccia utilizzata per passare argomenti ai Controller quando inizializzati
 */
public interface Controller {
    void setArgApplicationGui(MainGame mainGame);
    void cartaTabelloneToGiocatore(CartaGraphic carta, GiocatoreGraphic giocatoreGraphic);
    void riscossionePrivilegio(Risorsa risorse);
    void exitGame();
    void giocaAdesso();
    void selezionaFamiliare(FamiliareGraphic familiare, boolean piazzamentoPossibile);
    void casellaSelezionata(CasellaGraphic casella);
    void mandaMossaAlServer(FamiliareGraphic f, CasellaGraphic casella, int servitori);
    void scegliEffetto(String nomeCarta, Integer codiceScelta);
}
