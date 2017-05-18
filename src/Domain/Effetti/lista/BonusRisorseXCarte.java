package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Azionabile;
import Domain.Risorsa;
import Domain.SpazioAzione;
import Domain.TipoCarta;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 * Bonus Risorse moltiplicato per le carte di un certo tipo
 */
public class BonusRisorseXCarte implements Azionabile{

    TipoCarta tipoCarta;
    int fattoreMoltiplicatore;
    Risorsa risorseBonus;

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore) {
        int numCarte=0;
        for (Carta carta: carteGiocatore) {
            if(carta.getTipoCarta()==tipoCarta){
                numCarte=+1;
            }
        }
        costo=Risorsa.sub(costo, costo.multScalare(numCarte));
    }
}
