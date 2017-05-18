package Domain.Effetti;

import Domain.TipoAzione;

/**
 * Created by pietro on 18/05/17.
 */
public interface ModificaValoreAzione {

    public void aggiungiValoreAzione(Integer valoreAzione, TipoAzione tipoAzioneCorrente);
}
