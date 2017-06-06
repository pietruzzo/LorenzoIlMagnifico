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
        tesseraScomunica = new TesseraScomunica(1, new ArrayList<Effetto>());

        assertEquals("", tesseraScomunica.Nome);
        assertEquals(1, tesseraScomunica.Periodo);
        assertEquals(0, tesseraScomunica.CostoRisorse.getLegno());
        assertEquals(0, tesseraScomunica.CostoRisorse.getPietra());
        assertEquals(0, tesseraScomunica.CostoRisorse.getServi());
        assertEquals(0, tesseraScomunica.CostoRisorse.getMonete());
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