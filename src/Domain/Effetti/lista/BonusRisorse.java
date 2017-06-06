package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Azionabile;
import Domain.Giocatore;
import Domain.Risorsa;
import Domain.SpazioAzione;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class BonusRisorse extends Effetto implements Azionabile{

    private Risorsa bonusRisorse;

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
        Risorsa malus=applicaMalus(bonusRisorse, malusRisorsa);
        costo= Risorsa.sub(costo, bonusRisorse);
        costo= Risorsa.add(costo, malus);
    }
}
