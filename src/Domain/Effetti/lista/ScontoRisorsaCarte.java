package Domain.Effetti.lista;

import Domain.*;
import Domain.Effetti.lista.effectInterface.Azionabile;
import Domain.Effetti.lista.effectInterface.Validabile;
import Exceptions.SpazioAzioneDisabilitatoEffettoException;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class ScontoRisorsaCarte implements Azionabile, Validabile {

        TipoCarta tipoCarta; //sul quale si applica lo sconto
        Risorsa risorseBonus;

        public ScontoRisorsaCarte(TipoCarta tipoCarta, Risorsa risorseBonus) {
            this.tipoCarta = tipoCarta;
            this.risorseBonus = risorseBonus;
        }

        @Override
        public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
            applicaBonus(costo, carteGiocatore, casella);
        }

        @Override
        public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa) throws SpazioAzioneDisabilitatoEffettoException {
            applicaBonus(costo, carteGiocatore, casella);
        }

        private void applicaBonus(Risorsa costo, List<Carta>carteGiocatore, SpazioAzione casella) {
            if (casella instanceof SpazioAzioneTorre && ((SpazioAzioneTorre) casella).getCartaAssociata()!=null) {
                SpazioAzioneTorre spazioAzioneTorre = (SpazioAzioneTorre) casella;
                Risorsa costoCarta= spazioAzioneTorre.getCartaAssociata().getCostoRisorse();
                TipoCarta tipoCartaTorre = spazioAzioneTorre.getCartaAssociata().getTipoCarta();

                costoCarta.sub(risorseBonus);
                costoCarta = Risorsa.setNegToZero(costoCarta);
                costo.sub(costoCarta);
            }
        }
    }

