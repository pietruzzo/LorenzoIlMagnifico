package Domain.Effetti.lista;

import Domain.*;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Azionabile;

import java.util.List;

/**
 * Created by pietro on 08/06/17.
 */
public class BonusRisorseXRisorse extends Effetto implements Azionabile{
    /*
    Risorsa.TipoRisorsa tipoRisorsa;
    Risorsa risorseBonus;
    */
    Risorsa.TipoRisorsa origine;
    Risorsa.TipoRisorsa bonus;
    Double fattoreBonus;

    /*
    public BonusRisorseXRisorse(Risorsa.TipoRisorsa tipoRisorsa, Risorsa risorseBonus) {
        this.tipoRisorsa=tipoRisorsa;
        this.risorseBonus = risorseBonus;
    }
    */

    public BonusRisorseXRisorse(Risorsa.TipoRisorsa tipoRisorsaOrigine, Risorsa.TipoRisorsa tipoRisorsaBonus, Double fattore) {
        this.origine=tipoRisorsaOrigine;
        this.bonus= tipoRisorsaBonus;
        this.fattoreBonus= fattore;
    }
    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {

        //Risorsa bonus = risorseBonus.multScalare(giocatore.getRisorse().getRisorse(tipoRisorsa));
        Risorsa bonus = new Risorsa(this.bonus, (int) (giocatore.getRisorse().getRisorse(origine)*fattoreBonus));
        Risorsa malus= applicaMalus(bonus, malusRisorsa);
        costo.add(malus);
        costo.sub(bonus);
    }
}
