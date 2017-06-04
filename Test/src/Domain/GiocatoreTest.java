package Domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michele on 04/06/2017.
 */
public class GiocatoreTest {

    Giocatore giocatore;

    @Before
    public void setUp() throws Exception {
        giocatore = new Giocatore();

        assertEquals(0, giocatore.CarteEdificio.size());
        assertEquals(0, giocatore.CarteTerritorio.size());
        assertEquals(0, giocatore.CartePersonaggio.size());
        assertEquals(0, giocatore.CarteImpresa.size());
        assertEquals(0, giocatore.CarteScomunica.size());
        assertEquals(4, giocatore.Familiari.size());

        giocatore.SettaProprietaIniziali((short)1, "nome", ColoreGiocatore.BLU, 5);

        assertEquals(1, giocatore.getIdGiocatore());
        assertEquals(1, giocatore.getOrdineTurno());
        assertEquals("nome", giocatore.Nome);
        assertEquals(ColoreGiocatore.BLU, giocatore.Colore);
        assertEquals(2, giocatore.Risorse.getLegno());
        assertEquals(2, giocatore.Risorse.getPietra());
        assertEquals(5, giocatore.Risorse.getMonete());
        assertFalse(giocatore.getRapportoVaticanoEffettuato());
    }

    @Test
    public void setOrdineTurno() throws Exception {
        giocatore.setOrdineTurno(5);
        assertEquals(5, giocatore.getOrdineTurno());
    }

    @Test
    public void setRapportoVaticanoEffettuato() throws Exception {
        giocatore.setRapportoVaticanoEffettuato(true);
        assertTrue(giocatore.getRapportoVaticanoEffettuato());
    }

    @Test
    public void ottieniBonusSpazioAzione() throws Exception {
        giocatore.OttieniBonusSpazioAzione(new Risorsa(Risorsa.TipoRisorsa.MONETE, 10));
        assertEquals(15, giocatore.Risorse.getMonete());
    }

    @Test
    public void pagaRisorse() throws Exception {
        giocatore.PagaRisorse(new Risorsa(Risorsa.TipoRisorsa.MONETE, 1));
        assertEquals(4, giocatore.Risorse.getMonete());
    }

    @Test
    public void settaValoreFamiliare() throws Exception {
        int[] esitoDadi = new int[]{1,2,3};
        giocatore.SettaValoreFamiliare(esitoDadi);

        assertEquals(1, giocatore.Familiari.get(0).getValore());
        assertEquals(2, giocatore.Familiari.get(1).getValore());
        assertEquals(3, giocatore.Familiari.get(2).getValore());
        assertEquals(0, giocatore.Familiari.get(3).getValore());
    }

    @Test
    public void sostieniLaChiesa() throws Exception {
        giocatore.Risorse.setRisorse(Risorsa.TipoRisorsa.PFEDE, 10);
        giocatore.SostieniLaChiesa(10);

        assertTrue(giocatore.getRapportoVaticanoEffettuato());
        assertEquals(0, giocatore.Risorse.getPuntiFede());
        assertEquals(10, giocatore.Risorse.getPuntiVittoria());

    }

    @Test
    public void updatePuntiVittoriaByEffettiCarte() throws Exception {

    }

    @Test
    public void getPuntiVittoriaByRisorse() throws Exception {
        giocatore.Risorse.setRisorse(Risorsa.TipoRisorsa.LEGNO, 14);
        assertEquals(21, giocatore.Risorse.getPietra() + giocatore.Risorse.getLegno() + giocatore.Risorse.getMonete()) ;
        assertEquals(4, giocatore.getPuntiVittoriaByRisorse());
    }

}