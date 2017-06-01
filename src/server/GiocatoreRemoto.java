package server;

import Domain.Giocatore;
import Domain.Tabellone;
import Exceptions.DomainException;
import Exceptions.NetworkException;

import java.util.HashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public abstract class GiocatoreRemoto extends Giocatore{

    /**
     * Indica la partita alla quale il giocatore sta partecipando
     */
    private transient Partita partita;

    protected GiocatoreRemoto(){}

    protected void setPartita(Partita partita) { this.partita = partita; }

    /**
     * Ritorna la partita del giocatore
     */
    public Partita getPartita() {
        return partita;
    }

    /**
     * Comunica ai giocatori l'inzio della partita
     */
    public void PartitaIniziata(Tabellone tabellone) throws NetworkException {}

    /**
     * Comunica ai giocatori l'inizio di un nuovo turno di gioco
     */
    public void IniziaTurno(int[] esitoDadi, HashMap<Integer, String> mappaCarte) throws NetworkException {}
}
