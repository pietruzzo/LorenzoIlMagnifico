package Domain;

import Domain.Effetti.Effetto;
import Domain.Effetti.lista.EffettoEndGame;
import Domain.Effetti.lista.ScambiaRisorse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        giocatore.OttieniBonusRisorse(new Risorsa(Risorsa.TipoRisorsa.MONETE, 10));
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
        //Il giocatore parte senza punti vittoria

        //Crea la carta Impresa 'Campagna Militare'
        Effetto cinquePuntiFine = new EffettoEndGame(5);
        Risorsa[] costoRisorse = new Risorsa[]{new Risorsa(Risorsa.TipoRisorsa.PMILITARI, 3)};
        Risorsa[] guadagniRisorse = new Risorsa[]{new Risorsa(Risorsa.TipoRisorsa.PMILITARI, 1)};
        Effetto costoCarta = new ScambiaRisorse(costoRisorse, guadagniRisorse, true);
        Effetto immediato = new ScambiaRisorse(new Risorsa[]{new Risorsa()}, new Risorsa[]{new Risorsa(Risorsa.TipoRisorsa.MONETE, 3)}, false);

        List<Effetto> effImmediati = new ArrayList<>();
        effImmediati.add(costoCarta);
        effImmediati.add(immediato);

        List<Effetto> effPermanenti = new ArrayList<>();
        effPermanenti.add(cinquePuntiFine);

        CartaImpresa campagnaMilitare = new CartaImpresa("CampagnaMilitare", 1, effImmediati, effPermanenti);
        giocatore.CarteImpresa.add(campagnaMilitare);

        //Aggiorna i punti vittoria del giocatore
        giocatore.updatePuntiVittoriaByEffettiCarte();

        //Essendo partito senza punti vittoria e avendo solo la carta 'CampagnaMilitare' che aggiunge 5 punti alla fine
        //Il punteggio atteso del giocatore è di 5 punti vittoria
        assertEquals(5, giocatore.Risorse.getPuntiVittoria());
    }

    @Test
    public void getPuntiVittoriaByRisorse() throws Exception {
        giocatore.Risorse.setRisorse(Risorsa.TipoRisorsa.LEGNO, 14);
        assertEquals(21, giocatore.Risorse.getPietra() + giocatore.Risorse.getLegno() + giocatore.Risorse.getMonete()) ;
        assertEquals(4, giocatore.getPuntiVittoriaByRisorse());
    }

}