package Domain;

import Exceptions.DomainException;
import server.Partita;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Tabellone {

    //region Proprieta
    protected Partita Partita;
    protected List<Giocatore> Giocatori;
    protected List<Torre> Torri;
    protected List<SpazioAzioneProduzione> SpaziAzioneProduzione;
    protected List<SpazioAzioneRaccolto> SpaziAzioneRaccolto;
    protected List<SpazioAzioneMercato> SpaziAzioneMercato;
    protected SpazioAzioneConsiglio SpazioAzioneConsiglio;

    protected List<SpazioAzione> SpaziAzione;
    protected static int maxIdSpazioAzione = 0;
    //endregion

    /**
     * Costruttore Tabellone
     */
    public Tabellone(Partita partita)
    {
        this.Partita = partita;
        this.Giocatori = new ArrayList<>();
        this.Torri = new ArrayList<>();
        this.SpaziAzioneProduzione = new ArrayList<>();
        this.SpaziAzioneRaccolto = new ArrayList<>();
        this.SpaziAzioneMercato = new ArrayList<>();
        this.SpaziAzione = new ArrayList<>();

        //Inizializza le 4 torri
        for (TipoCarta tipo : TipoCarta.values()) {
            this.Torri.add(new Torre(tipo));
        }

        //region Spazi Produzione
        this.SpaziAzioneProduzione.add(new SpazioAzioneProduzione(1, 1,0, this));
        this.SpaziAzioneProduzione.add(new SpazioAzioneProduzione(1, 16,-3, this));
        //endregion

        //region Spazi Raccolto
        this.SpaziAzioneRaccolto.add(new SpazioAzioneRaccolto(1, 1,0, this));
        this.SpaziAzioneRaccolto.add(new SpazioAzioneRaccolto(1, 16,-3, this));
        //endregion

        //region Spazi Mercato

        this.SpaziAzioneMercato.add(new SpazioAzioneMercato(1, new Risorsa(0, 0, 0, 5, 0, 0, 0)));
        this.SpaziAzioneMercato.add(new SpazioAzioneMercato(1, new Risorsa(0, 0, 5, 0, 0, 0, 0)));
        this.SpaziAzioneMercato.add(new SpazioAzioneMercato(1, new Risorsa(0, 0, 0, 2, 0, 3, 0)));
        this.SpaziAzioneMercato.add(new SpazioAzioneMercato(1, new Risorsa(0, 0, 0, 5, 0, 0, 0)));
        //endregion

        this.SpazioAzioneConsiglio = new SpazioAzioneConsiglio(1, new Risorsa(0, 0, 0, 1, 0, 0, 0));

        //Aggiungo tutti gli spazi azione nella collection generica
        this.Torri.stream().forEach(x -> this.SpaziAzione.addAll(x.SpaziAzione));
        this.SpaziAzione.addAll(this.SpaziAzioneProduzione);
        this.SpaziAzione.addAll(this.SpaziAzioneRaccolto);
        this.SpaziAzione.addAll(this.SpaziAzioneMercato);
        this.SpaziAzione.add(this.SpazioAzioneConsiglio);
    }

    /**
     * Aggiunge un giocatore alla partita (ci possono essere al massimo 4 giocatori)
     */
    public void AggiungiGiocatore(short idGiocatore, String nome, Giocatore giocatore) throws DomainException {
        int numeroGiocatori = this.Giocatori.size();
        if(numeroGiocatori >= 4)
            throw new DomainException("E' stato raggiunto il numero limite di giocatori");
        if(this.Giocatori.stream().anyMatch(x -> x.Nome.equals(nome)))
            throw new DomainException("Esiste già un giocatore con lo stesso username.");

        //il primo giocatore riceve 5 monete, il secondo 6, il terzo 7 e il quarto 8.
        int monete = 5 + numeroGiocatori;

        giocatore.SettaProprietaIniziali(idGiocatore, nome, Color.BLUE, monete);
        this.Giocatori.add(giocatore);
    }

    //region Getters
    /**
     * Ritorna il giocatore dato il suo id
     * @param idGiocatore
     */
    private Giocatore GetGiocatoreById(short idGiocatore)
    {
        return this.Giocatori.stream().filter(x -> x.getIdGiocatore() == idGiocatore).findFirst().orElse(null);
    }

    /**
     * Ritorna il nome del giocatore dato il suo id
     */
    public String GetNomeGiocatoreById (short idGiocatore)
    {
        return this.GetGiocatoreById(idGiocatore).Nome;
    }

    /**
     * Ritorna la torre dato il tipo
     */
    private Torre getTorreByTipo(TipoCarta tipo) {
        return this.Torri.stream().filter(x -> x.Tipo == tipo).findFirst().orElse(null);
    }

    /**
     * Ritorna lo spazio azione dato l'id
     */
    private SpazioAzione getSpazioAzioneById(int idSpazioAzione) {
        return this.SpaziAzione.stream().filter(x -> x.getIdSpazioAzione() == idSpazioAzione).findFirst().orElse(null);
    }

    //endregion

    //region Piazzamento familiari
    /**
     * Consente di piazzare un familiare in uno spazio azione
     */
    public void PiazzaFamiliare(int idSpazioAzione, Familiare familiare) throws DomainException {
        SpazioAzione spazioAzione = this.getSpazioAzioneById(idSpazioAzione);
        spazioAzione.PiazzaFamiliare(familiare);
    }

    /**
     * Valida il piazzamento di un familiare nella zona produzione
     */
    protected void ValidaPiazzamentoFamiliareProduzione(Familiare familiare) throws DomainException {
        //Non ci possono essere due familiari dello stesso colore nella stessa zona.
        //Un giocatore può piazzare un familiare colorato e il familiare neutro
        if(this.SpaziAzioneProduzione.stream().
            anyMatch(x -> x.FamiliariPiazzati.stream().anyMatch(y -> y.Giocatore == familiare.Giocatore
                                                                &&  y.Neutro == familiare.Neutro)))
            throw new DomainException("E' già presente un familiare dello stesso colore nella zona Produzione.");
    }

    /**
     * Valida il piazzamento di un familiare nella zona raccolto
     */
    protected void ValidaPiazzamentoFamiliareRaccolto(Familiare familiare) throws DomainException {
        //Non ci possono essere due familiari dello stesso colore nella stessa zona.
        //Un giocatore può piazzare un familiare colorato e il familiare neutro
        if(this.SpaziAzioneRaccolto.stream().
                anyMatch(x -> x.FamiliariPiazzati.stream().anyMatch(y -> y.Giocatore == familiare.Giocatore
                        &&  y.Neutro == familiare.Neutro)))
            throw new DomainException("E' già presente un familiare dello stesso colore nella zona Produzione.");
    }

    //endregion
}
