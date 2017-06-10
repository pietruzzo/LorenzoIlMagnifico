package server;

import Domain.DTO.AzioneBonusDTO;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Dado;
import Domain.Giocatore;
import Domain.Risorsa;
import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;
import Exceptions.DomainException;
import Exceptions.NetworkException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Portatile on 23/05/2017.
 */
public class Partita  implements Serializable {

    //region Proprieta
    private static final short MIN_GIOCATORI = 2;
    private static final short MAX_GIOCATORI = 2;
    private static final short NUM_DADI = 3;
    private static final Object MUTEX_PARTITA = new Object();


    private Server server;
    private Tabellone tabellone;
    private ArrayList<GiocatoreRemoto> giocatoriPartita;
    private boolean iniziata;
    private int turno;
    private int periodo;
    private int[] esitoDadi;
    private int ordineMossaCorrente;
    //endregion

    /**
     * Costruttore partita
     */
    public Partita(Server server)
    {
        this.server = server;
        this.iniziata = false;
        this.turno = 0;
        this.periodo = 0;
        this.ordineMossaCorrente = 0;
        this.tabellone = new Tabellone();
        this.giocatoriPartita = new ArrayList<>();
        this.esitoDadi = new int[NUM_DADI];
    }


    //region Getters
    /**
     * Ritorna il periodo di gioco
     * @return
     */
    public int getPeriodo() {
        return periodo;
    }

    /**
     * Ritorna true se la partita è iniziata
     */
    public boolean isIniziata() {
        return iniziata;
    }

    protected int getTurno() {
        return turno;
    }

    protected int getOrdineMossaCorrente() {
        return ordineMossaCorrente;
    }

    protected int[] getEsitoDadi() {
        return esitoDadi;
    }

    protected ArrayList<GiocatoreRemoto> getGiocatoriPartita() {
        return giocatoriPartita;
    }

    public Tabellone getTabellone() {
        return tabellone;
    }

    //endregion

    /**
     * Aggiunge un giocatore alla partita
     */
    public void AggiungiGiocatore(short idGiocatore, String nome, GiocatoreRemoto giocatore) throws DomainException {
        synchronized (MUTEX_PARTITA)
        {
            this.tabellone.AggiungiGiocatore(idGiocatore, nome, giocatore);
            this.giocatoriPartita.add(giocatore);
            giocatore.setPartita(this);
        }
    }

    /**
     * Se ci sono 4 giocatori e la partita non è ancora iniziata, viene iniziata in automatico
     */
    public void VerificaInizioAutomatico() throws DomainException {
        if(this.giocatoriPartita.size() == this.MAX_GIOCATORI)
            this.IniziaPartita();
    }

    /**
     * Inizia la partita
     */
    public void IniziaPartita() throws DomainException {
        if(this.giocatoriPartita.size() >= this.MIN_GIOCATORI) {
            synchronized (MUTEX_PARTITA) {
                if (this.iniziata == false) {
                    this.iniziata = true;
                    System.out.println("Partita iniziata");

                    //Comunica l'inizio della partita agli altri giocatori
                    for (GiocatoreRemoto giocatore : this.giocatoriPartita) {
                        try{ giocatore.PartitaIniziata(this.tabellone); }
                        catch (NetworkException e) {
                            System.out.println("Giocatore non più connesso");
                        }
                    }

                    //Comunica l'inizio del nuovo turno
                    this.InizioNuovoTurno();
                }
            }
        }
        else
            throw new DomainException("E' necessario che ci siano almeno due utenti connessi alla partita per cominciare.");
    }

