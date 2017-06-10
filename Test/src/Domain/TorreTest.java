package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Michele on 04/06/2017.
 */
public class TorreTest {
    private Torre torre;

    @Before
    public void setUp() throws Exception {
        torre = new Torre(TipoCarta.Territorio);

        assertEquals(TipoCarta.Territorio, torre.Tipo);
        assertEquals(4, torre.SpaziAzione.size());
        assertEquals(1, torre.SpaziAzione.get(0).Valore);
        assertEquals(7, torre.SpaziAzione.get(3).Valore);
    }

    @Test
    public void torreOccupata() throws Exception {
        assertFalse(torre.TorreOccupata());

        Giocatore giocatore = new Giocatore();
        torre.SpaziAzione.get(1).FamiliarePiazzato = new Familiare(giocatore, ColoreDado.NERO);

        assertTrue(torre.TorreOccupata());
    }

    @Test(expected = DomainException.class)
    public void validaPiazzamentoFamiliare_stessoGiocatore() throws Exception {
        Giocatore giocatore = new Giocatore();
        torre.SpaziAzione.get(1).FamiliarePiazzato = new Familiare(giocatore, ColoreDado.NERO);

        torre.ValidaPiazzamentoFamiliare(new Familiare(giocatore, ColoreDado.ARANCIO));
    }

    @Test
    public void validaPiazzamentoFamiliare_familiareNeutro() throws Exception {
        Giocatore giocatore = new Giocatore();
        torre.SpaziAzione.get(1).FamiliarePiazzato = new Familiare(giocatore, ColoreDado.NERO);

        torre.ValidaPiazzamentoFamiliare(new Familiare(giocatore, ColoreDado.NEUTRO));
    }

    @Test
    public void validaPiazzamentoFamiliare_torreVuota() throws Exception {
        Giocatore giocatore = new Giocatore();
        torre.ValidaPiazzamentoFamiliare(new Familiare(giocatore, ColoreDado.NEUTRO));
    }

    @Test
    public void pescaCarte() throws Exception {
        ArrayList<Carta> carteDisponibili = new ArrayList<>();

        for(int i = 0; i < 4; i++ )
            carteDisponibili.add(new CartaTerritorio("nome"+i, 1, new ArrayList<>(), new ArrayList<>()));

        torre.PescaCarte(1, carteDisponibili);

        assertTrue(torre.SpaziAzione.stream().allMatch(x -> x.FamiliarePiazzato == null));
        assertTrue(torre.SpaziAzione.stream().allMatch(x -> x.CartaAssociata != null));
    }

}