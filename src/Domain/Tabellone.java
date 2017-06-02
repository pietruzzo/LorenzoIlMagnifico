package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;
import server.Partita;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Portatile on 12/05/2017.
 */
public class Tabellone implements Serializable {

    //region Proprieta
    protected transient Partita Partita;
    protected List<Giocatore> Giocatori;
    protected List<Torre> Torri;
    protected List<SpazioAzioneProduzione> SpaziAzioneProduzione;
    protected List<SpazioAzioneRaccolto> SpaziAzioneRaccolto;
    protected List<SpazioAzioneMercato> SpaziAzioneMercato;
    protected SpazioAzioneConsiglio SpazioAzioneConsiglio;
    protected List<Carta> mazzoCarte;

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
        this.mazzoCarte = new ArrayList<>();

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

        //region Creazioe carte
        String nome;
        Effetto effetto = new Effetto();
        Risorsa risorsa = new Risorsa();

        for (int period = 1; period <= 3; period++)
        {
            for(int tipo = 0; tipo < 4; tipo++)
            {
                for (int num = 0; num < 8; num++)
                {
                    nome = String.format("%d_%d_%d", period, tipo, num);

                    //Ci sono 8 carte di ogni tipo per ogni periodo
                    switch (tipo)
                    {
                        case 0:
                            this.mazzoCarte.add(new CartaTerritorio(nome, period, risorsa, effetto, effetto));
                            break;

                        case 1:
                            this.mazzoCarte.add(new CartaEdificio(nome, period, risorsa, effetto, effetto));
                            break;

                        case 2:
                            this.mazzoCarte.add(new CartaPersonaggio(nome, period, risorsa, effetto, effetto));
                            break;

                        case 3:
                            this.mazzoCarte.add(new CartaImpresa(nome, period, risorsa, effetto, effetto));
                            break;
                    }
                }
            }
        }
        //endregion
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
        ColoreGiocatore colore = ColoreGiocatore.values()[this.Giocatori.size()];
        giocatore.SettaProprietaIniziali(idGiocatore, nome, colore, monete);
        this.Giocatori.add(giocatore);
    }

    /**
     * Aggiorna l'ordine dei giocatori in base ai familiari piazzati nello spazio azione del consiglio
     */
    public void AggiornaOrdineGiocatori()
    {
        //Recupera i gicatori piazzati nello spazio azione consiglio
        List<Giocatore> giocatoriConConsiglio = this.SpazioAzioneConsiglio.FamiliariPiazzati.stream().map(f -> f.Giocatore).distinct().collect(Collectors.toList());
        //Recupera i giocatori che non hanno piazzato familiari nello spazio azione del consiglio, ordinati con l'ordine di gioco
        List<Giocatore> giocatoriSenzaConsiglio = this.Giocatori.stream().filter(g -> !giocatoriConConsiglio.contains(g))
                                                                        .sorted(Comparator.comparingInt(Giocatore::getOrdineTurno))
                                                                        .collect(Collectors.toList());

        //Unisce le liste
        List<Giocatore> giocatoriOrdinati = new ArrayList<>();
        giocatoriOrdinati.addAll(giocatoriConConsiglio);
        giocatoriOrdinati.addAll(giocatoriSenzaConsiglio);

        //Setta il nuovo ordine di gioco
        int ordineGioco = 1;
        for (Giocatore giocatore : giocatoriOrdinati) {
            giocatore.setOrdineTurno(ordineGioco);
            ordineGioco++;
        }
    }

    //region Getters

    /**
     * Ritorna i giocatori
     */
    public List<Giocatore> getGiocatori() {
        return Giocatori;
    }

    /**
     * Ritorna gli spazi azione
     */
    public List<SpazioAzione> getSpaziAzione() {
        return SpaziAzione;
    }

    /**
     * Ritorna le carte da gioco disponibili
     */
    public List<Carta> getMazzoCarte() {
        return mazzoCarte;
    }

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

    /**
     * Ritorna true se ci sono dei giocatori che hanno ancora dei familiari da piazzare e se hanno la possibilità di piazzarli
     * (mi basta controllare se ho almeno un servitore per il neutro, perchè in quel caso è sempre possibile piazzarlo
     *  nello spazio azione del consiglio, se non è neutro non è necessario neanche un servitore)
     */
    public Boolean EsistonoFamiliariPiazzabili()
    {
        return this.Giocatori.stream().anyMatch(g ->
                g.Familiari.stream().anyMatch(
                        f -> f.SpazioAzioneAttuale == null
                        &&  ((!f.Neutro) || (g.Risorse.getServi() > 0))
                ));
    }
    //endregion

    /**
     * Pulisce il tabellone e carica le carte negli spazi azione torre
     * @return
     */
    public HashMap<Integer, String> IniziaTurno()
    {
        //Toglie i familiari dagli spazi azione
        this.SpaziAzione.forEach(s -> s.RimuoviFamiliari());
        this.Giocatori.forEach(g -> g.Familiari.forEach(f -> f.SpazioAzioneAttuale = null));

        //Associa le carte agli spazi azione
        this.Torri.forEach(t -> t.PescaCarte(this.Partita.getPeriodo(), this.mazzoCarte));

        //Rimuove dal mazzo tutte le carte pescate
        //e ritorna la lista con l'associazione spazioAzione-Carta
        HashMap<Integer, String> mappaCarte = new HashMap<>();
        this.Torri.forEach(t -> t.SpaziAzione.forEach(s -> this.mazzoCarte.remove(s.getCartaAssociata())));
        this.Torri.forEach(t -> t.SpaziAzione.forEach(s -> mappaCarte.put(s.getIdSpazioAzione(), s.getCartaAssociata().Nome)));

        return mappaCarte;
    }
}
