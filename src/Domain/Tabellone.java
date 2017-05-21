package Domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Tabellone {

    //region Proprieta
    protected int Turno;
    protected int Periodo;

    protected List<Giocatore> Giocatori;
    protected List<Torre> Torri;
    protected List<SpazioAzioneProduzione> SpaziAzioneProduzione;
    protected List<SpazioAzioneRaccolto> SpaziAzioneRaccolto;
    protected List<SpazioAzioneMercato> SpaziAzioneMercato;
    protected SpazioAzioneConsiglio SpazioAzioneConsiglio;
    //endregion

    /**
     * Costruttore Tabellone
     */
    public Tabellone()
    {
        this.Turno = 1;
        this.Periodo = 1;
        this.Giocatori = new ArrayList<>();
        this.Torri = new ArrayList<>();
        this.SpaziAzioneProduzione = new ArrayList<>();
        this.SpaziAzioneRaccolto = new ArrayList<>();
        this.SpaziAzioneMercato = new ArrayList<>();

        //Inizializza le 4 torri
        for (TipoCarta tipo : TipoCarta.values()) {
            this.Torri.add(new Torre(tipo));
        }

        //region Spazi Produzione
        this.SpaziAzioneProduzione.add(new SpazioAzioneProduzione(1, 1,0));
        this.SpaziAzioneProduzione.add(new SpazioAzioneProduzione(1, 16,-3));
        //endregion

        //region Spazi Raccolto
        this.SpaziAzioneRaccolto.add(new SpazioAzioneRaccolto(1, 1,0));
        this.SpaziAzioneRaccolto.add(new SpazioAzioneRaccolto(1, 16,-3));
        //endregion

        //region Spazi Mercato

        this.SpaziAzioneMercato.add(new SpazioAzioneMercato(1, new Risorsa(0, 0, 0, 5, 0, 0, 0)));
        this.SpaziAzioneMercato.add(new SpazioAzioneMercato(1, new Risorsa(0, 0, 5, 0, 0, 0, 0)));
        this.SpaziAzioneMercato.add(new SpazioAzioneMercato(1, new Risorsa(0, 0, 0, 2, 0, 3, 0)));
        this.SpaziAzioneMercato.add(new SpazioAzioneMercato(1, new Risorsa(0, 0, 0, 5, 0, 0, 0)));
        //endregion

        this.SpazioAzioneConsiglio = new SpazioAzioneConsiglio(1, new Risorsa(0, 0, 0, 1, 0, 0, 0));
    }

    /**
     * Aggiunge un giocatore alla partita (ci possono essere al massimo 4 giocatori)
     */
    public void AggiungiGiocatore(String nome, Color colore, Giocatore giocatore) throws Exception {
        int numeroGiocatori = this.Giocatori.size();
        if(numeroGiocatori >= 4)
            throw new Exception("E' stato raggiunto il numero limite di giocatori");

        //il primo giocatore riceve 5 monete, il secondo 6, il terzo 7 e il quarto 8.
        int monete = 5 + numeroGiocatori;

        giocatore.SettaProprietaIniziali(nome, colore, monete);
        this.Giocatori.add(giocatore);
    }

    /**
     * Ritorna la torre dato il tipo
     */
    private Torre getTorreByTipo(TipoCarta tipo) {
        return this.Torri.stream().filter(x -> x.Tipo == tipo).findFirst().orElse(null);
    }

    //region Piazzamento familiari
    /**
     * Consente di piazzare un familiare in una torre, nella torre indicata dal tipo
     */
    public void PiazzaFamiliareTorre(Familiare familiare, TipoCarta tipo, int valore) throws Exception {
        Torre torre = this.getTorreByTipo(tipo);
        torre.PiazzaFamiliare(familiare, valore);
    }

    /**
     * Piazza il familiare nella zona produzione (1 lo spazio piccolo, 2 quello grande)
     */
    public void PiazzaFamiliareProduzione(Familiare familiare, int posizione) throws Exception {
        //Non ci possono essere due familiari dello stesso colore nella stessa zona.
        //Un giocatore può piazzare un familiare colorato e il familiare neutro
        if(this.SpaziAzioneProduzione.stream().
            anyMatch(x -> x.FamiliariPiazzati.stream().anyMatch(y -> y.Giocatore == familiare.Giocatore
                                                                &&  y.Neutro == familiare.Neutro)))
            throw new Exception("E' già presente un familiare dello stesso colore nella zona Produzione.");

        SpazioAzioneProduzione spazioAzioneProduzione = this.SpaziAzioneProduzione.get(posizione);
        spazioAzioneProduzione.PiazzaFamiliare(familiare);
    }

    /**
     * Piazza il familiare nella zona raccolto (1 lo spazio piccolo, 2 quello grande)
     */
    public void PiazzaFamiliareRaccolto(Familiare familiare, int posizione) throws Exception {
        //Non ci possono essere due familiari dello stesso colore nella stessa zona.
        //Un giocatore può piazzare un familiare colorato e il familiare neutro
        if(this.SpaziAzioneRaccolto.stream().
                anyMatch(x -> x.FamiliariPiazzati.stream().anyMatch(y -> y.Giocatore == familiare.Giocatore
                        &&  y.Neutro == familiare.Neutro)))
            throw new Exception("E' già presente un familiare dello stesso colore nella zona Produzione.");

        SpazioAzioneRaccolto spazioAzioneRaccolto = this.SpaziAzioneRaccolto.get(posizione);
        spazioAzioneRaccolto.PiazzaFamiliare(familiare);
    }

    /**
     * Piazza il familiare nella zona mercato (1, 2, 3, 4 da sx a dx)
     */
    public void PiazzaFamiliareMercato(Familiare familiare, int posizione) throws Exception {
        SpazioAzioneMercato spazioAzioneMercato = this.SpaziAzioneMercato.get(posizione);
        spazioAzioneMercato.PiazzaFamiliare(familiare);
    }

    /**
     * Piazza il familiare nella zona consiglio
     */
    public void PiazzaFamiliareConsiglio(Familiare familiare) throws Exception {
        this.SpazioAzioneConsiglio.PiazzaFamiliare(familiare);
    }
    //endregion
}
