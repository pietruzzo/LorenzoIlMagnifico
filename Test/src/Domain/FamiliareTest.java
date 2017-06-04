package Domain;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michele on 04/06/2017.
 */
public class FamiliareTest {
    @Test
    public void creaFamiliare() throws Exception{
        Giocatore giocatore = new Giocatore();
        Familiare familiare = new Familiare(giocatore, ColoreDado.ARANCIO);

        assertEquals(ColoreDado.ARANCIO, familiare.ColoreDado);
        assertEquals(0, familiare.getValore());
        assertFalse(familiare.Neutro);
        assertEquals(giocatore, familiare.Giocatore);
    }

    @Test
    public void creaFamiliareNeutro() throws Exception{
        Giocatore giocatore = new Giocatore();
        Familiare familiare = new Familiare(giocatore, ColoreDado.NEUTRO);

        assertEquals(ColoreDado.NEUTRO, familiare.ColoreDado);
        assertEquals(0, familiare.getValore());
        assertTrue(familiare.Neutro);
        assertEquals(giocatore, familiare.Giocatore);
    }

    @Test
    public void setSpazioAzioneAttuale() throws Exception {
        Giocatore giocatore = new Giocatore();
        Familiare familiare = new Familiare(giocatore, ColoreDado.ARANCIO);
        SpazioAzione spazioAzione = new SpazioAzione();
        familiare.SetSpazioAzioneAttuale(spazioAzione);

        assertEquals(spazioAzione, familiare.SpazioAzioneAttuale);
    }

    @Test
    public void setValore() throws Exception {
        Giocatore giocatore = new Giocatore();
        Familiare familiare = new Familiare(giocatore, ColoreDado.ARANCIO);
        familiare.setValore(5);

        assertEquals(5, familiare.getValore());
    }

    @Test
    public void ottieniBonusSpazioAzione() throws Exception {
        Giocatore giocatore = new Giocatore();
        giocatore.Risorse = new Risorsa();

        Familiare familiare = new Familiare(giocatore, ColoreDado.ARANCIO);
        SpazioAzione spazioAzione = new SpazioAzione(1, new Risorsa(Risorsa.TipoRisorsa.LEGNO, 2));
        familiare.SetSpazioAzioneAttuale(spazioAzione);
        familiare.OttieniBonusSpazioAzione();

        assertEquals(2, giocatore.Risorse.getLegno());
    }
}