package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.lista.effectInterface.Azionabile;
import Domain.Risorsa;
import Domain.SpazioAzione;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class BonusRisorse implements Azionabile{

    private Risorsa bonusRisorse;

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) {
        costo.sub(bonusRisorse);
    }
}
