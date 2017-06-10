package Domain;

import Exceptions.DomainException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michele on 10/06/2017.
 */
public class SpazioAzioneConsiglioTest {
    SpazioAzioneConsiglio spazioAzione;
    Giocatore giocatore;
    Familiare familiare;

    @Before
    public void setUp() throws Exception {
        spazioAzione = new SpazioAzioneConsiglio(1, new Risorsa());
        giocatore = new Giocatore();
        giocatore.SettaProprietaIniziali((short)1, "giocatore", ColoreGiocatore.BLU, 5);
        familiare = giocatore.Familiari.get(0);

        assertEquals(1, spazioAzione.Valore);
        assertEquals(0, spazioAzione.BonusRisorse.getMonete());
        assertEquals(0, spazioAzione.FamiliariPiazzati.size());
        assertEquals( TipoSpazioAzione.Consiglio, spazioAzione.Tipo);
    }

    @Test
    public void piazzaFamiliare() throws Exception {
        familiare.setValore(3);
        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(1, spazioAzione.FamiliariPiazzati.size());
        //assertEquals(1, giocatore.getPrivilegiDaScegliere());
    }

    @Test
    public void rimuoviFamiliari() throws Exception {
        spazioAzione.FamiliariPiazzati.add(familiare);
        spazioAzione.RimuoviFamiliari();

        assertEquals(0, spazioAzione.FamiliariPiazzati.size());
    }

    @Test (expected = DomainException.class)
    public void azioneBonusEffettuata() throws Exception {
        spazioAzione.AzioneBonusEffettuata(giocatore, 1, new Risorsa(), 0);
    }

}