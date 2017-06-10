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
        if (tipoAzioneCorrente == tipoAzione || (tipoAzioneCorrente == TipoAzione.TORRE && (
                tipoAzione == TipoAzione.TORRE || tipoAzione == TipoAzione.TORRE_PERSONAGGIO || tipoAzione == TipoAzione.TORRE_IMPRESA ||
                        tipoAzione == TipoAzione.TORRE_TERRITORIO || tipoAzione == TipoAzione.TORRE_EDIFICIO))
                || (tipoAzione == TipoAzione.TORRE && (
                tipoAzioneCorrente == TipoAzione.TORRE || tipoAzioneCorrente == TipoAzione.TORRE_PERSONAGGIO || tipoAzioneCorrente == TipoAzione.TORRE_IMPRESA ||
                        tipoAzioneCorrente == TipoAzione.TORRE_TERRITORIO || tipoAzioneCorrente == TipoAzione.TORRE_EDIFICIO)))
            valoreAzione.set(valoreAzione.get() + incrementoAzione);
    }
    //Aumenta il valore di una azione specifica
}
