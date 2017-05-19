package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Validabile;
import Domain.Risorsa;
import Domain.SpazioAzione;
import Exceptions.SpazioAzioneDisabilitatoEffettoException;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class MalusAttivazioneBonus implements Validabile {

    Risorsa malusRisorsa; //corrispettivi da sottrarre tutte le volte che guadagno una certa risorsa (Bonus casella, effetti...)

    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) throws SpazioAzioneDisabilitatoEffettoException {
        //TODO set parametri in gestore Risorse
    }

}
