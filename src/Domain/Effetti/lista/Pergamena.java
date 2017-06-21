package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Azionabile;
import Domain.Giocatore;
import Domain.Risorsa;
import Domain.SpazioAzione;
import Exceptions.NetworkException;

import java.util.List;

/**
 * Created by pietro on 06/06/17.
 */
public class Pergamena extends Effetto implements Azionabile {

    private int numPergameneDiverse;


    public Pergamena(int numPergameneDiverse) {
        this.numPergameneDiverse = numPergameneDiverse;
    }

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
        lanciaPrivilegio(giocatore);
    }

    public void lanciaPrivilegio (Giocatore giocatore) {
        try {
            giocatore.SceltaPrivilegioConsiglio(numPergameneDiverse);
        } catch (NetworkException e) {
            System.out.println("Giocatore non più connesso");
        }
    }
}
