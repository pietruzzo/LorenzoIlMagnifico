package Domain.Effetti.lista;

import Domain.*;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Azionabile;
import Domain.Effetti.lista.effectInterface.Trigger;
import Domain.Effetti.lista.effectInterface.Validabile;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class AnnullaBonusSpazioAz extends Effetto implements Validabile, Azionabile, Trigger{
    boolean trigger;
    List<SpazioAzione> spaziSenzaBonus;

    public AnnullaBonusSpazioAz(List<SpazioAzione> spaziAzione) {
        this.trigger = false;
        spaziSenzaBonus=spaziAzione;
    }
    public AnnullaBonusSpazioAz(Integer[] spaziAzione) {
        this.trigger = false;
        //TODO recupera caselle
    }

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
        scalaBonus(costo, casella);
    }

    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa) {
        scalaBonus(costo, casella);
    }

    /**
     * @param costo dal quale sarà scalato il bonusCasella (Viene sovrascritto)
     * @param casella dal quale si ottiene il bonusCasella
     */
    private void scalaBonus(Risorsa costo, SpazioAzione casella){
        if ((casella != null) && (casella instanceof SpazioAzioneTorre) && spaziSenzaBonus.contains(casella) && !trigger){
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
