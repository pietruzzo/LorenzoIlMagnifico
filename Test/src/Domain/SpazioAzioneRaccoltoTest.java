package Domain;

import Exceptions.DomainException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Michele on 10/06/2017.
 */
public class SpazioAzioneRaccoltoTest {
    SpazioAzioneRaccolto spazioAzione;
    Giocatore giocatore;
    Familiare familiare;
    Tabellone tabellone;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        tabellone = new Tabellone();
        spazioAzione = new SpazioAzioneRaccolto(1, 2, -3, tabellone);
        giocatore = new Giocatore();
        giocatore.SettaProprietaIniziali((short)1, "giocatore", ColoreGiocatore.BLU, 5);
        familiare = giocatore.Familiari.get(0);
        familiare.setValore(3);

        assertEquals(1, spazioAzione.Valore);
        assertEquals(0, spazioAzione.BonusRisorse.getMonete());
        assertEquals(0, spazioAzione.FamiliariPiazzati.size());
        assertEquals(-3, spazioAzione.MalusValore);
        assertEquals(2, spazioAzione.LimiteFamiliari);
        assertEquals( tabellone, spazioAzione.tabellone);
        assertEquals( TipoSpazioAzione.Raccolto, spazioAzione.Tipo);
    }

    @Test
    public void piazzaFamiliare() throws Exception {
        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(1, spazioAzione.FamiliariPiazzati.size());
    }

    @Test
    public void validaPiazzamentoFamiliare_Ok() throws Exception {
        spazioAzione.ValidaPiazzamentoFamiliare(familiare, 1);
    }

    @Test
    public void validaPiazzamentoFamiliare_LimiteRaggiunto() throws Exception {
        exception.expect(DomainException.class);
        exception.expectMessage("E' stato raggiunto il numero massimo di familiari per questo spazio azione!");

        spazioAzione.FamiliariPiazzati.add(giocatore.Familiari.get(1));
        spazioAzione.FamiliariPiazzati.add(giocatore.Familiari.get(2));

        spazioAzione.ValidaPiazzamentoFamiliare(familiare, 1);
    }

    @Test
    public void validaPiazzamentoFamiliare_ColoreGiaPresente() throws Exception {
        exception.expect(DomainException.class);
        exception.expectMessage("E' già presente un familiare di questo colore in questo spazio azione!");

        Familiare familiareArancio = giocatore.getFamiliareByColor(ColoreDado.ARANCIO);
        Familiare familiareNero = giocatore.getFamiliareByColor(ColoreDado.NERO);

        spazioAzione.FamiliariPiazzati.add(familiareArancio);
        spazioAzione.ValidaPiazzamentoFamiliare(familiareNero, 1);
    }

    @Test
    public void rimuoviFamiliari() throws Exception {
        spazioAzione.FamiliariPiazzati.add(familiare);
        spazioAzione.RimuoviFamiliari();

        assertEquals(0, spazioAzione.FamiliariPiazzati.size());
    }

    @Test
    public void azioneBonusEffettuata() throws Exception {

        CartaTerritorio cittaMercantile = (CartaTerritorio) tabellone.getCartaByName("CittaMercantile");
        CartaTerritorio rocca = (CartaTerritorio) tabellone.getCartaByName("Rocca");
        giocatore.CarteTerritorio.add(cittaMercantile);
        giocatore.CarteTerritorio.add(rocca);

        //Si attiverà solamente l'effetto della carta 'CittaMercantile', che richiede un valore di attivazione pari a 1
        //Non si attiverà quello della Rocca, che richiede un valore di 5 (siccome c'è il malus di -3)
        spazioAzione.AzioneBonusEffettuata(giocatore, 4, new Risorsa(), 1);

        assertEquals(8, giocatore.Risorse.getMonete()); //Ne guadagna 3 con lo scambia risorse
        assertEquals(2, giocatore.Risorse.getServi()); //Ne perde uno per averlo aggiunto al valore
    }

}