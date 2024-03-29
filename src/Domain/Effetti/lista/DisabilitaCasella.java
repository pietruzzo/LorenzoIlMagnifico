package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Validabile;
import Domain.Risorsa;
import Domain.SpazioAzione;
import Exceptions.SpazioAzioneDisabilitatoEffettoException;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class DisabilitaCasella extends Effetto implements Validabile {
    List<SpazioAzione> caselleDisabilitate;

    public DisabilitaCasella(List<SpazioAzione> caselleDisabilitate) {
        this.caselleDisabilitate = caselleDisabilitate;
    }

    public DisabilitaCasella(Integer[] caselle) {
        //TODO
    }

    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa) throws SpazioAzioneDisabilitatoEffettoException{
        if (casella!=null && caselleDisabilitate.contains(casella)) throw new SpazioAzioneDisabilitatoEffettoException("Questa casella è disabilitata per effetto di una carta!");
    }
}
