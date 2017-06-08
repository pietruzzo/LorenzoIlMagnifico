package Domain.Effetti.lista;

import Domain.*;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Azionabile;

import java.util.List;

/**
 * Created by pietro on 08/06/17.
 */
public class BonusRisorseXRisorse extends Effetto implements Azionabile{
    Risorsa.TipoRisorsa tipoRisorsa;
    Risorsa risorseBonus;

    public BonusRisorseXRisorse(Risorsa.TipoRisorsa tipoRisorsa, Risorsa risorseBonus) {
        this.tipoRisorsa=tipoRisorsa;
        this.risorseBonus = risorseBonus;
    }

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
        Risorsa bonus = risorseBonus.multScalare(giocatore.getRisorse().getRisorse(tipoRisorsa));
        Risorsa malus= applicaMalus(bonus, malusRisorsa);
        costo.add(malus);
        costo.sub(bonus);
    }
}
