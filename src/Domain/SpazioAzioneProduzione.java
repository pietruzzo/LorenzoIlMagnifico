package Domain;

import Exceptions.DomainException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Portatile on 13/05/2017.
 */
public class SpazioAzioneProduzione extends SpazioAzione  implements Serializable {

    protected int LimiteFamiliari;
    protected List<Familiare> FamiliariPiazzati;
    protected int MalusValore;
    protected Tabellone tabellone;

    /**
     * Costruttore
     */
    public SpazioAzioneProduzione(int valore, int limiteFamiliari, int malusValore, Tabellone tabellone)
    {
        super(valore, new Risorsa());
        this.FamiliariPiazzati = new ArrayList<>();
        this.LimiteFamiliari = limiteFamiliari;
        this.MalusValore = malusValore;
        this.tabellone = tabellone;
        this.Tipo = TipoSpazioAzione.Produzione;
    }

    /**
     * Consente di piazzare un familiare nello spazioAzione, previa verifica
     */
    @Override
    public void PiazzaFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException {
        this.ValidaPiazzamentoFamiliare(familiare, servitoriAggiunti);
        familiare.Giocatore.OttieniBonusRisorse(new Risorsa(0,0,0,2,0,1,0));
        super.PiazzaFamiliare(familiare, servitoriAggiunti);
        this.FamiliariPiazzati.add(familiare);
    }

    /** Verifica se è possibile piazzare il familiare nello spazio azione */
    @Override
    protected void ValidaPiazzamentoFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException {
        //Effettua le validazioni del tabellone
        tabellone.ValidaPiazzamentoFamiliareProduzione(familiare);

        if(familiare.Giocatore.Risorse.getServi() < servitoriAggiunti)
            throw new DomainException("Non si dispone di servitori a sufficienza!");

        super.ValidaPiazzamentoFamiliare(familiare, servitoriAggiunti);

        if(this.FamiliariPiazzati.size() >= this.LimiteFamiliari)
            throw new DomainException("E' stato raggiunto il numero massimo di familiari per questo spazio azione!");
        if(this.FamiliariPiazzati.stream().anyMatch(x -> x.Giocatore.Colore == familiare.Giocatore.Colore && x.Neutro == familiare.Neutro))
            throw new DomainException("E' già presente un familiare di questo colore in questo spazio azione!");
    }

    /**
     * Toglie tutti i familiari dallo spazio azione
     */
    @Override
    protected void RimuoviFamiliari() {
        this.FamiliariPiazzati = new ArrayList<>();
    }

    /**
     * Metodo per effettuare azioni bonus nello spazio azione Produzione, ovvero senza piazzamento familiare
     * Attiva tutti gli effetti delle carte Edificio con valore sufficiente
     * @param giocatore giocatore che effettua l'azione
     * @param valoreAzione valore dell'azione
     */
    @Override
    protected void AzioneBonusEffettuata(Giocatore giocatore, int valoreAzione, Risorsa bonusRisorse, int servitoriAggiunti) throws DomainException {
        super.ValidaValoreAzioneBonus(giocatore, valoreAzione, servitoriAggiunti);
        super.AzioneBonusEffettuata(giocatore, valoreAzione, bonusRisorse, servitoriAggiunti);
    }
}
