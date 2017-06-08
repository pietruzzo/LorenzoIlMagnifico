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
    boolean costo;


    public ScambiaRisorse(Risorsa[]costi, Risorsa[]guadagni, boolean costo) {
        opzioni= new Opzioni(costi, guadagni, null);
        this.costo = costo;
    }

    /**
     * Permette di settare l'opzione di default per lo scambio di risorse
     * @param scelta indice della scelta
     */
    public void SetDefaultChoice(Integer scelta)
    {
        if (this.costo = true && scelta ==null) throw new IllegalArgumentException("non puoi deselezionare un costo!");
        this.opzioni.setOpzione(scelta);
    }

    public boolean isCosto() {
        return costo;
    }

    public Risorsa[] getSelectedOption(){
        return opzioni.getOpzione();
    }

    @Override
    public void aziona(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa, Giocatore giocatore) {

        Risorsa[] opzioneScelta = opzioni.getOpzione();
        costo.add(opzioneScelta[0]);

        if(opzioneScelta[1].getMonete()==Short.MAX_VALUE){
            new Pergamena(1).lanciaPrivilegio(giocatore);
        } else{
            costo.sub(opzioneScelta[1]);
            //Calcolo ed aggiunta del malusAttivazioneBonus SSE non è un costo
            if(!this.isCosto()){
                Risorsa malus = applicaMalus(opzioneScelta[1], malusRisorsa);
                costo.add(malusRisorsa);
            }
        }

    }

    @Override
    public void valida(Risorsa costo, int valoreAzione, SpazioAzione casella, List<Carta> carteGiocatore, Risorsa risorseAllocate, Risorsa malusRisorsa) throws SpazioAzioneDisabilitatoEffettoException {
        Risorsa[] opzioneScelta = opzioni.getOpzione();
        costo.add(opzioneScelta[0]);
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
     * @param defaultChoice @Nullable, se c'è una sola opzione, viene settata come default
     */
    public Opzioni(Risorsa[] pagamento, Risorsa[] guadagno, Integer defaultChoice) {
        if (pagamento.length != guadagno.length)
            throw new IllegalArgumentException("guadagno e pagamento devono aver la stessa dimensione");

        if(pagamento.length == 1) this.opzione = 0;
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
            return null;
        }

        opzioneCorrente[0] = pagamento[opzione];
        opzioneCorrente[1] = guadagno[opzione];
        return opzioneCorrente;
    }
}