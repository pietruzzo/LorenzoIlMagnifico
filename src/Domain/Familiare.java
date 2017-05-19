package Domain;

import java.awt.*;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Familiare {

    //region Proprieta
    protected Giocatore Giocatore;
    protected Color ColoreDado;
    protected Boolean Neutro;
    protected int Valore;
    protected SpazioAzione SpazioAzioneAttuale;

    public void SetSpazioAzioneAttuale(SpazioAzione spazioAzione)
    {
        this.SpazioAzioneAttuale = spazioAzione;
    }
    //endregion

    /**
     * Costruttore
     */
    public Familiare(Giocatore giocatore, Color coloreDado, Boolean neutro)
    {
        this.Giocatore = giocatore;
        this.ColoreDado = coloreDado;
        this.Neutro = neutro;
        this.Valore = 0;
    }

    /**
     * Ottiene il bonus derivato dallo spazio azione
     */
    public void OttieniBonusSpazioAzione()
    {
        if(this.SpazioAzioneAttuale != null)
            this.Giocatore.OttieniBonusSpazioAzione(SpazioAzioneAttuale.BonusRisorse);
    }
}

