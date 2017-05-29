package server;

import Domain.Tabellone;
import Exceptions.DomainException;
import Exceptions.NetworkException;
import sun.nio.ch.Net;

import java.util.ArrayList;


/**
 * Created by Portatile on 23/05/2017.
 */
public class Partita {

    //region Proprieta
    private static final short MIN_GIOCATORI = 2;
    private static final short MAX_GIOCATORI = 4;
    private static final Object MUTEX_PARTITA = new Object();

    private Server server;
    private Tabellone tabellone;
    private ArrayList<GiocatoreRemoto> giocatoriPartita;
    private boolean iniziata;
    //endregion

    /**
     * Costruttore partita
     */
    public Partita(Server server)
    {
        this.server = server;
        this.iniziata = false;
        this.tabellone = new Tabellone(this);
        this.giocatoriPartita = new ArrayList<>();
    }

    /**
     * Ritorna true se la partita è iniziata
     */
    public boolean isIniziata() {
        return iniziata;
    }

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
                        try{ giocatore.PartitaIniziata(); }
                        catch (NetworkException e) {
                            System.out.println("Giocatore non più connesso");
                        }
                    }
                }
            }
        }
        else
            throw new DomainException("E' necessario che ci siano almeno due utenti connessi alla partita per cominciare.");
    }
}
