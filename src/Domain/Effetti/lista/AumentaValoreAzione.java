package Domain.Effetti.lista;

import Domain.Effetti.Azionabile;
import Domain.Effetti.ModificaValoreAzione;
import Domain.Effetti.Validabile;
import Domain.TipoAzione;

/**
 * Created by pietro on 18/05/17.
 * Aumenta il valore di una azione specifica
 */
public class AumentaValoreAzione implements ModificaValoreAzione {
    private TipoAzione tipoAzione;
    private int incrementoAzione;
    @Override
    public void aggiungiValoreAzione(Integer valoreAzione, TipoAzione tipoAzioneCorrente) {
        if (tipoAzioneCorrente==tipoAzione) valoreAzione=valoreAzione+incrementoAzione;

    }
    //Aumenta il valore di una azione specifica
}
