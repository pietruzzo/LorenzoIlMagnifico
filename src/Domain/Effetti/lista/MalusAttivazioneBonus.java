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
public class MalusAttivazioneBonus extends Effetto implements Validabile {

    Risorsa malusRisorsa; //corrispettivi da sottrarre tutte le volte che guadagno una certa risorsa (Bonus casella, effetti...)

    public MalusAttivazioneBonus(Risorsa malusRisorsa) {
        this.malusRisorsa = malusRisorsa;
    }

    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa) throws SpazioAzioneDisabilitatoEffettoException {
        malusRisorsa.add(this.malusRisorsa);
    }

}
