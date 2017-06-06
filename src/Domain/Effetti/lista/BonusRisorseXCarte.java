package Domain.Effetti.lista;

import Domain.*;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Azionabile;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 * Bonus Risorse moltiplicato per le carte di un certo tipo
 */
public class BonusRisorseXCarte extends Effetto implements Azionabile{

    TipoCarta tipoCarta;
    int fattoreMoltiplicatore;
    Risorsa risorseBonus;

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
        int numCarte=0;
        for (Carta carta: carteGiocatore) {
            if(carta.getTipoCarta()==tipoCarta){
                numCarte=+1;
            }
        }
        Risorsa malus= applicaMalus(risorseBonus.multScalare(numCarte), malusRisorsa);
        costo=Risorsa.sub(costo, risorseBonus.multScalare(numCarte));
        costo = Risorsa.add(costo, malus);
    }
}
