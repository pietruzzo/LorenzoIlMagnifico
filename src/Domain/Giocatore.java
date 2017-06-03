package Domain;

import Domain.DTO.UpdateGiocatoreDTO;
import Domain.Effetti.GestoreEffettiGiocatore;

import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Giocatore implements Serializable {

    //region Proprietà
    private short IdGiocatore;
    protected String Nome;
    protected ColoreGiocatore Colore;
    protected List<CartaTerritorio> CarteTerritorio;
    protected List<CartaEdificio> CarteEdificio;
    protected List<CartaPersonaggio> CartePersonaggio;
    protected List<CartaImpresa> CarteImpresa;
    protected List<Familiare> Familiari;
    protected List<TesseraScomunica> CarteScomunica;
    protected transient GestoreEffettiGiocatore gestoreEffettiGiocatore;
    protected Risorsa Risorse;
    private int ordineTurno;
    private Boolean rapportoVaticanoEffettuato;
    //endregion

    //region Getters
    public short getIdGiocatore() {
        return IdGiocatore;
    }

    public int getOrdineTurno() { return ordineTurno; }

    public Risorsa getRisorse() {
        return Risorse;
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

    public Boolean getRapportoVaticanoEffettuato() {
        return rapportoVaticanoEffettuato;
    }

    public Familiare getFamiliareByColor(ColoreDado coloreDado)
    {
        return this.Familiari.stream().filter(f -> f.ColoreDado == coloreDado).findFirst().orElse(null);
    }
    //endregion

    //region Setters
    public void setOrdineTurno(int ordineTurno) {
        this.ordineTurno = ordineTurno;
    }

    public void setRapportoVaticanoEffettuato(Boolean rapportoVaticanoEffettuato) {
        this.rapportoVaticanoEffettuato = rapportoVaticanoEffettuato;
    }
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
        this.CarteScomunica = new ArrayList<>();
        gestoreEffettiGiocatore= new GestoreEffettiGiocatore(this);
        //region Inizializzazione Familiari
        //Inizializzazione Familiari
        this.Familiari = new ArrayList<>();
        ColoreDado coloreFamiliare = null;

        for(int i = 0; i <= 3; i++)
        {
            switch (i)
            {
                case 0:
                    coloreFamiliare = ColoreDado.BIANCO;
                    break;

                case 1:
                    coloreFamiliare = ColoreDado.ARANCIO;
                    break;

                case 2:
                    coloreFamiliare = ColoreDado.NERO;
                    break;

                case 3:
                    coloreFamiliare= ColoreDado.NEUTRO;
                    break;
            }
            this.Familiari.add(new Familiare(this, coloreFamiliare));
        }
        //endregion
    }

    /**
     *  Setta le proprietà al login del GiocatoreGraphic
     */
    public void SettaProprietaIniziali(short idGiocatore, String nome, ColoreGiocatore colore, int monete)
    {
        this.IdGiocatore = idGiocatore;
        this.ordineTurno = idGiocatore;
        this.Nome = nome;
        this.Colore = colore;
        this.rapportoVaticanoEffettuato = false;

        this.Risorse = new Risorsa(2, 2, 3, monete, 0, 0, 0 );
    }

    /**
     * Incrementa le risorse ottenute da un familiare su uno spazio azione
     */
    public void OttieniBonusSpazioAzione(Risorsa risorseSpazioAzione)
    {
        this.Risorse = Risorsa.add(this.Risorse, risorseSpazioAzione);
    }

    /**
     * Paga le risorse necessarie per prendere una carta
     */
    public void PagaRisorse(Risorsa costoRisorse)
    {
        this.Risorse = Risorsa.sub(this.Risorse, costoRisorse);
    }

    /**
     * Aggiorna il valore delle azioni dei singoli familiari
     * @param esitoDadi valore dei dadi tirati all'inizio del turno
     */
    public void SettaValoreFamiliare(int[] esitoDadi)
    {
        for (Familiare fam : this.Familiari) {
            switch(fam.ColoreDado)
            {
                case NERO:
                    fam.setValore(esitoDadi[0]);
                    break;

                case BIANCO:
                    fam.setValore(esitoDadi[1]);
                    break;

                case ARANCIO:
                    fam.setValore(esitoDadi[2]);
                    break;

                case NEUTRO: //Il familiare neutro non ha valore
                    fam.setValore(0);
                    break;
            }
        }
    }

    /**
     * Spende tutti i suoi punti fede
     * ottiene un certo numero di punti vittoria in base ai punti fede spesi
     */
    public UpdateGiocatoreDTO SostieniLaChiesa(int bonusPuntiVittoria)
    {
        this.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, this.Risorse.getPuntiVittoria() + bonusPuntiVittoria );
        this.Risorse.setRisorse(Risorsa.TipoRisorsa.PFEDE, 0);
        this.setRapportoVaticanoEffettuato(true);

        return new UpdateGiocatoreDTO(this.IdGiocatore, this.Risorse, null, null );
    }
}

