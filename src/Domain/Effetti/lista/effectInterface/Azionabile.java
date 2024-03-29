package Domain.Effetti.lista.effectInterface;

import Domain.Carta;
import Domain.Giocatore;
import Domain.Risorsa;
import Domain.SpazioAzione;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public interface Azionabile {
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore);
}
