package Domain.Effetti;

import Domain.Carta;
import Domain.Risorsa;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public interface EndGame {
    public void azioneTerminale(Risorsa risorseGiocatore, List<Carta> listaCarte);
}