    /**
     * Comunica ai giocatori l'inizio di un nuovo turno
     * Deve passare quindi l'esito dei dadi e le carte da mettere sulle torri
     */
    public void InizioNuovoTurno()
    {
        //Incrementa il turno
        this.turno++;

        //Azzera l'ordine delle mosse della partita in corso
        this.ordineMossaCorrente = 0;

        //Aggiorna l'ordine dei giocatori in base allo spazio azione del consiglio
        this.tabellone.AggiornaOrdineGiocatori();

        //Se necessario, incrementa anche il periodo
        if(this.turno % 2 == 1) this.periodo++;

        //Inizializza i dadi per il turno di gioco
        for (int i = 0; i < NUM_DADI; i++)
        {
            this.esitoDadi[i] = Dado.lancia();
        }

        //Inizializza il valore dei familiari in base all'esito dei dadi
        this.giocatoriPartita.forEach(x -> x.SettaValoreFamiliare(esitoDadi));

        //Azzera i dati relativi ai rapporti al vaticano effettuati
        this.giocatoriPartita.forEach(x -> x.setRapportoVaticanoEffettuato(false));

        //"Pulisce" il tabellone e pesca le 16 carte da mettere sulle torri
        HashMap<Integer, String> mappaCarte = this.tabellone.IniziaTurno(this.periodo);
        int[] ordineGiocatori = this.giocatoriPartita.stream().mapToInt(Giocatore::getIdGiocatore).toArray();

        //Comunica ai giocatori l'inzio del nuovo turno
        for (GiocatoreRemoto giocatore : this.giocatoriPartita) {
            try{ giocatore.IniziaTurno(ordineGiocatori, this.esitoDadi, mappaCarte); }
            catch (NetworkException e) {
                System.out.println("Giocatore non più connesso");
            }
        }

        //Parte il primo giro di mosse del turno appena iniziato
        this.IniziaNuovaMossa();
    }

    /**
     * Comunica il giocatore che deve effettuare la prossima mossa
     */
    public void IniziaNuovaMossa()
    {
        GiocatoreRemoto nextGiocatore = this.GetNextGiocatore();

        //Se è stato individuato il prossimo giocatore che deve effettuare la mossa
        //allora viene comunicato l'inizio della sua mossa ai client
        if(nextGiocatore != null)
        {
            this.ordineMossaCorrente = nextGiocatore.getOrdineTurno();
            this.ComunicaInizioMossa(nextGiocatore.getIdGiocatore());
        }
        else
        {
            //Se non è stato individuato il prossimo giocatore significa che tutti hanno fatto il loro giro di mosse
            //Se non ci sono giocatori che possono piazzare un familiare si inizia un nuovo turno
            //Se invece i giocatori hanno ancora mosse a disposizione si inizia un nuovo giro di mosse
            if(this.EsistonoFamiliariPiazzabili())
            {
                //L'ordine dei giocatori rimane lo stesso e si ricomincia il giro
                this.ordineMossaCorrente = 0;
                this.IniziaNuovaMossa();
            }
            else
            {
                //Se non ci sono più familiari piazzabili si inizia un nuovo turno
                //Se siamo alla fine del periodo parte il rapporto al vaticano
                if(this.turno % 2 == 0)
                {
                    this.EffettuaRapportoVaticano();
                }
                else {
                    //Alla fine dei turni dispari si comincia semplicemente un nuovo turno
                    //Si arriverà qui solo al termine del primo, terzo e quinto turno
                    this.InizioNuovoTurno();
                }
            }
        }
    }

    /**
     * Ritorna l'id del giocatore che deve fare la prossima mossa
     */
    private GiocatoreRemoto GetNextGiocatore()
    {
        return this.giocatoriPartita.stream().filter(g -> g.getOrdineTurno() > this.ordineMossaCorrente)
                                                .sorted(Comparator.comparingInt(Giocatore::getOrdineTurno))
                                                .findFirst().orElse(null);
    }

    /**
     * Effettua la chiamata ai client per comunicare l'inizio della mossa di qualcuno
     */
    private void ComunicaInizioMossa(int idGiocatore)
    {
        for (GiocatoreRemoto giocatore : this.giocatoriPartita) {
            try{ giocatore.IniziaMossa(idGiocatore); }
            catch (NetworkException e) {
                System.out.println("Giocatore non più connesso");
            }
        }
    }

