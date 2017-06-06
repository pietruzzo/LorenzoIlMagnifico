package Domain.Effetti.lista;

import Domain.*;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Azionabile;
import Exceptions.NetworkException;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class EffettuaAzioneSpecifica extends Effetto implements Azionabile {
    TipoAzione azione;
    Risorsa bonusPerAzioneSpecifica;

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
        try {
            giocatore.EffettuaAzioneBonus(this.azione, valoreAzione, bonusPerAzioneSpecifica);
        } catch (NetworkException e) {
            System.out.println("Giocatore non più connesso");
        }
    }
}
