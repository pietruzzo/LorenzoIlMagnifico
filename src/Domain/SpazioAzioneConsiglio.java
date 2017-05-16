package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Portatile on 13/05/2017.
 */
public class SpazioAzioneConsiglio extends SpazioAzione {

    protected List<Familiare> FamiliariPiazzati;

    /**
     * Costruttore
     */
    public SpazioAzioneConsiglio(int valore, Risorsa bonusRisorse)
    {
        super(valore, bonusRisorse);
        this.FamiliariPiazzati = new ArrayList<>();
        this.Tipo = TipoSpazioAzione.Consiglio;
    }

    /**
     * Consente di piazzare un familiare nello spazioAzione, previa verifica
     */
    public void PiazzaFamiliare(Familiare familiare) throws Exception {
        super.ValidaPiazzamentoFamiliare(familiare);
        this.FamiliariPiazzati.add(familiare);
        super.PiazzaFamiliare(familiare);
    }
}
