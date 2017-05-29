package Domain;

import Exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Portatile on 13/05/2017.
 */
public class SpazioAzioneRaccolto extends SpazioAzione {

    protected int LimiteFamiliari;
    protected List<Familiare> FamiliariPiazzati;
    protected int MalusValore;

    /**
     * Costruttore
     */
    public SpazioAzioneRaccolto(int valore, int limiteFamiliari, int malusValore)
    {
        super(valore, new Risorsa());
        this.FamiliariPiazzati = new ArrayList<>();
        this.LimiteFamiliari = limiteFamiliari;
        this.MalusValore = malusValore;
        this.Tipo = TipoSpazioAzione.Raccolto;
    }

    /**
     * Consente di piazzare un familiare nello spazioAzione, previa verifica
     */
    public void PiazzaFamiliare(Familiare familiare) throws DomainException {
        this.ValidaPiazzamentoFamiliare(familiare);
        super.PiazzaFamiliare(familiare);
        this.FamiliariPiazzati.add(familiare);
    }

    /** Verifica se è possibile piazzare il familiare nello spazio azione */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare) throws DomainException {
        if(this.FamiliariPiazzati.size() >= this.LimiteFamiliari)
            throw new DomainException("E' stato raggiunto il numero massimo di familiari per questo spazio azione!");
        if(this.FamiliariPiazzati.stream().anyMatch(x -> x.Giocatore.Colore == familiare.Giocatore.Colore && x.Neutro == familiare.Neutro))
            throw new DomainException("E' già presente un familiare di questo colore in questo spazio azione!");
        super.ValidaPiazzamentoFamiliare(familiare);
    }
}