    /**
     * Ritorna true se c'è almeno un giocatore che ha ancora dei familiari da piazzare
     */
    private Boolean EsistonoFamiliariPiazzabili()
    {
        return this.tabellone.EsistonoFamiliariPiazzabili();
    }


    /**
     * Notifica a tutti i client l'aggiornamento di un giocatore
     * @param update nuove caratteristiche del giocatore
     */
    private void ComunicaAggiornaGiocatore(UpdateGiocatoreDTO update)
    {
        for (GiocatoreRemoto giocatore : this.giocatoriPartita) {
            try{ giocatore.ComunicaAggiornaGiocatore(update); }
            catch (NetworkException e) {
                System.out.println("Giocatore non più connesso");
            }
        }
    }


    /**
     * Tenta di effettuare il piazzamento indicato nei parametri
     * @param pfDTO parametri indicanti il piazzamento da effettuare
     */
    public void PiazzaFamiliare(PiazzaFamiliareDTO pfDTO) throws DomainException {
        UpdateGiocatoreDTO update = this.tabellone.PiazzaFamiliare(pfDTO.getIdGiocatore(), pfDTO.getColoreDado(), pfDTO.getIdSpazioAzione(), pfDTO.getServitoriAggiunti());
        this.ComunicaAggiornaGiocatore(update);

        //Verifica che ci siano le condizioni per iniziare una nuova mossa
        if(this.PuoCominciareUnaNuovaMossa())
            this.IniziaNuovaMossa();
    }

    /**
     * Tenta di effettuare l'azione bonus specificata dal client
     * @param azioneBonusDTO parametri dell'azione
     * @throws DomainException eccezione di validazione
     */
    public void AzioneBonusEffettuata(AzioneBonusDTO azioneBonusDTO) throws DomainException {
        UpdateGiocatoreDTO update = tabellone.AzioneBonusEffettuata(azioneBonusDTO.getIdGiocatore(), azioneBonusDTO.getIdSpazioAzione(),
                                                                    azioneBonusDTO.getValoreAzione(), azioneBonusDTO.getBonusRisorse(),
                                                                    azioneBonusDTO.getServitoriAggiunti());
        this.ComunicaAggiornaGiocatore(update);

        //Verifica che ci siano le condizioni per iniziare una nuova mossa
        if(this.PuoCominciareUnaNuovaMossa())
            this.IniziaNuovaMossa();
    }

    /**
     * Aggiorna le risorse del giocatore in seguito alla scelta di un privilegio del consiglio
     * @param idGiocatore id del giocatore che ha ottenuto il privilegio
     * @param risorsa risorse corrispondenti al privilegio scelto
     */
    public void RiscuotiPrivilegiDelConsiglio(short idGiocatore, Risorsa risorsa)
    {
        UpdateGiocatoreDTO update = this.tabellone.RiscuotiPrivilegiDelConsiglio(idGiocatore, risorsa);
        this.ComunicaAggiornaGiocatore(update);

        //Verifica che ci siano le condizioni per iniziare una nuova mossa
        if(this.PuoCominciareUnaNuovaMossa())
            this.IniziaNuovaMossa();
    }

    /**
     * Torna true se è possibile iniziare una nuova mossa
     */
    @NotNull
    private Boolean PuoCominciareUnaNuovaMossa()
    {
        //Se qualche giocatore deve ancora effettuare la sua mossa bonus non comincia una nuova mossa
        //Se qualche giocatore deve ancora scegliere i privilegi del consiglio non comincia una nuova mossa
        if(giocatoriPartita.stream().anyMatch(g -> g.getAzioneBonusDaEffettuare() || g.getPrivilegiDaScegliere() > 0))
            return false;

        return true;
    }

