package Domain.Effetti.lista.effectInterface;

import Domain.TipoAzione;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pietro on 18/05/17.
 */
public interface ModificaValoreAzione {

    public void aggiungiValoreAzione(AtomicInteger valoreAzione, TipoAzione tipoAzioneCorrente);
}
