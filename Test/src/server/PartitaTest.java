package server;

import Domain.ColoreDado;
import Domain.DTO.PiazzaFamiliareDTO;
import Domain.Risorsa;
import Domain.SpazioAzione;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michele on 09/06/2017.
 */
public class PartitaTest {
    Partita partita;

    @Before
    public void setUp() throws Exception {
        partita = new Partita(new Server());
        partita.AggiungiGiocatore((short)1, "Michele", new GiocatoreRemotoForTest());
        partita.AggiungiGiocatore((short)2, "Pietro", new GiocatoreRemotoForTest());

        assertEquals(2, partita.getGiocatoriPartita().size());
        assertTrue(partita.getGiocatoriPartita().stream().allMatch(g -> g.getPartita().equals(partita)));
    }

    @Test
    public void inizioNuovoTurno() throws Exception {
        partita.InizioNuovoTurno();

        assertEquals(1, partita.getOrdineMossaCorrente());
        assertEquals(1, partita.getTurno());
        assertEquals(1, partita.getPeriodo());
    }

    @Test
    public void iniziaNuovaMossa_Mossa2() throws Exception {
        partita.InizioNuovoTurno();
        partita.IniziaNuovaMossa();

        assertEquals(2, partita.getOrdineMossaCorrente());
        assertEquals(1, partita.getTurno());
        assertEquals(1, partita.getTurno());
    }

    @Test
    public void iniziaNuovaMossa_Mossa3() throws Exception {
        partita.InizioNuovoTurno();
        partita.IniziaNuovaMossa();
        partita.IniziaNuovaMossa();

        assertEquals(1, partita.getOrdineMossaCorrente());
        assertEquals(1, partita.getTurno());
        assertEquals(1, partita.getTurno());
    }

