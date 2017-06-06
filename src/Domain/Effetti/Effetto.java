package Domain.Effetti;

import Domain.Risorsa;

import java.io.Serializable;

/**
 * Created by pietro on 18/05/17.
 */
public abstract class Effetto  implements Serializable {

    /**
     * @param bonus
     * @param malusRisorsa
     * @return costo derivante dal malusRisorsa
     * @apiNote per calcolare il malus derivante da malusAttivazioneBonus in fase di Attivazione
     * @implSpec costo ritornato >=0
     */
    public Risorsa applicaMalus(Risorsa bonus, Risorsa malusRisorsa){
        Risorsa risultato = Risorsa.sub(bonus, malusRisorsa);
        risultato=Risorsa.setNegToZero(risultato);
        return risultato;
    }
}
