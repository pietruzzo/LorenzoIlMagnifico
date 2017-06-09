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
public class CartaImpresaTest {
    CartaImpresa cartaImpresa;

    @Before
    public void setUp() throws Exception {
        cartaImpresa = new CartaImpresa("carta", 1, new ArrayList<Effetto>(), new ArrayList<Effetto>());

        assertEquals("carta", cartaImpresa.Nome);
        assertEquals(1, cartaImpresa.Periodo);
        assertEquals(0, cartaImpresa.getCostoRisorse().getLegno());
        assertEquals(0, cartaImpresa.getCostoRisorse().getPietra());
        assertEquals(0, cartaImpresa.getCostoRisorse().getServi());
        assertEquals(0, cartaImpresa.getCostoRisorse().getMonete());
        assertFalse(cartaImpresa.SceltaCosto);
    }

    @Test
    public void validaPresaCarta_OK() throws Exception {
        Giocatore giocatore = new Giocatore();
        Torre torre = new Torre(TipoCarta.Impresa);
        SpazioAzioneTorre spazioAzioneTorre = new SpazioAzioneTorre(1, new Risorsa(), torre);
        cartaImpresa.ValidaPresaCarta(giocatore, spazioAzioneTorre);
    }

    @Test (expected = DomainException.class)
    public void validaPresaCarta_PlanciaPiena() throws Exception {
        Giocatore giocatore = new Giocatore();
        Torre torre = new Torre(TipoCarta.Impresa);
        SpazioAzioneTorre spazioAzioneTorre = new SpazioAzioneTorre(1, new Risorsa(), torre);

        for (int i = 0; i<6; i++)
            giocatore.CarteImpresa.add(new CartaImpresa("nome", 1, new ArrayList<Effetto>(), new ArrayList<Effetto>()));

        cartaImpresa.ValidaPresaCarta(giocatore, spazioAzioneTorre);
    }

    @Test
    public void assegnaGiocatore() throws Exception {
        Giocatore giocatore = new Giocatore();
        cartaImpresa.AssegnaGiocatore(giocatore);

        assertEquals(1, giocatore.CarteImpresa.size());
    }

    @Test
    public void getTipoCarta() throws Exception {
        assertEquals(TipoCarta.Impresa, cartaImpresa.getTipoCarta());
    }

}