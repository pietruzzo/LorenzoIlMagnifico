package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Azionabile;
import Domain.Risorsa;
import Domain.SpazioAzione;
import Domain.TipoAzione;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class EffettuaAzioneSpecifica implements Azionabile {
    TipoAzione azione;

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) {
        //TODO scegliere come implementare un'azione a fine turno
    }
}
