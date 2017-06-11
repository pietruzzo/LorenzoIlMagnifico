package Domain.Effetti.lista;

import Domain.*;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.EndGame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pietro on 18/05/17.
 * Esempio di classe totalmente generica che viene eseguita a fine gioco
 */
public class EffettoEndGame extends Effetto implements EndGame {

    private int pVittoria;
    private TipoCarta noPVittoria;
    private Risorsa pVittoriaXRisorsa;
    private boolean scalaCarteImpresa;
    private Risorsa scalaXTipoCarta;
    private TipoCarta tipoDaScalare;

    public EffettoEndGame(int pVittoria) {
        this.pVittoria = pVittoria;
        noPVittoria=null;
        pVittoriaXRisorsa=null;
        scalaCarteImpresa=false;
        this.tipoDaScalare=null;
        this.scalaXTipoCarta=null;
    }

    public EffettoEndGame(TipoCarta noPVittoria) {
        this.noPVittoria = noPVittoria;
        pVittoria=0;
        pVittoriaXRisorsa=null;
        scalaCarteImpresa=false;
        this.tipoDaScalare=null;
        this.scalaXTipoCarta=null;
    }

    public EffettoEndGame(Risorsa pVittoriaXRisorsa) {
        this.pVittoriaXRisorsa = pVittoriaXRisorsa;
        pVittoria=0;
        noPVittoria=null;
        scalaCarteImpresa=false;
        this.tipoDaScalare=null;
        this.scalaXTipoCarta=null;
    }

    public EffettoEndGame(boolean scalaCarteImpresa) {
        this.pVittoriaXRisorsa = null;
        pVittoria=0;
        noPVittoria=null;
        scalaCarteImpresa=true;
        this.tipoDaScalare=null;
        this.scalaXTipoCarta=null;
    }

    public EffettoEndGame(Risorsa risorsa, TipoCarta tipoCarta){
        this.tipoDaScalare=tipoCarta;
        this.scalaXTipoCarta=risorsa;
        this.pVittoriaXRisorsa = null;
        pVittoria=0;
        noPVittoria=null;
        scalaCarteImpresa=false;

    }

    @Override
    public void azioneTerminale(Risorsa risorseGiocatore, List<Carta> listaCarte, AtomicInteger modificaPVittoria, Giocatore giocatore) {
        //Aumento i punti vittoria ogni PVittoriaXRisorsa
        if(pVittoriaXRisorsa!=null){
        for (Risorsa.TipoRisorsa tipo : Risorsa.TipoRisorsa.values()) {
            if (pVittoriaXRisorsa.getRisorse(tipo) != 0) {
                modificaPVittoria.set(modificaPVittoria.get() + (risorseGiocatore.getRisorse(tipo) / pVittoriaXRisorsa.getRisorse(tipo)));
            }
        }}

        //Aggiungo i punti vittoria
        modificaPVittoria.set(modificaPVittoria.get()+ pVittoria);

        //Scala carte impresa
        if (scalaCarteImpresa) modificaPVittoria.set(modificaPVittoria.get()-giocatore.getCarteImpresa().size());

        //noPuntiVittoria //TODO calcolo dei punti vittoria per il TipoCarta "noPVittoria" da togliere a modificaPVittoria

        //punti vittoriaXCostoCarteImpresa (ridotta a funzionare solo per carte edificio)
       if(tipoDaScalare!=null && tipoDaScalare != TipoCarta.Scomunica && scalaXTipoCarta!=null){
           List<Carta> carteDaControllare = new ArrayList<>();
           for (Carta c: giocatore.getListaCarte()){
               if (c.getTipoCarta()==tipoDaScalare)
                    carteDaControllare.add(c);
           }
           scalaPuntiPerCostoCarte(modificaPVittoria, carteDaControllare);
       }

    }

    private void scalaPuntiPerCostoCarte(AtomicInteger modificaPVittoria, List<Carta> carteDaControllare){

        for(Carta c : carteDaControllare){
            Risorsa costoCarta= c.getCostoRisorse();

            if(costoCarta.getRisorse(Risorsa.TipoRisorsa.LEGNO)>0 && scalaXTipoCarta.getRisorse(Risorsa.TipoRisorsa.LEGNO)>0) modificaPVittoria.set(modificaPVittoria.get()-1);
            if(costoCarta.getRisorse(Risorsa.TipoRisorsa.PIETRA)>0 && scalaXTipoCarta.getRisorse(Risorsa.TipoRisorsa.PIETRA)>0) modificaPVittoria.set(modificaPVittoria.get()-1);
            if(costoCarta.getRisorse(Risorsa.TipoRisorsa.SERVI)>0 && scalaXTipoCarta.getRisorse(Risorsa.TipoRisorsa.SERVI)>0) modificaPVittoria.set(modificaPVittoria.get()-1);
            if(costoCarta.getRisorse(Risorsa.TipoRisorsa.MONETE)>0 && scalaXTipoCarta.getRisorse(Risorsa.TipoRisorsa.MONETE)>0) modificaPVittoria.set(modificaPVittoria.get()-1);
            if(costoCarta.getRisorse(Risorsa.TipoRisorsa.PVITTORIA)>0 && scalaXTipoCarta.getRisorse(Risorsa.TipoRisorsa.PVITTORIA)>0) modificaPVittoria.set(modificaPVittoria.get()-1);
            if(costoCarta.getRisorse(Risorsa.TipoRisorsa.PMILITARI)>0 && scalaXTipoCarta.getRisorse(Risorsa.TipoRisorsa.PMILITARI)>0) modificaPVittoria.set(modificaPVittoria.get()-1);
            if(costoCarta.getRisorse(Risorsa.TipoRisorsa.PFEDE)>0 && scalaXTipoCarta.getRisorse(Risorsa.TipoRisorsa.PFEDE)>0) modificaPVittoria.set(modificaPVittoria.get()-1);

        }
    }


}
