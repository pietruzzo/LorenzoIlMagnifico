package server;

import Domain.Giocatore;
import Exceptions.DomainException;
import Exceptions.NetworkException;

/**
 * Created by Portatile on 18/05/2017.
 */
public abstract class GiocatoreRemoto extends Giocatore{

    /**
     * Indica la partita alla quale il giocatore sta partecipando
     */
    private Partita partita;

    protected GiocatoreRemoto(){}

    protected void setPartita(Partita partita) { this.partita = partita; };

    /**
     * Ritorna la partita del giocatore
     */
    public Partita getPartita() {
        return partita;
    }

    public void PartitaIniziata() throws NetworkException {}
}
