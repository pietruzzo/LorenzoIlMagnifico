package graphic.Gui;

import Domain.Risorsa;
import graphic.Gui.Items.CartaGraphic;
import lorenzo.Applicazione;
import lorenzo.MainGame;

/**
 * Created by pietro on 31/05/17.
 * Interfaccia utilizzata per passare argomenti ai Controller quando inizializzati
 */
public interface Controller {
    void setArgApplicationGui(MainGame mainGame);
    void cartaTabelloneToGiocatore(CartaGraphic carta);
    void riscossionePrivilegio(Risorsa risorse);
}
