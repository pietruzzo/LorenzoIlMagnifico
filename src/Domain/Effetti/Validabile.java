package Domain.Effetti;

import Domain.Risorsa;
import Domain.SpazioAzione;

/**
 * Created by pietro on 18/05/17.
 */
public interface Validabile {
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella);
}
