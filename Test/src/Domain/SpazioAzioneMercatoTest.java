package Domain;

import Exceptions.DomainException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michele on 10/06/2017.
 */
public class SpazioAzioneMercatoTest {
    SpazioAzioneMercato spazioAzione;
    Giocatore giocatore;
    Familiare familiare;

    @Before
    public void setUp() throws Exception {
        spazioAzione = new SpazioAzioneMercato(1, new Risorsa(Risorsa.TipoRisorsa.LEGNO, 5));
        giocatore = new Giocatore();
        giocatore.SettaProprietaIniziali((short)1, "giocatore", ColoreGiocatore.BLU, 5);
        familiare = giocatore.Familiari.get(0);

        assertEquals(1, spazioAzione.Valore);
        assertEquals(0, spazioAzione.BonusRisorse.getMonete());
        assertEquals(5, spazioAzione.BonusRisorse.getLegno());
        assertNull(spazioAzione.FamiliarePiazzato);
        assertEquals( TipoSpazioAzione.Mercato, spazioAzione.Tipo);
    }

    @Test
    public void piazzaFamiliare() throws Exception {
        familiare.setValore(3);
        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(familiare, spazioAzione.FamiliarePiazzato);
    }

    @Test
    public void validaPiazzamentoFamiliare_Ok() throws Exception {
        familiare.setValore(3);
        spazioAzione.PiazzaFamiliare(familiare, 1);
    }

    @Test (expected = DomainException.class)
    public void validaPiazzamentoFamiliare_Occupato() throws Exception {
        familiare.setValore(3);
        spazioAzione.FamiliarePiazzato = giocatore.Familiari.get(1);
        spazioAzione.PiazzaFamiliare(familiare, 1);
    }

    @Test
    public void rimuoviFamiliari() throws Exception {
        spazioAzione.FamiliarePiazzato = familiare;
        spazioAzione.RimuoviFamiliari();

        assertNull(spazioAzione.FamiliarePiazzato);
    }

    @Test (expected = DomainException.class)
    public void azioneBonusEffettuata() throws Exception {
        spazioAzione.AzioneBonusEffettuata(giocatore, 1, new Risorsa(), 0);
    }
}