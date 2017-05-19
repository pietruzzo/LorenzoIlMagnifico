package Domain.Effetti.lista;

import Domain.*;
import Domain.Effetti.Azionabile;
import Domain.Effetti.Validabile;
import Exceptions.SpazioAzioneDisabilitatoEffettoException;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class ScontoRisorseXCarte {
    //Come bonusRisorseXCarte ma viene conteggiato in validazione
    public class BonusRisorseXCarte implements Azionabile, Validabile {

        TipoCarta tipoCarta;
        int fattoreMoltiplicatore;
        Risorsa risorseBonus;

        @Override
        public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) {
            applicaBonus(costo, carteGiocatore, casella);
        }

        @Override
        public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate) throws SpazioAzioneDisabilitatoEffettoException {
            applicaBonus(costo, carteGiocatore, casella);
        }

        private void applicaBonus(Risorsa costo, List<Carta>carteGiocatore, SpazioAzione casella) {
            if (casella instanceof SpazioAzioneTorre) {
                SpazioAzioneTorre spazioAzioneTorre = (SpazioAzioneTorre) casella;
                Risorsa costoCarta= spazioAzioneTorre.getCartaAssociata().getCostoRisorse();

                int numCarte = 0;
                for (Carta carta : carteGiocatore) {
                    if (carta.getTipoCarta() == tipoCarta) {
                        numCarte = +1;
                    }
                }
                costoCarta = Risorsa.sub(costoCarta, costo.multScalare(numCarte));
                costoCarta = Risorsa.setNegToZero(costoCarta);
                costo = Risorsa.sub(costo, costoCarta);
            }
        }
    }

}
