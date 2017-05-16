package Domain;

import java.awt.*;
import java.util.*;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Giocatore {

    //region Proprietà
    protected String Nome;
    protected Color Colore;
    protected java.util.List<CartaTerritorio> CarteTerritorio;
    protected java.util.List<CartaEdificio> CarteEdificio;
    protected java.util.List<CartaPersonaggio> CartePersonaggio;
    protected java.util.List<CartaImpresa> CarteImpresa;
    protected java.util.List<Familiare> Familiari;

    protected Risorsa Risorse;
    //endregion

    /**
     * Costruttore
     */
    public Giocatore(String nome, Color colore, int monete)
    {
        this.Nome = nome;
        this.Colore = colore;

        this.Risorse = new Risorsa(2, 2, 3, monete, 0, 0, 0 );

        this.CarteTerritorio = new ArrayList<>();
        this.CarteEdificio = new ArrayList<>();
        this.CartePersonaggio = new ArrayList<>();
        this.CarteImpresa = new ArrayList<>();

        //region Inizializzazione Familiari
        //Inizializzazione Familiari
        this.Familiari = new ArrayList<>();
        Color coloreFamiliare = null;
        Boolean neutro = false;

        for(int i = 0; i <= 3; i++)
        {
            switch (i)
            {
                case 0:
                    coloreFamiliare = Color.WHITE;
                    break;

                case 1:
                    coloreFamiliare = Color.ORANGE;
                    break;

                case 2:
                    coloreFamiliare = Color.BLACK;
                    break;

                case 3:
                    neutro = true;
                    break;
            }
            this.Familiari.add(new Familiare(this, coloreFamiliare, neutro));
        }
        //endregion
    }

    /**
     * Incrementa le risorse ottenute da un familiare su uno spazio azione
     */
    public void OttieniBonusSpazioAzione(Risorsa risorseSpazioAzione)
    {
        this.Risorse.add(risorseSpazioAzione);
    }

    /**
     * Paga le risorse necessarie per prendere una carta
     */
    public void PagaRisorse(Risorsa costoRisorse)
    {
        this.Risorse.sub(costoRisorse);
    }

}

