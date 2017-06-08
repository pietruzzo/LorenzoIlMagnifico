package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Azionabile;
import Domain.Effetti.lista.effectInterface.Trigger;
import Domain.Effetti.lista.effectInterface.Validabile;
import Domain.Giocatore;
import Domain.Risorsa;
import Domain.SpazioAzione;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class DimezzaRisorsaAllocata extends Effetto implements Validabile, Azionabile, Trigger {
    Risorsa.TipoRisorsa tipoRisorsa;
    boolean trigger;

    public DimezzaRisorsaAllocata(Risorsa.TipoRisorsa tipoRisorsa) {
        this.tipoRisorsa = tipoRisorsa;
        this.trigger = false;
    }

    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa) {
        dimezzaRisorsa(risorseAllocate, costo);
    }

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
        dimezzaRisorsa(risorseAllocate, costo);
    }

    private void dimezzaRisorsa(Risorsa risorseAllocate, Risorsa costo){
        if (risorseAllocate!=null && !trigger){
            costo= costo.setRisorse(tipoRisorsa, risorseAllocate.getRisorse(tipoRisorsa)/2);
            setTrigger(true);
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
