package Domain;

import Domain.DTO.UpdateGiocatoreDTO;
import Domain.Effetti.Effetto;
import Exceptions.DomainException;
import server.Partita;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
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
    protected List<TesseraScomunica> carteScomunica;

    private HashMap<Integer, Integer> bonusVittoriaPerPuntiFede;
    private HashMap<Integer, Integer> bonusVittoriaPerTerritori;
    private HashMap<Integer, Integer> bonusVittoriaPerPersonaggi;

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
        this.carteScomunica = new ArrayList<>();
        this.bonusVittoriaPerPuntiFede = new HashMap<>();
        this.bonusVittoriaPerTerritori = new HashMap<>();
        this.bonusVittoriaPerPersonaggi = new HashMap<>();

        //Inizializza le 4 torri
        for (TipoCarta tipo : TipoCarta.values()) {
            if(tipo != TipoCarta.Scomunica)
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
        List<Effetto> effetto = new ArrayList<Effetto>();
        Risorsa risorsa = new Risorsa();

        for (int period = 1; period <= 3; period++)
        {
            //Aggiunge le 3 carte scomunica
            this.carteScomunica.add(new TesseraScomunica(period, effetto));

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

        //region Set del bonus vittoria per punti fede
        //I punti fede sono la chiave dell'hashmap
        //Il bonus punti vittoria sono il valore corrispondente
        this.bonusVittoriaPerPuntiFede.put(0,0);
        this.bonusVittoriaPerPuntiFede.put(1,1);
        this.bonusVittoriaPerPuntiFede.put(2,2);
        this.bonusVittoriaPerPuntiFede.put(3,3);
        this.bonusVittoriaPerPuntiFede.put(4,4);
        this.bonusVittoriaPerPuntiFede.put(5,5);
        this.bonusVittoriaPerPuntiFede.put(6,7);
        this.bonusVittoriaPerPuntiFede.put(7,9);
        this.bonusVittoriaPerPuntiFede.put(8,11);
        this.bonusVittoriaPerPuntiFede.put(9,13);
        this.bonusVittoriaPerPuntiFede.put(10,15);
        this.bonusVittoriaPerPuntiFede.put(11,17);
        this.bonusVittoriaPerPuntiFede.put(12,19);
        this.bonusVittoriaPerPuntiFede.put(13,22);
        this.bonusVittoriaPerPuntiFede.put(14,25);
        this.bonusVittoriaPerPuntiFede.put(15,30);
        //endregion

        //region Set del bonus vittoria per carte territorio
        //Il numero di carte territorio del giocatore è la chiave dell'hashmap
        //Il bonus punti vittoria sono il valore corrispondente
        this.bonusVittoriaPerTerritori.put(0,0);
        this.bonusVittoriaPerTerritori.put(1,0);
        this.bonusVittoriaPerTerritori.put(2,0);
        this.bonusVittoriaPerTerritori.put(3,1);
        this.bonusVittoriaPerTerritori.put(4,4);
        this.bonusVittoriaPerTerritori.put(5,10);
        this.bonusVittoriaPerTerritori.put(6,20);
        //endregion

        //region Set del bonus vittoria per carte personaggio
        //Il numero di carte personaggio del giocatore è la chiave dell'hashmap
        this.bonusVittoriaPerTerritori.put(0,0);
        this.bonusVittoriaPerTerritori.put(1,1);
        this.bonusVittoriaPerTerritori.put(2,3);
        this.bonusVittoriaPerTerritori.put(3,6);
        this.bonusVittoriaPerTerritori.put(4,10);
        this.bonusVittoriaPerTerritori.put(5,15);
        this.bonusVittoriaPerTerritori.put(6,21);
        //endregion
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
     * Ritorna il GiocatoreGraphic dato il suo id
     * @param idGiocatore
     */
    private Giocatore getGiocatoreById(short idGiocatore)
    {
        return this.Giocatori.stream().filter(x -> x.getIdGiocatore() == idGiocatore).findFirst().orElse(null);
    }

    /**
     * Ritorna il nome del GiocatoreGraphic dato il suo id
     */
    public String getNomeGiocatoreById (short idGiocatore)
    {
        return this.getGiocatoreById(idGiocatore).Nome;
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

    /**
     * Ritorna il bonus vittoria associato ai punti fede specificati
     */
    public int getBonusVittoriaByPuntiFede(int puntiFede)
    {
        return this.bonusVittoriaPerPuntiFede.get(puntiFede);
    }

    /**
     * Ritorna il bonus vittoria associato al numero di carte territorio
     */
    public int getBonusVittoriaByTerritori(int numCarteTerritorio)
    {
        return this.bonusVittoriaPerTerritori.get(numCarteTerritorio);
    }


    /**
     * Ritorna il bonus vittoria associato al numero di carte personaggio
     */
    public int getBonusVittoriaByPersonaggi(int numCartePersonaggio)
    {
        return this.bonusVittoriaPerPersonaggi.get(numCartePersonaggio);
    }

    //endregion

    /**
     * Aggiunge un GiocatoreGraphic alla partita (ci possono essere al massimo 4 giocatori)
     */
    public void AggiungiGiocatore(short idGiocatore, String nome, Giocatore giocatore) throws DomainException {
        int numeroGiocatori = this.Giocatori.size();
        if(numeroGiocatori >= 4)
            throw new DomainException("E' stato raggiunto il numero limite di giocatori");
        if(this.Giocatori.stream().anyMatch(x -> x.Nome.equals(nome)))
            throw new DomainException("Esiste già un GiocatoreGraphic con lo stesso username.");

        //il primo GiocatoreGraphic riceve 5 monete, il secondo 6, il terzo 7 e il quarto 8.
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

    //region Piazzamento familiari
    /**
     * Consente di piazzare un familiare in uno spazio azione
     */
    public UpdateGiocatoreDTO PiazzaFamiliare(short idGiocatore, ColoreDado coloreDado, int idSpazioAzione, int servitoriAggiunti) throws DomainException {
        Giocatore giocatore = this.getGiocatoreById(idGiocatore);
        Familiare familiare = giocatore.getFamiliareByColor(coloreDado);
        SpazioAzione spazioAzione = this.getSpazioAzioneById(idSpazioAzione);
        spazioAzione.PiazzaFamiliare(familiare, servitoriAggiunti);

        return new UpdateGiocatoreDTO(idGiocatore, giocatore.getRisorse(), coloreDado, idSpazioAzione);
    }

    /**
     * Valida il piazzamento di un familiare nella zona produzione
     */
    protected void ValidaPiazzamentoFamiliareProduzione(Familiare familiare) throws DomainException {
        //Non ci possono essere due familiari dello stesso colore nella stessa zona.
        //Un GiocatoreGraphic può piazzare un familiare colorato e il familiare neutro
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
        //Un GiocatoreGraphic può piazzare un familiare colorato e il familiare neutro
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
     * Scomunica un giocatore
     */
    public void ScomunicaGiocatore(Giocatore giocatore)
    {
        //Recupera il periodo di gioco e la relativa carta scomunica
        int periodo = this.Partita.getPeriodo();
        TesseraScomunica tesseraScomunica = this.carteScomunica.get(periodo-1);
        tesseraScomunica.AssegnaGiocatore(giocatore);
        giocatore.setRapportoVaticanoEffettuato(true);
    }

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

    /**
     * Calcola i punteggi finali dei giocatori per stabilire la vittoria
     */
    public void CalcolaPunteggiFinali()
    {
        //Individua il primo e il secondo punteggio militare
        int firstPMilitari = this.Giocatori.stream().mapToInt(g -> g.getRisorse().getPuntiMilitari()).max().orElse(0);
        int secondPMilitari = this.Giocatori.stream().filter(x -> x.getRisorse().getPuntiMilitari() < firstPMilitari)
                                    .mapToInt(g -> g.getRisorse().getPuntiMilitari()).max().orElse(0);

        //Individua gli id dei giocatori che sono arrivati primi e secondi nella classifica militare
        int[] giocatoriMaxPunti = this.Giocatori.stream().filter(x -> x.getRisorse().getPuntiMilitari() == firstPMilitari).mapToInt(g -> g.getIdGiocatore()).toArray();
        int[] giocatoriSecondPunti = this.Giocatori.stream().filter(x -> x.getRisorse().getPuntiMilitari() == secondPMilitari).mapToInt(g -> g.getIdGiocatore()).toArray();

        //Calcola i punteggi per ogni giocatore
        for (Giocatore giocatore : this.Giocatori) {
            int pVittoriaToAdd = 0;

            //1/4/10/20 Punti Vittoria per 3/4/5/6 carte territorio sulla propria plancia giocatore.
            pVittoriaToAdd += this.getBonusVittoriaByTerritori(giocatore.CarteTerritorio.size());
            //1/3/6/10/15/21 Punti Vittoria per 1/2/3/4/5/6 carte personaggio sulla propria plancia giocatore.
            pVittoriaToAdd += this.getBonusVittoriaByPersonaggi(giocatore.CartePersonaggio.size());
            //Guadagna il numero di Punti Vittoria indicato sullo spazio del tracciato dei Punti Fede sul quale si trova
            pVittoriaToAdd += this.getBonusVittoriaByPuntiFede(giocatore.Risorse.getPuntiFede());
            //Considera la classifica dei punti militari
            pVittoriaToAdd += this.getBonusVittoriaByPuntiMilitari(giocatore.getIdGiocatore(), giocatoriMaxPunti, giocatoriSecondPunti);

            //Aggiorna le risorse del giocatore considerando gli effetti delle carte (tutte le carte impresa e eventuali tessere scomunica)
            giocatore.updatePuntiVittoriaByEffettiCarte();

            //Calcola il punteggio in base alle risorse ( legno, pietra, servitori, monete) del giocatore
            pVittoriaToAdd += giocatore.getPuntiVittoriaByRisorse();

            //Aggiunge tutti i punti vittoria al giocatore
            giocatore.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, giocatore.getRisorse().getPuntiVittoria() + pVittoriaToAdd);
        }
    }

    /**
     * Il giocatore più in alto sul tracciato dei Punti Militari guadagna 5 Punti Vittoria, il secondo ne guadagna 2.
     * (In caso di pareggio al primo posto, entrambi i giocatori guadagnano 5 Punti Vittoria e nessuno ne guadagna 2.
     * In caso di pareggio al secondo posto, entrambi i giocatori guadagnano 2 Punti Vittoria.)
     * @param idGiocatore id del giocatore al quale andranno assegnati i punti
     * @param primiGiocatori array degli id dei giocatori che arrivati primi nella classifica militare
     * @param secondiGiocatori array degli id dei giocatori che sono arrivati secondi nella classifica militare
     * @return punti vittoria in base alla posizione
     */
    private int getBonusVittoriaByPuntiMilitari(int idGiocatore, int[] primiGiocatori, int[] secondiGiocatori)
    {
        //Se il giocatore ha il punteggio massimo prende 5 punti
        if(Arrays.stream(primiGiocatori).anyMatch(x -> x == idGiocatore))
            return 5;
        //Se il giocatore non ha il punteggio massimo, ma è secondo
        else if(Arrays.stream(secondiGiocatori).anyMatch(x -> x == idGiocatore))
        {
            //In caso di pareggio al primo posto gli altri non guadagnano punti
            if(primiGiocatori.length > 1)
                return 0;
            else
                return 2;
        }

        //Se non si è in nessuna lista non si ricevono punti
        return 0;
    }

}
