package Domain;

import Domain.Effetti.Effetto;
import Domain.Effetti.lista.AumentaValoreAzione;
import Exceptions.DomainException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Michele on 10/06/2017.
 */
public class SpazioAzioneTest {
    SpazioAzione spazioAzione;
    Giocatore giocatore;
    Familiare familiare;

    @Before
    public void setUp() throws Exception {
        spazioAzione = new SpazioAzione(5, new Risorsa(Risorsa.TipoRisorsa.MONETE, 5));
        giocatore = new Giocatore();
        giocatore.SettaProprietaIniziali((short)1, "giocatore", ColoreGiocatore.BLU, 5);
        familiare = giocatore.Familiari.get(0);

        assertEquals(5, spazioAzione.Valore);
        assertEquals(5, spazioAzione.BonusRisorse.getMonete());
    }

    @Test
    public void piazzaFamiliare() throws Exception {
        familiare.setValore(4);

        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(2, giocatore.Risorse.getServi());
        assertEquals(10, giocatore.Risorse.getMonete());
        assertEquals(spazioAzione.getIdSpazioAzione(), familiare.SpazioAzioneAttuale.getIdSpazioAzione());
    }

    @Test
    public void validaValoreAzione_OK() throws Exception {
        familiare.setValore(3);
        spazioAzione.ValidaValoreAzione(familiare, 2);
    }

    @Test (expected = DomainException.class)
    public void validaValoreAzione_NeedMoreServant() throws Exception {
        familiare.setValore(3);
        spazioAzione.ValidaValoreAzione(familiare, 1);
    }

    @Test (expected = DomainException.class)
    public void validaValoreAzione_CausaEffetto() throws Exception {
        familiare.setValore(3);

        Effetto abbassaValore = new AumentaValoreAzione(TipoAzione.GENERICA, -3);
        ArrayList<Effetto> effettiPermanenti = new ArrayList<>();
        effettiPermanenti.add(abbassaValore);
        TesseraScomunica tessera = new TesseraScomunica("id", 1, effettiPermanenti);
        giocatore.CarteScomunica.add(tessera);

        spazioAzione.ValidaValoreAzione(familiare, 2);
    }

    @Test
    public void azioneBonusEffettuata() throws Exception {
        spazioAzione.AzioneBonusEffettuata(giocatore, 5, new Risorsa(), 1);

        assertEquals(2, giocatore.Risorse.getServi());
        assertEquals(10, giocatore.Risorse.getMonete());
    }

    @Test
    public void validaValoreAzioneBonus_Ok() throws Exception {
        spazioAzione.ValidaValoreAzioneBonus(giocatore, 4, 1);
    }

    @Test (expected = DomainException.class)
    public void validaValoreAzioneBonus_NeedMoreServant() throws Exception {
        spazioAzione.ValidaValoreAzioneBonus(giocatore, 3, 1);
    }

    @Test (expected = DomainException.class)
    public void validaValoreAzioneBonus_CausaEffetto() throws Exception {

        Effetto abbassaValore = new AumentaValoreAzione(TipoAzione.GENERICA, -3);
        ArrayList<Effetto> effettiPermanenti = new ArrayList<>();
        effettiPermanenti.add(abbassaValore);
        TesseraScomunica tessera = new TesseraScomunica("id", 1, effettiPermanenti);
        giocatore.CarteScomunica.add(tessera);

        spazioAzione.ValidaValoreAzioneBonus(giocatore, 3, 2);
    }
}