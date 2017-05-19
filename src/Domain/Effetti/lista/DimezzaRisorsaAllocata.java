package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Azionabile;
import Domain.Effetti.Trigger;
import Domain.Effetti.Validabile;
import Domain.Risorsa;
import Domain.SpazioAzione;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class DimezzaRisorsaAllocata implements Validabile, Azionabile, Trigger {
    Risorsa.TipoRisorsa tipoRisorsa;
    boolean trigger=false;
    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) {
        dimezzaRisorsa(risorseAllocate, costo);
    }

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) {
        dimezzaRisorsa(risorseAllocate, costo);
    }

    private void dimezzaRisorsa(Risorsa risorseAllocate, Risorsa costo){
        if (risorseAllocate!=null && !trigger){
            costo.setRisorse(tipoRisorsa, risorseAllocate.getRisorse(tipoRisorsa)/2);
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
