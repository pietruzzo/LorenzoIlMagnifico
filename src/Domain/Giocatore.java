package Domain;

import jdk.management.resource.NotifyingMeter;

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

    protected int Legni;
    protected int Pietre;
    protected int Servitori;
    protected int Monete;

    protected int PuntiFede;
    protected int PuntiMilitari;
    protected int PuntiVittoria;
    //endregion

    /**
     * Costruttore
     */
    public Giocatore(String nome, Color colore, int monete)
    {
        this.Nome = nome;
        this.Colore = colore;
        this.Monete = monete;
        this.Legni = 2;
        this.Pietre = 2;
        this.Servitori = 3;
        this.PuntiFede = 0;
        this.PuntiMilitari = 0;
        this.PuntiVittoria = 0;

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
    public void OttieniBonusSpazioAzione(int legni, int pietre, int servitori, int monete, int puntiMilitari)
    {
        this.Legni += legni;
        this.Pietre += pietre;
        this.Servitori += servitori;
        this.Monete += monete;
        this.PuntiMilitari += puntiMilitari;
    }

    /**
     * Paga le risorse necessarie per prendere una carta
     */
    public void PagaRisorse(int legni, int pietre, int servitori, int monete)
    {
        this.Legni -= legni;
        this.Pietre -= pietre;
        this.Servitori -= servitori;
        this.Monete -= monete;
    }

    /**
     * Paga i punti militari necessari per prendere una carta
     */
    public void PagaPuntiMilitari(int puntiMilitari)
    {
        this.PuntiMilitari -= puntiMilitari;
    }
}

