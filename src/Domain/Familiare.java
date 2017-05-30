package Domain;

import java.awt.*;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Familiare {

    //region Proprieta
    protected Giocatore Giocatore;
    protected ColoreDado ColoreDado;
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
            this.Giocatore.OttieniBonusSpazioAzione(SpazioAzioneAttuale.BonusRisorse);
    }
}

