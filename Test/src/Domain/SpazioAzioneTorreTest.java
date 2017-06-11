package Domain;

import Exceptions.DomainException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import server.GiocatoreRemoto;
import server.socket.GiocatoreSocket;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Michele on 10/06/2017.
 */
public class SpazioAzioneTorreTest {
    SpazioAzioneTorre spazioAzione;
    Giocatore giocatore;
    Familiare familiare;
    Tabellone tabellone;
    Torre torre;


    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        tabellone = new Tabellone();
        torre = tabellone.Torri.get(0);
        spazioAzione = torre.SpaziAzione.get(2);
        giocatore = new Giocatore();
        giocatore.SettaProprietaIniziali((short)1, "giocatore", ColoreGiocatore.BLU, 5);
        familiare = giocatore.getFamiliareByColor(ColoreDado.ARANCIO);
        familiare.setValore(4);

        assertEquals(5, spazioAzione.Valore);
        assertEquals(1, spazioAzione.BonusRisorse.getLegno());
        assertEquals( torre, spazioAzione.Torre);
        assertEquals( TipoSpazioAzione.Torre, spazioAzione.Tipo);
        assertNull(spazioAzione.FamiliarePiazzato);
    }

    @Test
    public void associaCarta() throws Exception {
        Carta carta = new CartaEdificio("edificio", 1, 0, new ArrayList<>(), new ArrayList<>());

        spazioAzione.AssociaCarta(carta);
        assertEquals(carta, spazioAzione.CartaAssociata);
    }

    @Test
    public void piazzaFamiliare_TorreLibera_Ok() throws Exception {
        Carta messoPapale = tabellone.getCartaByName("MessoPapale");
        spazioAzione.AssociaCarta(messoPapale);
        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(familiare, spazioAzione.FamiliarePiazzato);
        assertEquals(0, giocatore.Risorse.getMonete()); //Spese per la carta
        assertEquals(2, giocatore.Risorse.getPietra()); //Rimaste invariate
        assertEquals(3, giocatore.Risorse.getLegno()); //Prese dal bonus spazio azione
        assertEquals(3, giocatore.Risorse.getPuntiFede()); //Prese dall'effetto immediato della carta
        assertFalse(giocatore.getAzioneBonusDaEffettuare());
        assertTrue(giocatore.CartePersonaggio.contains(messoPapale));
        assertNull(spazioAzione.CartaAssociata);
    }

    @Test
    public void piazzaFamiliare_TorreOccupata_Ok() throws Exception {
        SpazioAzioneTorre primoSP = torre.SpaziAzione.get(0);
        //Il familiare neutro può coesistere nella stessa torre con un altro familiare del giocatore
        primoSP.FamiliarePiazzato = giocatore.getFamiliareByColor(ColoreDado.NEUTRO);
        giocatore.Risorse.setRisorse(Risorsa.TipoRisorsa.LEGNO, 3);

        Carta accogliereGliStranieri = tabellone.getCartaByName("AccogliereGliStranieri");
        spazioAzione.AssociaCarta(accogliereGliStranieri);
        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(familiare, spazioAzione.FamiliarePiazzato);
        assertEquals(2, giocatore.Risorse.getMonete()); //Spese per la torre occupata
        assertEquals(0, giocatore.Risorse.getLegno()); //Prese dal bonus e spese per la carta
        assertEquals(7, giocatore.Risorse.getServi()); //Presi dall'effetto immediato della carta e spesi per aumentare il valore
        assertFalse(giocatore.getAzioneBonusDaEffettuare());
        assertTrue(giocatore.CarteImpresa.contains(accogliereGliStranieri));
        assertNull(spazioAzione.CartaAssociata);
    }

    @Test
    public void piazzaFamiliare_EffettoPergamena() throws Exception {
        Carta costruireBastioni = tabellone.getCartaByName("CostruireBastioni");
        spazioAzione.AssociaCarta(costruireBastioni);
        giocatore.Risorse.setRisorse(Risorsa.TipoRisorsa.PIETRA, 4);

        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(familiare, spazioAzione.FamiliarePiazzato);
        assertEquals(5, giocatore.Risorse.getMonete()); //Immutate
        assertEquals(0, giocatore.Risorse.getPietra()); //Spese per la carta
        assertEquals(3, giocatore.Risorse.getLegno()); //Prese dal bonus spazio azione
        assertEquals(3, giocatore.Risorse.getPuntiMilitari()); //Presi dall'effetto immediato della carta

        //funziona solo se eseguito da GiocatoreRemoto
        //assertEquals(1, giocatore.getPrivilegiDaScegliere()); //Effetto immediato
        assertFalse(giocatore.getAzioneBonusDaEffettuare());
        assertTrue(giocatore.CarteImpresa.contains(costruireBastioni));
        assertNull(spazioAzione.CartaAssociata);
    }

    @Test
    public void piazzaFamiliare_EffettoAzioneBonus() throws Exception {
        Carta capitano = tabellone.getCartaByName("Capitano");
        spazioAzione.AssociaCarta(capitano);

        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(familiare, spazioAzione.FamiliarePiazzato);
        assertEquals(1, giocatore.Risorse.getMonete()); //Spese per la carta
        assertEquals(3, giocatore.Risorse.getLegno()); //Prese dal bonus spazio azione
        assertEquals(2, giocatore.Risorse.getPuntiMilitari()); //Presi dall'effetto immediato della carta

        //funziona solo se eseguito da GiocatoreRemoto
        //assertTrue(giocatore.getAzioneBonusDaEffettuare()); //Effetto immediato
        assertTrue(giocatore.CartePersonaggio.contains(capitano));
        assertNull(spazioAzione.CartaAssociata);
    }

    @Test
    public void piazzaFamiliare_EffettoRisorsaPerRisorsa() throws Exception {
        Carta generale = tabellone.getCartaByName("Generale");
        spazioAzione.AssociaCarta(generale);
        giocatore.Risorse.setRisorse(Risorsa.TipoRisorsa.PMILITARI, 10);

        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(familiare, spazioAzione.FamiliarePiazzato);
        assertEquals(0, giocatore.Risorse.getMonete()); //Spese per la carta
        assertEquals(3, giocatore.Risorse.getLegno()); //Prese dal bonus spazio azione
        assertEquals(10, giocatore.Risorse.getPuntiMilitari()); //Assegnati nel test
        assertEquals(5, giocatore.Risorse.getPuntiVittoria()); //Presi dall'effetto immediato della carta

        assertFalse(giocatore.getAzioneBonusDaEffettuare());
        assertTrue(giocatore.CartePersonaggio.contains(generale));
        assertNull(spazioAzione.CartaAssociata);
    }

    @Test
    public void piazzaFamiliare_EffettoRisorsaPerCarte() throws Exception {
        Carta governatore = tabellone.getCartaByName("Governatore");
        spazioAzione.AssociaCarta(governatore);
        CartaEdificio cartaEdificio = new CartaEdificio("carta", 1, 0, new ArrayList<>(), new ArrayList<>());
        //Assegna 3 carte edificio al giocatore
        giocatore.CarteEdificio.add(cartaEdificio);
        giocatore.CarteEdificio.add(cartaEdificio);
        giocatore.CarteEdificio.add(cartaEdificio);
        //Assegna le risorse necessarie
        giocatore.Risorse.setRisorse(Risorsa.TipoRisorsa.MONETE, 10);

        spazioAzione.PiazzaFamiliare(familiare, 1);

        assertEquals(familiare, spazioAzione.FamiliarePiazzato);
        assertEquals(4, giocatore.Risorse.getMonete()); //Spese per la carta (costa 6)
        assertEquals(3, giocatore.Risorse.getLegno()); //Prese dal bonus spazio azione
        assertEquals(3, giocatore.CarteEdificio.size()); //Assegnati nel test
        assertEquals(6, giocatore.Risorse.getPuntiVittoria()); //Presi dall'effetto immediato della carta

        assertFalse(giocatore.getAzioneBonusDaEffettuare());
        assertTrue(giocatore.CartePersonaggio.contains(governatore));
        assertNull(spazioAzione.CartaAssociata);
    }

    @Test
    public void piazzaFamiliare_EffettoScontoRisorsaCarte() throws Exception {
        Carta cavaliere = tabellone.getCartaByName("Cavaliere");
        //Prendo uno spazio azione dalla torre personaggi
        spazioAzione = tabellone.Torri.get(2).SpaziAzione.get(2);
        spazioAzione.AssociaCarta(cavaliere);


        //Assegna la carta dama al giocatore
        CartaPersonaggio dama = (CartaPersonaggio)tabellone.getCartaByName("Dama");
        giocatore.CartePersonaggio.add(dama);

        //Il familiare ha valore 4, lo spazio azione 5
        //Con l'effetto della dama può comunque prendere la nuova carta
        spazioAzione.PiazzaFamiliare(familiare, 0);

        assertEquals(familiare, spazioAzione.FamiliarePiazzato);
        assertEquals(4, giocatore.Risorse.getMonete()); //Spese per la carta (costa 2, ma viene scontata dall'effetto della dama)
        assertEquals(3, giocatore.Risorse.getLegno()); //Prese dal bonus spazio azione

        //funziona solo se eseguito da GiocatoreRemoto
        //assertEquals(1, giocatore.getPrivilegiDaScegliere()); //Presi dall'effetto immediato della carta
        assertFalse(giocatore.getAzioneBonusDaEffettuare());
        assertTrue(giocatore.CartePersonaggio.contains(cavaliere));
        assertNull(spazioAzione.CartaAssociata);
    }

    @Test
    public void validaPiazzamentoFamiliare_Ok() throws Exception {
        Carta monastero = tabellone.getCartaByName("Monastero");
        spazioAzione.AssociaCarta(monastero);

        spazioAzione.ValidaPiazzamentoFamiliare(familiare, false,1);
    }

    @Test
    public void validaPiazzamentoFamiliare_Occupato() throws Exception {
        exception.expect(DomainException.class);
        exception.expectMessage("Questo spazio azione è già occupato da un altro familiare!");

        Carta monastero = tabellone.getCartaByName("Monastero");
        spazioAzione.AssociaCarta(monastero);
        spazioAzione.FamiliarePiazzato = giocatore.getFamiliareByColor(ColoreDado.NEUTRO);

        spazioAzione.ValidaPiazzamentoFamiliare(familiare, false,1);
    }

    @Test
    public void validaPiazzamentoFamiliare_CartaMancante() throws Exception {
        exception.expect(DomainException.class);
        exception.expectMessage("A questo spazio azione non è associata alcuna carta!");

        spazioAzione.ValidaPiazzamentoFamiliare(familiare, false,1);
    }


    @Test
    public void validaPiazzamentoFamiliare_MonetePerTorreOccupata() throws Exception {
        exception.expect(DomainException.class);
        exception.expectMessage("Siccome la torre è occupata, sono necessarie almeno 3 monete per prendere la carta.");

        Carta monastero = tabellone.getCartaByName("Monastero");
        spazioAzione.AssociaCarta(monastero);
        giocatore.Risorse.setRisorse(Risorsa.TipoRisorsa.MONETE, 1);

        spazioAzione.ValidaPiazzamentoFamiliare(familiare, true,1);
    }

    @Test
    public void validaPiazzamentoFamiliare_RisorseInsufficienti() throws Exception {
        exception.expect(DomainException.class);
        exception.expectMessage("Non si dispone di risorse sufficienti per poter prendere la carta.");

        Carta basilica = tabellone.getCartaByName("Basilica");
        spazioAzione.AssociaCarta(basilica);

        spazioAzione.ValidaPiazzamentoFamiliare(familiare, true,1);
    }

    @Test
    public void validaPiazzamentoFamiliare_PuntiMilitariTerritori() throws Exception {
        exception.expect(DomainException.class);
        exception.expectMessage("Per poter prendere questa carta sono necessari almeno");
        exception.expectMessage("punti militari.");

        Carta avampostoCommerciale = tabellone.getCartaByName("AvampostoCommerciale");
        spazioAzione.AssociaCarta(avampostoCommerciale);

        CartaTerritorio borgo = (CartaTerritorio) tabellone.getCartaByName("Borgo");
        giocatore.CarteTerritorio.add(borgo);
        giocatore.CarteTerritorio.add(borgo);
        giocatore.CarteTerritorio.add(borgo);

        spazioAzione.ValidaPiazzamentoFamiliare(familiare, true,1);
    }


    @Test
    public void rimuoviFamiliari() throws Exception {
        spazioAzione.FamiliarePiazzato = familiare;
        spazioAzione.RimuoviFamiliari();

        assertNull(spazioAzione.FamiliarePiazzato);
    }

    @Test
    public void azioneBonusEffettuata() throws Exception {
        Carta innalzareUnaStatua = tabellone.getCartaByName("InnalzareUnaStatua");
        spazioAzione.AssociaCarta(innalzareUnaStatua);

        spazioAzione.AzioneBonusEffettuata(giocatore, 4, new Risorsa(Risorsa.TipoRisorsa.PIETRA, 2), 1);

        assertNull(spazioAzione.FamiliarePiazzato);
        assertEquals(5, giocatore.Risorse.getMonete()); //Immutate
        assertEquals(1, giocatore.Risorse.getLegno()); //Prese dal bonus SpazioAzione e spese per la carta
        assertEquals(1, giocatore.Risorse.getPietra()); //Prese dal bonus EffettoAzioneBonus e spese per la carta
        assertEquals(2, giocatore.Risorse.getServi()); //Spesi per aumento valore

        assertEquals(1, giocatore.getPrivilegiDaScegliere()); //Effetto carta
        assertFalse(giocatore.getAzioneBonusDaEffettuare());
        assertTrue(giocatore.CarteImpresa.contains(innalzareUnaStatua));
        assertNull(spazioAzione.CartaAssociata);
    }

}