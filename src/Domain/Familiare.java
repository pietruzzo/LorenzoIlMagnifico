package Domain;

import java.io.Serializable;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Familiare  implements Serializable {

    //region Proprieta
    protected Giocatore Giocatore;
    protected ColoreDado ColoreDado;
    protected Boolean Neutro;
    private int Valore;
    protected SpazioAzione SpazioAzioneAttuale;

    public void SetSpazioAzioneAttuale(SpazioAzione spazioAzione)
    {
        this.SpazioAzioneAttuale = spazioAzione;
    }
    public int getValore() { return Valore; }
    public void setValore(int valore) { Valore = valore;  }
    //endregion

    /**
     * Costruttore
     */
    public Familiare(Giocatore giocatore, ColoreDado coloreDado)
    {
        this.Giocatore = giocatore;
        this.ColoreDado = coloreDado;

        if(coloreDado == ColoreDado.NEUTRO)
            this.Neutro = true;
        else
            this.Neutro = false;

        this.Valore = 0;
    }

    /**
     * Ottiene il bonus derivato dallo spazio azione
     */
    public void OttieniBonusSpazioAzione()
    {
        if(this.SpazioAzioneAttuale != null)
            this.Giocatore.OttieniBonusRisorse(SpazioAzioneAttuale.BonusRisorse);
    }
}

