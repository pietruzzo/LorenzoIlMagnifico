package Domain;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Giocatore {

    //region Proprietà
    protected String Nome;
    protected Color Colore;
    protected List<CartaTerritorio> CarteTerritorio;
    protected List<CartaEdificio> CarteEdificio;
    protected List<CartaPersonaggio> CartePersonaggio;
    protected List<CartaImpresa> CarteImpresa;
    protected List<Familiare> Familiari;
    protected List<Carta> CarteScomunica; //TODO: Carte Scomunica?

    protected Risorsa Risorse;
    //endregion

    /**
     * Costruttore
     */
    protected Giocatore( )
    {
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
     *  Setta le proprietà al login del giocatore
     */
    public void SettaProprietaIniziali(String nome, Color colore, int monete)
    {
        this.Nome = nome;
        this.Colore = colore;

        this.Risorse = new Risorsa(2, 2, 3, monete, 0, 0, 0 );
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

    public List<Carta> getListaCarte(){
        List<Carta> listaCarte=new ArrayList<Carta>();
        listaCarte.addAll(CarteTerritorio);
        listaCarte.addAll(CarteEdificio);
        listaCarte.addAll(CartePersonaggio);
        listaCarte.addAll(CarteImpresa);
        listaCarte.addAll(CarteScomunica);
        return  listaCarte;
    }
}

