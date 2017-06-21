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
public class CartaTerritorioTest {
    CartaTerritorio cartaTerritorio;

    @Before
    public void setUp() throws Exception {
        cartaTerritorio = new CartaTerritorio("carta", 1, 1, new ArrayList<Effetto>(), new ArrayList<Effetto>());

        assertEquals("carta", cartaTerritorio.Nome);
        assertEquals(1, cartaTerritorio.Periodo);
        assertEquals(0, cartaTerritorio.getCostoRisorse().getLegno());
        assertEquals(0, cartaTerritorio.getCostoRisorse().getPietra());
        assertEquals(0, cartaTerritorio.getCostoRisorse().getServi());
        assertEquals(0, cartaTerritorio.getCostoRisorse().getMonete());
        assertEquals(1, cartaTerritorio.getValoreAzione());
    }

    @Test
    public void validaPresaCarta_OK() throws Exception {
        Giocatore giocatore = new Giocatore();
        Torre torre = new Torre(TipoCarta.Edificio);
        SpazioAzioneTorre spazioAzioneTorre = new SpazioAzioneTorre(1, new Risorsa(), torre);
        cartaTerritorio.ValidaPresaCarta(giocatore, spazioAzioneTorre);
    }

    @Test (expected = DomainException.class)
    public void validaPresaCarta_PlanciaPiena() throws Exception {
        Giocatore giocatore = new Giocatore();
        Torre torre = new Torre(TipoCarta.Territorio);
        SpazioAzioneTorre spazioAzioneTorre = new SpazioAzioneTorre(1, new Risorsa(), torre);

        for (int i = 0; i<6; i++)
            giocatore.CarteTerritorio.add(new CartaTerritorio("nome", 1, 0, new ArrayList<>(), new ArrayList<>()));

        cartaTerritorio.ValidaPresaCarta(giocatore, spazioAzioneTorre);
    }

    @Test
    public void assegnaGiocatore() throws Exception {
        Giocatore giocatore = new Giocatore();
        cartaTerritorio.AssegnaGiocatore(giocatore);

        assertEquals(1, giocatore.CarteTerritorio.size());
    }

    @Test
    public void getTipoCarta() throws Exception {
        assertEquals(TipoCarta.Territorio, cartaTerritorio.getTipoCarta());
    }
}