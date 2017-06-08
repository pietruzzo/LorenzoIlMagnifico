package Domain.Effetti.lista;

import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.ModificaValoreAzione;
import Domain.TipoAzione;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pietro on 18/05/17.
 * Aumenta il valore di una azione specifica
 */
public class AumentaValoreAzione extends Effetto implements ModificaValoreAzione {
    private TipoAzione tipoAzione;
    private int incrementoAzione;

    public AumentaValoreAzione(TipoAzione tipoAzione, int incrementoAzione) {
        this.tipoAzione = tipoAzione;
        this.incrementoAzione = incrementoAzione;
    }

    @Override
    public void aggiungiValoreAzione(AtomicInteger valoreAzione, TipoAzione tipoAzioneCorrente) {
        if (tipoAzioneCorrente==tipoAzione) valoreAzione.set(valoreAzione.get()+incrementoAzione);
    }
    //Aumenta il valore di una azione specifica
}
