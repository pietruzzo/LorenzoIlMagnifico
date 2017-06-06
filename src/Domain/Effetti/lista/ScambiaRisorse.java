package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.Azionabile;
import Domain.Effetti.lista.effectInterface.Validabile;
import Domain.Giocatore;
import Domain.Risorsa;
import Domain.SpazioAzione;
import Exceptions.SpazioAzioneDisabilitatoEffettoException;

import java.util.List;

/**
 * Created by pietro on 18/05/17.
 */
public class ScambiaRisorse extends Effetto implements Validabile, Azionabile {
    Opzioni opzioni;

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {
        Risorsa[] opzioneScelta = opzioni.getOpzione();
        costo = Risorsa.add(costo, opzioneScelta[0]);
        costo = Risorsa.sub(costo, opzioneScelta[1]);
        //Calcolo ed aggiunta del malusAttivazioneBonus
        Risorsa malus = applicaMalus(opzioneScelta[1], malusRisorsa);
        costo = Risorsa.add(costo, malusRisorsa);
    }

    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa) throws SpazioAzioneDisabilitatoEffettoException {
        Risorsa[] opzioneScelta = opzioni.getOpzione();
        costo = Risorsa.add(costo, opzioneScelta[0]);
    }
    //Anche più possibilità


}

class Opzioni {
    Risorsa[] pagamento;
    Risorsa[] guadagno;
    Integer opzione;

    /**
     *
     * @param pagamento
     * @param guadagno
     * @param defaultChoice @Nullable
     */
    public Opzioni(Risorsa[] pagamento, Risorsa[] guadagno, Integer defaultChoice) {
        if (pagamento.length != guadagno.length)
            throw new IllegalArgumentException("guadagno e pagamento devono aver la stessa dimensione");
        this.pagamento = pagamento;
        this.guadagno = guadagno;
        this.opzione = defaultChoice;
    }

    public void setOpzione(Integer opzione) {
        if(opzione==null){
            this.opzione = null;
        } else if (opzione < 0 || opzione > pagamento.length) {
            throw new ArrayIndexOutOfBoundsException("l'opzione scelta per scambia risorse non c'è: " + opzione);
        } else {
            this.opzione = opzione;
        }
    }

    public Risorsa[] getOpzione() {
        Risorsa[] opzioneCorrente = new Risorsa[2];
        if (opzione==null){
            opzioneCorrente[0]= new Risorsa();
            opzioneCorrente[1]= new Risorsa();
            return opzioneCorrente;
        }

        opzioneCorrente[0] = pagamento[opzione];
        opzioneCorrente[1] = guadagno[opzione];
        return opzioneCorrente;
    }
}