package Domain;

import Domain.Effetti.Effetto;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Michele on 04/06/2017.
 */
public class TesseraScomunicaTest {
    TesseraScomunica tesseraScomunica;

    @Before
    public void setUp() throws Exception {
        tesseraScomunica = new TesseraScomunica("id", 1, new ArrayList<>());

        assertEquals("id", tesseraScomunica.Nome);
        assertEquals(1, tesseraScomunica.Periodo);
        assertEquals(0, tesseraScomunica.getCostoRisorse().getLegno());
        assertEquals(0, tesseraScomunica.getCostoRisorse().getPietra());
        assertEquals(0, tesseraScomunica.getCostoRisorse().getServi());
        assertEquals(0, tesseraScomunica.getCostoRisorse().getMonete());
        assertNull(tesseraScomunica.getEffettoImmediato());
        assertNotNull(tesseraScomunica.getEffettoPermanente());
    }

    @Test
    public void assegnaGiocatore() throws Exception {
        Giocatore giocatore = new Giocatore();
        tesseraScomunica.AssegnaGiocatore(giocatore);

        assertEquals(1, giocatore.CarteScomunica.size());
    }

    @Test
    public void getTipoCarta() throws Exception {
        assertEquals(TipoCarta.Scomunica, tesseraScomunica.getTipoCarta());
    }
}