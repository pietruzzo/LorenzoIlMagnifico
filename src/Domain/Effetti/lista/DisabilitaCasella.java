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
public class DisabilitaCasella implements Validabile {
    List<SpazioAzione> caselleDisabilitate;

    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) throws SpazioAzioneDisabilitatoEffettoException{
        if (casella!=null && caselleDisabilitate.contains(casella)) throw new SpazioAzioneDisabilitatoEffettoException();
    }
}
