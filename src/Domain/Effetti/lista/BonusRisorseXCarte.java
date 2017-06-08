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
    Risorsa risorseBonus;

    public BonusRisorseXCarte(TipoCarta tipoCarta, Risorsa risorseBonus) {
        this.tipoCarta = tipoCarta;
        this.risorseBonus = risorseBonus;
    }

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
        int numCarte=0;
        for (Carta carta: carteGiocatore) {
            if(carta.getTipoCarta()==tipoCarta){
                numCarte=+1;
            }
        }
        Risorsa malus= applicaMalus(risorseBonus.multScalare(numCarte), malusRisorsa);
        costo.sub(risorseBonus.multScalare(numCarte));
        costo.add(malus);
    }
}