    @Test
    public void iniziaNuovaMossa_SenzaFamiliari() throws Exception {
        partita.InizioNuovoTurno();

        partita.getGiocatoriPartita().stream().forEach(g -> g.SettaValoreFamiliare(new int[]{7,7,7}));
        partita.getGiocatoriPartita().stream().forEach(g -> g.OttieniBonusRisorse(new Risorsa(100,100,100,100,100,100,100)));

        for (GiocatoreRemoto giocatore : partita.getGiocatoriPartita()) {
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.NEUTRO, 1 + (4*(giocatore.getIdGiocatore()-1)), 1 ));
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.BIANCO, 2 + (4*(giocatore.getIdGiocatore()-1)), 0 ));
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.NERO, 3 + (4*(giocatore.getIdGiocatore())), 0 ));
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.ARANCIO, 4 + (4*(giocatore.getIdGiocatore()+1)), 0 ));
        }

        assertEquals(1, partita.getOrdineMossaCorrente());
        assertEquals(2, partita.getTurno());
        assertEquals(1, partita.getPeriodo());
    }

    @Test
    public void iniziaNuovaMossa_NuovoPeriodo() throws Exception {
        partita.InizioNuovoTurno();

        //Vengono assegnati 0 punti fede così da far scomunicare subito tutti i giocatori e passare al periodo successivo
        partita.getGiocatoriPartita().stream().forEach(g -> g.OttieniBonusRisorse(new Risorsa(100,100,100,100,100,100,0)));
        //Viene forzato il valore dei dadi a un valore alto così da non avere errori di validazione nel prendere le carte
        partita.getGiocatoriPartita().stream().forEach(g -> g.SettaValoreFamiliare(new int[]{7,7,7}));

        //Tutti e due i giocatori fanno un giro di mosse in modo da completare il turno
        for (GiocatoreRemoto giocatore : partita.getGiocatoriPartita()) {
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.NEUTRO, 1 + (4*(giocatore.getIdGiocatore()-1)), 1 ));
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.BIANCO, 2 + (4*(giocatore.getIdGiocatore()-1)), 0 ));
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.NERO, 3 + (4*(giocatore.getIdGiocatore())), 0 ));
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.ARANCIO, 4 + (4*(giocatore.getIdGiocatore()+1)), 0 ));
        }

        //Viene forzato il valore dei dadi a un valore alto così da non avere errori di validazione nel prendere le carte
        partita.getGiocatoriPartita().stream().forEach(g -> g.SettaValoreFamiliare(new int[]{7,7,7}));

        //Tutti e due i giocatori fanno un giro di mosse in modo da completare il turno
        for (GiocatoreRemoto giocatore : partita.getGiocatoriPartita()) {
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.NEUTRO, 1 + (4*(giocatore.getIdGiocatore()-1)), 1 ));
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.BIANCO, 2 + (4*(giocatore.getIdGiocatore()-1)), 0 ));
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.NERO, 3 + (4*(giocatore.getIdGiocatore())), 0 ));
            partita.PiazzaFamiliare(new PiazzaFamiliareDTO(giocatore.getIdGiocatore(), ColoreDado.ARANCIO, 4 + (4*(giocatore.getIdGiocatore()+1)), 0 ));
        }


        //Dopo aver completato due giri di mosse i giocatori hanno abbastanza punti fede per poter scegliere
        assertEquals(2, partita.getOrdineMossaCorrente());
        assertEquals(2, partita.getTurno());
        assertEquals(1, partita.getPeriodo());
    }

    @Test
    public void effettuaRapportoVaticano() throws Exception {
        GiocatoreRemoto michele = partita.getGiocatoriPartita().get(0);
        GiocatoreRemoto pietro = partita.getGiocatoriPartita().get(1);
        partita.InizioNuovoTurno();

        //cosi facendo pietro potrà scegliere se sostenere la chiesa o no.
        //mentre michele verrà scomunicato direttamente
        pietro.OttieniBonusRisorse(new Risorsa(Risorsa.TipoRisorsa.PFEDE, 10));
        partita.EffettuaRapportoVaticano();

        assertTrue(michele.getRapportoVaticanoEffettuato());
        assertFalse(pietro.getRapportoVaticanoEffettuato());
        assertEquals(1, partita.getTurno());
        assertEquals(1, partita.getPeriodo());
    }

    @Test
    public void rispostaSostegnoChiesa_Si() throws Exception {
        GiocatoreRemoto michele = partita.getGiocatoriPartita().get(0);
        GiocatoreRemoto pietro = partita.getGiocatoriPartita().get(1);
        partita.InizioNuovoTurno();

        //cosi facendo pietro potrà scegliere se sostenere la chiesa o no.
        //mentre michele verrà scomunicato direttamente
        pietro.OttieniBonusRisorse(new Risorsa(Risorsa.TipoRisorsa.PFEDE, 10));
        partita.EffettuaRapportoVaticano();

        //Se pietro sostiene la chiesa allora perde i suoi punti fede e si passa al turno successivo
        partita.RispostaSostegnoChiesa(pietro, true);

        //il flag per il rapporto viene azzerato con il nuovo turno
        assertFalse(michele.getRapportoVaticanoEffettuato());
        assertFalse(pietro.getRapportoVaticanoEffettuato());
        assertEquals(2, partita.getTurno());
    }

    @Test
    public void rispostaSostegnoChiesa_No() throws Exception {
        GiocatoreRemoto michele = partita.getGiocatoriPartita().get(0);
        GiocatoreRemoto pietro = partita.getGiocatoriPartita().get(1);
        partita.InizioNuovoTurno();

        //cosi facendo pietro potrà scegliere se sostenere la chiesa o no.
        //mentre michele verrà scomunicato direttamente
        pietro.OttieniBonusRisorse(new Risorsa(Risorsa.TipoRisorsa.PFEDE, 10));
        partita.EffettuaRapportoVaticano();

        //Se pietro non sostiene la chiesa allora viene scomunicato e si passa al turno successivo
        partita.RispostaSostegnoChiesa(pietro, false);

        //il flag per il rapporto viene azzerato con il nuovo turno
        assertFalse(michele.getRapportoVaticanoEffettuato());
        assertFalse(pietro.getRapportoVaticanoEffettuato());
        assertEquals(2, partita.getTurno());
    }

    @Test
    public void piazzaFamiliare_PalazzoConsiglio()throws Exception {
        GiocatoreRemoto michele = partita.getGiocatoriPartita().get(0);
        partita.InizioNuovoTurno();

        partita.PiazzaFamiliare(new PiazzaFamiliareDTO(michele.getIdGiocatore(), ColoreDado.NEUTRO, 25, 1));

        //Piazzando nel palazzo del consiglio deve scegliere una pergamena
        assertEquals(1, michele.getPrivilegiDaScegliere());
    }
}