    //region Rapporto Vaticano
    /**
     * Effettua le operazioni per il rapporto al vaticano
     */
    protected void EffettuaRapportoVaticano()
    {
        //Individua i giocatori che non hanno abbastanza punti fede per il periodo corrente
        List<GiocatoreRemoto> giocatoriDaScomunicare = this.giocatoriPartita.stream()
                                                        .filter(g -> g.getRisorse().getPuntiFede() < (this.periodo + 2))
                                                        .collect(Collectors.toList());

        //Agli altri giocatori verrà data la possibilità di scegliere
        List<GiocatoreRemoto> giocatoriCheScelgono = this.giocatoriPartita.stream().filter(g -> !giocatoriDaScomunicare.contains(g)).collect(Collectors.toList());

        //Scomunica i giocatori appena individuati
        giocatoriDaScomunicare.forEach(g -> this.tabellone.ScomunicaGiocatore(g, this.periodo));

        //Comunica la scomunica ai client
        int[] idGiocatoriScomunicati = giocatoriDaScomunicare.stream().mapToInt(Giocatore::getIdGiocatore).toArray();
        this.ComunicaScomunica(idGiocatoriScomunicati);

        //Se qualche giocatore ha la possibilità di scegliere gli viene data la possibilità
        if(giocatoriCheScelgono.size() > 0)
        {
            //Comunica la possibilità di scelta ai soli client abilitati
            for (GiocatoreRemoto giocatore : giocatoriCheScelgono) {
                try {
                    giocatore.SceltaSostegnoChiesa();
                } catch (NetworkException e) {
                    System.out.println("Giocatore non più connesso");
                }
            }
        }
        else //Altrimenti si prosegue normalmente
        {
            if (this.periodo < 3)
                this.InizioNuovoTurno();
            else
                this.FinePartita();
        }
    }

    /**
     * Comunica ai client la scomunica di giocatori
     * @param idGiocatoriScomunicati array degli id dei giocatori scomunicati
     */
    public void ComunicaScomunica(int[] idGiocatoriScomunicati)
    {
        for (GiocatoreRemoto giocatore : this.giocatoriPartita) {
            try{ giocatore.ComunicaScomunica(idGiocatoriScomunicati, this.periodo); }
            catch (NetworkException e) {
                System.out.println("Giocatore non più connesso");
            }
        }
    }

    /**
     *  Gestisce la risposta del client alla domanda sul sostegno della chiesa
     *  @param risposta true se sostiene, con false il giocatore viene scomunicato
     */
    public void RispostaSostegnoChiesa(GiocatoreRemoto giocatore, Boolean risposta){
        if(risposta == true)
        {
            //Se il giocatore ha scelto di sostenere la chiesa:
            // * deve spendere tutti i suoi punti fede
            // * ottiene un certo numero di punti vittoria in base ai punti fede spesi
            int bonusVittoria = this.tabellone.getBonusVittoriaByPuntiFede(giocatore.getRisorse().getPuntiFede());
            UpdateGiocatoreDTO update = giocatore.SostieniLaChiesa(bonusVittoria);

            this.ComunicaAggiornaGiocatore(update);
        }
        else
        {
            //Se il giocatore non vuole sostenere la chiesa viene scomunicato
            this.tabellone.ScomunicaGiocatore(giocatore, this.periodo);
            this.ComunicaScomunica(new int[] {giocatore.getIdGiocatore()} );
        }

        //Se tutti i giocatori hanno effettuato il rapporto al vaticano si può andare avanti
        if(this.giocatoriPartita.stream().allMatch(g -> g.getRapportoVaticanoEffettuato()))
        {
            if(periodo < 3)
                this.InizioNuovoTurno();
            else
                this.FinePartita();
        }

    }

    //endregion

    /**
     * Calcola il punteggio e lo comunica ai client
     * @return mappa dei risultati, utile per i test
     */
    public void FinePartita()
    {
        LinkedHashMap<Short, Integer> mappaRisultati = this.tabellone.FinePartita();

        //Comunica la fine della partita ai client
        for (GiocatoreRemoto giocatore : this.giocatoriPartita) {
            try{ giocatore.ComunicaFinePartita(mappaRisultati); }
            catch (NetworkException e) {
                System.out.println("Giocatore non più connesso");
            }
        }
    }
}
