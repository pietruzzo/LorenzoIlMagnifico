package Domain;

import Exceptions.DomainException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Portatile on 13/05/2017.
 */
public class SpazioAzioneConsiglio extends SpazioAzione  implements Serializable {

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
    @Override
    public void PiazzaFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException
    {
        super.ValidaPiazzamentoFamiliare(familiare, servitoriAggiunti);
        super.PiazzaFamiliare(familiare, servitoriAggiunti);
        this.FamiliariPiazzati.add(familiare);
    }


    /**
     * Toglie tutti i familiari dallo spazio azione
     */
    @Override
    protected void RimuoviFamiliari() {
        this.FamiliariPiazzati = new ArrayList<>();
    }
}
