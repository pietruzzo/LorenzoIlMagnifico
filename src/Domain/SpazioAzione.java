package Domain;

import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class SpazioAzione {

    //region Proprieta
    protected int Valore;
    protected TipoSpazioAzione Tipo;
    protected Risorsa BonusRisorse;

    //endregion

    protected SpazioAzione(){};

    /**
     * Costruttore SpazioAzione
     */
    protected SpazioAzione(int valore, Risorsa bonusRisorse)
    {
        this.Valore = valore;
        this.BonusRisorse = bonusRisorse;
    }

    /**
     * @return copia di BonusRisorse
     */
    public Risorsa getBonusRisorse() {
        return new Risorsa(BonusRisorse.getArrayRisorse());
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
        //TODO aggiungere bonus effetti permanent, carte scomunica
        if(familiare.Valore < this.Valore)
            throw new Exception(String.format("E' necessario un valore di almeno {0} per poter piazzare un familiare!", this.Valore));
    }
}
