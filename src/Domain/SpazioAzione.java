package Domain;

import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class SpazioAzione {

    //region Proprieta
    protected int Valore;
    protected TipoSpazioAzione Tipo;

    protected int BonusLegni;
    protected int BonusPietre;
    protected int BonusServitori;
    protected int BonusMonete;
    protected int BonusMilitare;

    //endregion

    protected SpazioAzione(){};

    /**
     * Costruttore SpazioAzione
     */
    protected SpazioAzione(int valore, int bonusLegni, int bonusPietre, int bonusServitori, int bonusMonete, int bonusMilitare)
    {
        this.Valore = valore;
        this.BonusLegni = bonusLegni;
        this.BonusPietre = bonusPietre;
        this.BonusServitori = bonusServitori;
        this.BonusMonete = bonusMonete;
        this.BonusMilitare = bonusMilitare;
    }

    /**
     * Metodo base per aggiornare i parametri del giocatore in funzione dei bonus dello spazio azione
     */
    public void PiazzaFamiliare(Familiare familiare) throws Exception {
        familiare.SetSpazioAzioneAttuale(this);
        familiare.OttieniBonusSpazioAzione();
    }

    /**
     * Validazione base per poter piazzare un familiare
      */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare) throws Exception {
        if(familiare.Valore < this.Valore)
            throw new Exception(String.format("E' necessario un valore di almeno {0} per poter piazzare un familiare!", this.Valore));
    }
}
