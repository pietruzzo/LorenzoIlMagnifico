package server;

import Domain.Dado;
import Domain.Giocatore;
import Domain.Tabellone;
import Exceptions.DomainException;
import Exceptions.NetworkException;
import sun.nio.ch.Net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


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
        this.periodo = 1;
        this.ordineMossaCorrente = 0;
        this.tabellone = new Tabellone(this);
        this.giocatoriPartita = new ArrayList<>();
        this.esitoDadi = new int[NUM_DADI];
    }

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

    /**
     * Aggiunge un GiocatoreGraphic alla partita
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
        if(this.turno % 2 == 0) this.periodo++;

        //Inizializza i dadi per il turno di gioco
        for (int i = 0; i < NUM_DADI; i++)
        {
            this.esitoDadi[i] = Dado.lancia();
        }

        //Inizializza il valore dei familiari in base all'esito dei dadi
        this.giocatoriPartita.forEach(x -> x.SettaValoreFamiliare(esitoDadi));

        //"Pulisce" il tabellone e pesca le 16 carte da mettere sulle torri
        HashMap<Integer, String> mappaCarte = this.tabellone.IniziaTurno();

        //Comunica ai giocatori l'inzio del nuovo turno
        for (GiocatoreRemoto giocatore : this.giocatoriPartita) {
            try{ giocatore.IniziaTurno(this.esitoDadi, mappaCarte); }
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
                    //TODO: comunica Rapporto in Vaticano
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
}
