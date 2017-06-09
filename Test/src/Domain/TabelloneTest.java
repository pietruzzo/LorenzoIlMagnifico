package Domain;

import Exceptions.DomainException;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import server.Partita;
import server.Server;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Michele on 09/06/2017.
 */
public class TabelloneTest {
    Tabellone tabellone;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        Partita partita = new Partita(new Server());
        tabellone = new Tabellone(partita);
        tabellone.AggiungiGiocatore((short)1, "Michele", new Giocatore());
        tabellone.AggiungiGiocatore((short)2, "Pietro", new Giocatore());

    }

    @Test
    public void aggiungiGiocatore_OK() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());

        assertEquals(3, tabellone.Giocatori.size());
        assertEquals("Carlo", tabellone.getNomeGiocatoreById((short)3));
        assertEquals(7, tabellone.Giocatori.get(2).Risorse.getMonete());
    }

    @Test
    public void aggiungiGiocatore_DoppioUsername() throws Exception {
        exception.expect(DomainException.class);
        exception.expectMessage("Esiste già un giocatore con lo stesso username.");

        tabellone.AggiungiGiocatore((short)3, "Michele", new Giocatore());
    }

    @Test
    public void aggiungiGiocatore_PiuDiQuattro() throws Exception {
        exception.expect(DomainException.class);
        exception.expectMessage("E' stato raggiunto il numero limite di giocatori");

        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        tabellone.AggiungiGiocatore((short)4, "Matteo", new Giocatore());
        tabellone.AggiungiGiocatore((short)5, "Extra", new Giocatore());
    }


    @Test
    public void aggiornaOrdineGiocatori_UnGiocatore() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Giocatore pietro = tabellone.getGiocatoreById((short)2);
        Giocatore carlo = tabellone.getGiocatoreById((short)3);


        tabellone.SpazioAzioneConsiglio.FamiliariPiazzati.add(carlo.Familiari.get(0));
        tabellone.AggiornaOrdineGiocatori();

        assertEquals(1, carlo.getOrdineTurno());
        assertEquals(2, michele.getOrdineTurno());
        assertEquals(3, pietro.getOrdineTurno());
    }

    @Test
    public void aggiornaOrdineGiocatori_DueGiocatori() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Giocatore pietro = tabellone.getGiocatoreById((short)2);
        Giocatore carlo = tabellone.getGiocatoreById((short)3);

        tabellone.SpazioAzioneConsiglio.FamiliariPiazzati.add(carlo.Familiari.get(0));
        tabellone.SpazioAzioneConsiglio.FamiliariPiazzati.add(pietro.Familiari.get(0));
        tabellone.AggiornaOrdineGiocatori();

        assertEquals(1, carlo.getOrdineTurno());
        assertEquals(2, pietro.getOrdineTurno());
        assertEquals(3, michele.getOrdineTurno());
    }

    @Test
    public void aggiornaOrdineGiocatori_ZeroTreGiocatori() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Giocatore pietro = tabellone.getGiocatoreById((short)2);
        Giocatore carlo = tabellone.getGiocatoreById((short)3);

        tabellone.AggiornaOrdineGiocatori();
        assertEquals(1, michele.getOrdineTurno());
        assertEquals(2, pietro.getOrdineTurno());
        assertEquals(3, carlo.getOrdineTurno());

        tabellone.SpazioAzioneConsiglio.FamiliariPiazzati.add(michele.Familiari.get(0));
        tabellone.SpazioAzioneConsiglio.FamiliariPiazzati.add(carlo.Familiari.get(0));
        tabellone.SpazioAzioneConsiglio.FamiliariPiazzati.add(pietro.Familiari.get(0));
        tabellone.AggiornaOrdineGiocatori();

        assertEquals(1, michele.getOrdineTurno());
        assertEquals(2, carlo.getOrdineTurno());
        assertEquals(3, pietro.getOrdineTurno());
    }

    @Test
    public void piazzaFamiliare() throws Exception {
        //TODO
    }

    @Test
    public void validaPiazzamentoFamiliareProduzione_Vuoto() throws Exception {
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        tabellone.ValidaPiazzamentoFamiliareProduzione(michele.Familiari.get(0));
    }

    @Test
    public void validaPiazzamentoFamiliareProduzione_NeutroColorato() throws Exception {
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Familiare familiareBianco = michele.getFamiliareByColor(ColoreDado.BIANCO);
        Familiare familiareNeutro = michele.getFamiliareByColor(ColoreDado.NEUTRO);

        tabellone.ValidaPiazzamentoFamiliareProduzione(familiareBianco);
        tabellone.SpaziAzioneProduzione.get(0).FamiliariPiazzati.add(familiareBianco);

        tabellone.ValidaPiazzamentoFamiliareProduzione(familiareNeutro);
    }

    @Test (expected = DomainException.class)
    public void validaPiazzamentoFamiliareProduzione_DueColorati() throws Exception {
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Familiare familiareBianco = michele.getFamiliareByColor(ColoreDado.BIANCO);
        Familiare familiareNero = michele.getFamiliareByColor(ColoreDado.NERO);

        tabellone.ValidaPiazzamentoFamiliareProduzione(familiareBianco);
        tabellone.SpaziAzioneProduzione.get(0).FamiliariPiazzati.add(familiareBianco);

        tabellone.ValidaPiazzamentoFamiliareProduzione(familiareNero);
    }

    @Test
    public void validaPiazzamentoFamiliareRaccolto_Vuoto() throws Exception {
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        tabellone.ValidaPiazzamentoFamiliareRaccolto(michele.Familiari.get(0));
    }

    @Test
    public void validaPiazzamentoFamiliareRaccolto_NeutroColorato() throws Exception {
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Familiare familiareBianco = michele.getFamiliareByColor(ColoreDado.BIANCO);
        Familiare familiareNeutro = michele.getFamiliareByColor(ColoreDado.NEUTRO);

        tabellone.ValidaPiazzamentoFamiliareRaccolto(familiareBianco);
        tabellone.SpaziAzioneRaccolto.get(0).FamiliariPiazzati.add(familiareBianco);

        tabellone.ValidaPiazzamentoFamiliareRaccolto(familiareNeutro);
    }

    @Test (expected = DomainException.class)
    public void validaPiazzamentoFamiliareRaccolto_DueColorati() throws Exception {
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Familiare familiareBianco = michele.getFamiliareByColor(ColoreDado.BIANCO);
        Familiare familiareNero = michele.getFamiliareByColor(ColoreDado.NERO);

        tabellone.ValidaPiazzamentoFamiliareRaccolto(familiareBianco);
        tabellone.SpaziAzioneRaccolto.get(0).FamiliariPiazzati.add(familiareBianco);

        tabellone.ValidaPiazzamentoFamiliareRaccolto(familiareNero);
    }

    @Test
    public void esistonoFamiliariPiazzabili_True() throws Exception {
        boolean esistonoFamiliariPiazzabili = this.tabellone.EsistonoFamiliariPiazzabili();
        assertTrue(esistonoFamiliariPiazzabili);
    }

    @Test
    public void esistonoFamiliariPiazzabili_False() throws Exception {
        this.tabellone.Giocatori.stream().forEach(g -> g.Familiari.stream().forEach(f -> f.SpazioAzioneAttuale = new SpazioAzione()));

        boolean esistonoFamiliariPiazzabili = this.tabellone.EsistonoFamiliariPiazzabili();
        assertFalse(esistonoFamiliariPiazzabili);
    }

    @Test
    public void esistonoFamiliariPiazzabili_False2() throws Exception {
        this.tabellone.Giocatori.stream().forEach(g -> g.Familiari.stream().forEach(f ->  f.SpazioAzioneAttuale = (f.ColoreDado != ColoreDado.NEUTRO) ? new SpazioAzione()  : null ));
        this.tabellone.Giocatori.stream().forEach(g -> g.Risorse.setRisorse(Risorsa.TipoRisorsa.SERVI, 0));

        boolean esistonoFamiliariPiazzabili = this.tabellone.EsistonoFamiliariPiazzabili();
        assertFalse(esistonoFamiliariPiazzabili);
    }

    @Test
    public void azioneBonusEffettuata() throws Exception {
        //TODO
    }

    @Test
    public void riscuotiPrivilegiDelConsiglio() throws Exception {
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        michele.incrementaPrivilegiDaScegliere();
        michele.incrementaPrivilegiDaScegliere();

        tabellone.RiscuotiPrivilegiDelConsiglio(michele.getIdGiocatore(), new Risorsa(Risorsa.TipoRisorsa.LEGNO, 10));

        assertEquals(1, michele.getPrivilegiDaScegliere());
        assertEquals(12, michele.Risorse.getLegno());
    }

    @Test
    public void scomunicaGiocatore() throws Exception {
        Giocatore michele = tabellone.getGiocatoreById((short)1);

        tabellone.ScomunicaGiocatore(michele);

        assertTrue(michele.getRapportoVaticanoEffettuato());
        assertEquals(1, michele.CarteScomunica.size());
    }

    @Test
    public void iniziaTurno() throws Exception {
        this.tabellone.Giocatori.stream().forEach(g -> g.Familiari.stream().forEach(f -> f.SpazioAzioneAttuale = new SpazioAzione()));
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Familiare familiareBianco = michele.getFamiliareByColor(ColoreDado.BIANCO);
        Familiare familiareNeutro = michele.getFamiliareByColor(ColoreDado.NEUTRO);

        HashMap<Integer, String> mappaCarte1 = tabellone.IniziaTurno();

        tabellone.SpaziAzioneRaccolto.get(0).FamiliariPiazzati.add(familiareBianco);
        tabellone.SpaziAzioneProduzione.get(0).FamiliariPiazzati.add(familiareNeutro);

        HashMap<Integer, String> mappaCarte2 = tabellone.IniziaTurno();

        assertTrue(tabellone.Giocatori.stream().allMatch(g -> g.Familiari.stream().allMatch(f -> f.SpazioAzioneAttuale == null)));
        assertTrue(mappaCarte2.values().stream().allMatch(v -> !mappaCarte1.containsValue(v)));
        assertTrue(mappaCarte2.keySet().stream().allMatch(k -> mappaCarte1.containsKey(k)));
        assertTrue(tabellone.Torri.stream().allMatch(t -> t.SpaziAzione.stream().allMatch(s -> s.CartaAssociata != null)));
        assertEquals(16, mappaCarte1.size());
        assertEquals(16, mappaCarte2.size());
        assertEquals(96-16-16, tabellone.getMazzoCarte().size());
    }

    @Test
    public void calcolaPunteggiFinali() throws Exception {
        //TODO
    }

}