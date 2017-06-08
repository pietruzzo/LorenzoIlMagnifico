package Domain.Effetti.lista.effectInterface;

import Domain.Carta;
import Domain.Risorsa;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pietro on 18/05/17.
 */
public interface EndGame {
    public void azioneTerminale(Risorsa risorseGiocatore, List<Carta> listaCarte, AtomicInteger modificaPVittoria);
}
