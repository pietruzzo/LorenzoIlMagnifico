package Domain.Effetti.lista;

import Domain.Carta;
import Domain.Effetti.Effetto;
import Domain.Effetti.lista.effectInterface.EndGame;
import Domain.Risorsa;
import Domain.TipoCarta;

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

    public EffettoEndGame(int pVittoria) {
        this.pVittoria = pVittoria;
        noPVittoria=null;
        pVittoriaXRisorsa=null;
        scalaCarteImpresa=false;
    }

    public EffettoEndGame(TipoCarta noPVittoria) {
        this.noPVittoria = noPVittoria;
        pVittoria=0;
        pVittoriaXRisorsa=null;
        scalaCarteImpresa=false;
    }

    public EffettoEndGame(Risorsa pVittoriaXRisorsa) {
        this.pVittoriaXRisorsa = pVittoriaXRisorsa;
        pVittoria=0;
        noPVittoria=null;
        scalaCarteImpresa=false;
    }

    public EffettoEndGame(boolean scalaCarteImpresa) {
        this.pVittoriaXRisorsa = null;
        pVittoria=0;
        noPVittoria=null;
        scalaCarteImpresa=true;
    }

    @Override
    public void azioneTerminale(Risorsa risorseGiocatore, List<Carta> listaCarte, AtomicInteger modificaPVittoria) {

        //Aumento i punti vittoria ogni PVittoriaXRisorsa
        for (Risorsa.TipoRisorsa tipo : Risorsa.TipoRisorsa.values()) {
            if (pVittoriaXRisorsa.getRisorse(tipo) != 0) {
                modificaPVittoria.set(modificaPVittoria.get() + (risorseGiocatore.getRisorse(tipo) / pVittoriaXRisorsa.getRisorse(tipo)));
            }
        }

        //Aggiungo i punti vittoria
        modificaPVittoria.set(modificaPVittoria.get()+ pVittoria);
    }


}
