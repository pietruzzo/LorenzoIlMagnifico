package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Azionabile;
import Domain.Effetti.Trigger;
import Domain.Effetti.Validabile;
import Domain.Risorsa;
import Domain.SpazioAzione;
import Domain.SpazioAzioneTorre;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class AnnullaBonusSpazioAz implements Validabile, Azionabile, Trigger{
    boolean trigger= false;
    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) {
        scalaBonus(costo, casella);
    }

    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) {
        scalaBonus(costo, casella);
    }

    /**
     * @param costo dal quale sarà scalato il bonusCasella (Viene sovrascritto)
     * @param casella dal quale si ottiene il bonusCasella
     */
    private void scalaBonus(Risorsa costo, SpazioAzione casella){
        if ((casella instanceof SpazioAzioneTorre) && (casella != null) && !trigger){
            SpazioAzioneTorre spazioAzioneTorre = (SpazioAzioneTorre) casella;
            Risorsa bonusRisorse=spazioAzioneTorre.getBonusRisorse();
            costo.add(bonusRisorse);
            this.trigger=true;
        }
    }

    @Override
    public boolean isTriggered() {
        return trigger;
    }

    @Override
    public void setDefaultTrigger() {
        trigger=false;
    }

    @Override
    public void setTrigger(boolean stato) {
        trigger=stato;
    }
}
