package Domain;

import Exceptions.DomainException;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import server.GiocatoreRemotoForTest;
import server.Partita;
import server.Server;

import java.util.HashMap;
import java.util.LinkedHashMap;

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
        tabellone = partita.getTabellone();
        partita.AggiungiGiocatore((short)1, "Michele", new GiocatoreRemotoForTest());
        partita.AggiungiGiocatore((short)2, "Pietro", new GiocatoreRemotoForTest());
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
    public void esistonoFamiliariPiazzabili_TuttiPiazzati() throws Exception {
        this.tabellone.Giocatori.stream().forEach(g -> g.Familiari.stream().forEach(f -> f.SpazioAzioneAttuale = new SpazioAzione()));

        boolean esistonoFamiliariPiazzabili = this.tabellone.EsistonoFamiliariPiazzabili();
        assertFalse(esistonoFamiliariPiazzabili);
    }

    @Test
    public void esistonoFamiliariPiazzabili_NeutriSenzaServi() throws Exception {
        this.tabellone.Giocatori.stream().forEach(g -> g.Familiari.stream().forEach(f ->  f.SpazioAzioneAttuale = (f.ColoreDado != ColoreDado.NEUTRO) ? new SpazioAzione()  : null ));
        this.tabellone.Giocatori.stream().forEach(g -> g.Risorse.setRisorse(Risorsa.TipoRisorsa.SERVI, 0));

        boolean esistonoFamiliariPiazzabili = this.tabellone.EsistonoFamiliariPiazzabili();
        assertFalse(esistonoFamiliariPiazzabili);
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

        tabellone.ScomunicaGiocatore(michele, 1);

        assertTrue(michele.getRapportoVaticanoEffettuato());
        assertEquals(1, michele.CarteScomunica.size());
    }

    @Test
    public void iniziaTurno() throws Exception {
        this.tabellone.Giocatori.stream().forEach(g -> g.Familiari.stream().forEach(f -> f.SpazioAzioneAttuale = new SpazioAzione()));
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Familiare familiareBianco = michele.getFamiliareByColor(ColoreDado.BIANCO);
        Familiare familiareNeutro = michele.getFamiliareByColor(ColoreDado.NEUTRO);

        HashMap<Integer, String> mappaCarte1 = tabellone.IniziaTurno(1);

        tabellone.SpaziAzioneRaccolto.get(0).FamiliariPiazzati.add(familiareBianco);
        tabellone.SpaziAzioneProduzione.get(0).FamiliariPiazzati.add(familiareNeutro);

        HashMap<Integer, String> mappaCarte2 = tabellone.IniziaTurno(1);

        assertTrue(tabellone.Giocatori.stream().allMatch(g -> g.Familiari.stream().allMatch(f -> f.SpazioAzioneAttuale == null)));
        assertTrue(mappaCarte2.values().stream().allMatch(v -> !mappaCarte1.containsValue(v)));
        assertTrue(mappaCarte2.keySet().stream().allMatch(k -> mappaCarte1.containsKey(k)));
        assertTrue(tabellone.Torri.stream().allMatch(t -> t.SpaziAzione.stream().allMatch(s -> s.CartaAssociata != null)));
        assertEquals(16, mappaCarte1.size());
        assertEquals(16, mappaCarte2.size());
        assertEquals(96-16-16, tabellone.getMazzoCarte().size());
    }

    @Test
    public void calcolaPunteggiFinali_SenzaEffetti() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Giocatore pietro = tabellone.getGiocatoreById((short)2);
        Giocatore carlo = tabellone.getGiocatoreById((short)3);

        //Territori
        CartaTerritorio cartaBosco = (CartaTerritorio)tabellone.getCartaByName("Bosco");
        michele.CarteTerritorio.add(cartaBosco);
        michele.CarteTerritorio.add(cartaBosco);
        michele.CarteTerritorio.add(cartaBosco); //3 carte territorio = +1

        pietro.CarteTerritorio.add(cartaBosco); //1 carta territorio = +0

        //Personaggio
        CartaPersonaggio contadino = (CartaPersonaggio)tabellone.getCartaByName("Contadino");
        pietro.CartePersonaggio.add(contadino);
        pietro.CartePersonaggio.add(contadino); //2 carta personaggio = +3

        //Punti Fede
        michele.Risorse.setRisorse(Risorsa.TipoRisorsa.PFEDE, 10); //10 pti fede = +15
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.PFEDE, 5); //5 pti fede = +5

        //Punti Militari
        michele.Risorse.setRisorse(Risorsa.TipoRisorsa.PMILITARI, 5); //Secondo in classifica militare = +2
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.PMILITARI, 10); //Primo in classifica militare = +5

        //Aggiorna in base agli effetti
        //le carte contadino e bosco non hanno effetti da applicare alla fine della partita
        //in questo test non vengono inserite carte scomunica quindi non ci sono altri effetti da applicare

        //Risorse
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.SERVI, 10);
        //michele -> 2 legni 2 pietre 3 servi 5 monete =  12 /5 = +2
        //pietro ->  2 legni 2 pietre 10 servi 6 monete = 20 / 5 = +4
        //carlo ->  2 legni 2 pietre 3 servi 5 monete = 12 / 5 = +2

        michele.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 5);
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 3);
        carlo.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 8);

        //Effettua il conteggio
        tabellone.CalcolaPunteggiFinali();

        //Totali previsti
        //michele   -> 1 + 15 + 2 + 2 = 20 + 5 = 25
        //pietro    -> 3 + 5 + 5 + 4 = 17 + 3 = 20
        //carlo     -> 2 + 8 = 10

        assertEquals(25, michele.Risorse.getPuntiVittoria());
        assertEquals(20, pietro.Risorse.getPuntiVittoria());
        assertEquals(10, carlo.Risorse.getPuntiVittoria());
    }

    @Test
    public void calcolaPunteggiFinali_ConEffetti() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Giocatore pietro = tabellone.getGiocatoreById((short)2);
        Giocatore carlo = tabellone.getGiocatoreById((short)3);

        //Territori
        CartaTerritorio cartaBosco = (CartaTerritorio)tabellone.getCartaByName("Bosco");
        michele.CarteTerritorio.add(cartaBosco);
        michele.CarteTerritorio.add(cartaBosco);
        michele.CarteTerritorio.add(cartaBosco); //3 carte territorio = +1

        pietro.CarteTerritorio.add(cartaBosco); //1 carta territorio = +0

        //Personaggio
        CartaPersonaggio contadino = (CartaPersonaggio)tabellone.getCartaByName("Contadino");
        pietro.CartePersonaggio.add(contadino);
        pietro.CartePersonaggio.add(contadino); //2 carta personaggio = +3

        //Punti Fede
        michele.Risorse.setRisorse(Risorsa.TipoRisorsa.PFEDE, 10); //10 pti fede = +15
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.PFEDE, 5); //5 pti fede = +5

        //Punti Militari
        michele.Risorse.setRisorse(Risorsa.TipoRisorsa.PMILITARI, 5); //Secondo in classifica militare = +2
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.PMILITARI, 10); //Primo in classifica militare = +5

        //Aggiorna in base agli effetti
        //Impresa
        CartaImpresa ingaggiareReclute = (CartaImpresa) tabellone.getCartaByName("IngaggiareReclute");
        CartaImpresa costruireMura = (CartaImpresa) tabellone.getCartaByName("CostruireLeMura");

        michele.CarteImpresa.add(ingaggiareReclute); // Effetto = +4
        pietro.CarteImpresa.add(ingaggiareReclute);
        pietro.CarteImpresa.add(costruireMura);      // Effetti = +7

        //Scomunica
        TesseraScomunica perdiPuntiPerOgniRisorsa = tabellone.getTesseraScomunicaByName("15");
        michele.CarteScomunica.add(perdiPuntiPerOgniRisorsa); //Effetto = (legno + pietra + servitori + monete) * -1

        //Risorse
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.SERVI, 10);
        //michele -> 2 legni 2 pietre 3 servi 5 monete  =  12 / 5 = +2   -12 (scomunica)
        //pietro -> 2 legni 2 pietre 10 servi 6 monete  =  20 / 5 = +4
        //carlo ->  2 legni 2 pietre 3 servi 5 monete = 12 / 5 = +2

        michele.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 8);
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 6);
        carlo.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 8);

        //Effettua il conteggio
        tabellone.CalcolaPunteggiFinali();

        //Totali previsti
        //michele   -> 1 + 15 + 2 + 4 + 2 - 12 = 12 + 8 = 20
        //pietro    -> 3 +  5 + 5 + 7 + 4 = 24 + 6 = 30
        //carlo     -> 2 + 8 = 10

        assertEquals(20, michele.Risorse.getPuntiVittoria());
        assertEquals(30, pietro.Risorse.getPuntiVittoria());
        assertEquals(10, carlo.Risorse.getPuntiVittoria());
    }

    @Test
    public void getBonusVittoriaByPuntiMilitari_PuntiDiversi() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        //michele   1
        //pietro    2
        //carlo     3

        int[] primiGiocatori = new int[]{3};
        int[] secondiGiocatori = new int[]{2};

        int punti = tabellone.getBonusVittoriaByPuntiMilitari(1, primiGiocatori, secondiGiocatori);
        assertEquals(0, punti);

        punti = tabellone.getBonusVittoriaByPuntiMilitari(2, primiGiocatori, secondiGiocatori);
        assertEquals(2, punti);

        punti = tabellone.getBonusVittoriaByPuntiMilitari(3, primiGiocatori, secondiGiocatori);
        assertEquals(5, punti);
    }

    @Test
    public void getBonusVittoriaByPuntiMilitari_DuePrimi() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        //michele   1
        //pietro    2
        //carlo     3

        int[] primiGiocatori = new int[]{3,2};
        int[] secondiGiocatori = new int[]{1};

        int punti = tabellone.getBonusVittoriaByPuntiMilitari(1, primiGiocatori, secondiGiocatori);
        assertEquals(0, punti);

        punti = tabellone.getBonusVittoriaByPuntiMilitari(2, primiGiocatori, secondiGiocatori);
        assertEquals(5, punti);

        punti = tabellone.getBonusVittoriaByPuntiMilitari(3, primiGiocatori, secondiGiocatori);
        assertEquals(5, punti);
    }

    @Test
    public void getBonusVittoriaByPuntiMilitari_DueSecondi() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        //michele   1
        //pietro    2
        //carlo     3

        int[] primiGiocatori = new int[]{3};
        int[] secondiGiocatori = new int[]{1,2};

        int punti = tabellone.getBonusVittoriaByPuntiMilitari(1, primiGiocatori, secondiGiocatori);
        assertEquals(2, punti);

        punti = tabellone.getBonusVittoriaByPuntiMilitari(2, primiGiocatori, secondiGiocatori);
        assertEquals(2, punti);

        punti = tabellone.getBonusVittoriaByPuntiMilitari(3, primiGiocatori, secondiGiocatori);
        assertEquals(5, punti);
    }

    @Test
    public void getBonusVittoriaByPuntiMilitari_TuttiPrimi() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        //michele   1
        //pietro    2
        //carlo     3

        int[] primiGiocatori = new int[]{3, 1, 2};
        int[] secondiGiocatori = new int[]{};

        int punti = tabellone.getBonusVittoriaByPuntiMilitari(1, primiGiocatori, secondiGiocatori);
        assertEquals(5, punti);

        punti = tabellone.getBonusVittoriaByPuntiMilitari(2, primiGiocatori, secondiGiocatori);
        assertEquals(5, punti);

        punti = tabellone.getBonusVittoriaByPuntiMilitari(3, primiGiocatori, secondiGiocatori);
        assertEquals(5, punti);
    }

    @Test
    public void finePartita_PunteggiDiversi() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Giocatore pietro = tabellone.getGiocatoreById((short)2);
        Giocatore carlo = tabellone.getGiocatoreById((short)3);

        michele.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 1);
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 10);
        carlo.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 100);

        LinkedHashMap<Short, Integer>  classifica = tabellone.FinePartita();

        Short idPrimo = classifica.keySet().stream().findFirst().orElse(null);
        Short idSecondo = classifica.keySet().stream().filter(x -> x != idPrimo).findFirst().orElse(null);
        Short idTerzo = classifica.keySet().stream().filter(x -> x != idPrimo && x != idSecondo).findFirst().orElse(null);

        assertEquals(3, idPrimo.intValue());
        assertEquals(2, idSecondo.intValue());
        assertEquals(1, idTerzo.intValue());
    }

    @Test
    public void finePartita_PariPunti() throws Exception {
        tabellone.AggiungiGiocatore((short)3, "Carlo", new Giocatore());
        Giocatore michele = tabellone.getGiocatoreById((short)1);
        Giocatore pietro = tabellone.getGiocatoreById((short)2);
        Giocatore carlo = tabellone.getGiocatoreById((short)3);

        michele.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 10);
        pietro.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 10);
        carlo.Risorse.setRisorse(Risorsa.TipoRisorsa.PVITTORIA, 100);

        LinkedHashMap<Short, Integer>  classifica = tabellone.FinePartita();

        Short idPrimo = classifica.keySet().stream().findFirst().orElse(null);
        Short idSecondo = classifica.keySet().stream().filter(x -> x != idPrimo).findFirst().orElse(null);
        Short idTerzo = classifica.keySet().stream().filter(x -> x != idPrimo && x != idSecondo).findFirst().orElse(null);

        assertEquals(3, idPrimo.intValue());
        assertEquals(1, idSecondo.intValue());
        assertEquals(2, idTerzo.intValue());
    }
}