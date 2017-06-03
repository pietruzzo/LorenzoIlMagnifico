package server;

import Domain.Giocatore;
import Domain.Tabellone;
import Domain.DTO.UpdateGiocatoreDTO;
import Exceptions.NetworkException;

import java.util.HashMap;

/**
 * Created by Portatile on 18/05/2017.
 */
public abstract class GiocatoreRemoto extends Giocatore{

    /**
     * Indica la partita alla quale il GiocatoreGraphic sta partecipando
     */
    private transient Partita partita;

    protected GiocatoreRemoto(){}

    protected void setPartita(Partita partita) { this.partita = partita; }

    /**
     * Ritorna la partita del GiocatoreGraphic
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
    public void IniziaTurno(int[] ordineGiocatori, int[] esitoDadi, HashMap<Integer, String> mappaCarte) throws NetworkException {}

    /**
     * Comunica ai giocatori l'inizio di una nuova mossa
     */
    public void IniziaMossa(int idGiocatore) throws NetworkException {}

    /**
     * Comunica ai client la scomunica di giocatori
     * @param idGiocatoriScomunicati array degli id dei giocatori scomunicati
     * @param periodo periodo nel quale avviene la scomunica
     */
    public void ComunicaScomunica(int[] idGiocatoriScomunicati, int periodo) throws NetworkException {}

    /**
     * Comunica a determinati giocatori che devono scegliere se sostenere o meno la chiesa
     */
    public void SceltaSostegnoChiesa() throws NetworkException {}


    /**
     * Notifica a tutti i client l'aggiornamento di un giocatore
     * @param update nuove caratteristiche del giocatore
     */
    public void ComunicaAggiornaGiocatore(UpdateGiocatoreDTO update) throws NetworkException {}
}
