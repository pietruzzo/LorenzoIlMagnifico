package server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michele on 09/06/2017.
 */
public class PartitaTest {
    Partita partita;

    @Before
    public void setUp() throws Exception {
        partita = new Partita(new Server());
        partita.AggiungiGiocatore((short)1, "Michele", new GiocatoreRemotoForTest());
        partita.AggiungiGiocatore((short)2, "Pietro", new GiocatoreRemotoForTest());

        assertEquals(2, partita.getGiocatoriPartita().size());
        assertTrue(partita.getGiocatoriPartita().stream().allMatch(g -> g.getPartita().equals(partita)));
    }

    @Test
    public void inizioNuovoTurno() throws Exception {
        partita.InizioNuovoTurno();

        assertEquals(1, partita.getTurno());
        assertEquals(1, partita.getOrdineMossaCorrente());
        assertEquals(1, partita.getPeriodo());
    }

    @Test
    public void iniziaNuovaMossa() throws Exception {

    }

    @Test
    public void rispostaSostegnoChiesa() throws Exception {
    }

    @Test
    public void finePartita() throws Exception {
    }

}