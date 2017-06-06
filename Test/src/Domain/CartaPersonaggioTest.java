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
public class CartaPersonaggioTest {
    CartaPersonaggio cartaPersonaggio;

    @Before
    public void setUp() throws Exception {
        cartaPersonaggio = new CartaPersonaggio("carta", 1, new Risorsa(), new ArrayList<Effetto>(), new ArrayList<Effetto>());

        assertEquals("carta", cartaPersonaggio.Nome);
        assertEquals(1, cartaPersonaggio.Periodo);
        assertEquals(0, cartaPersonaggio.CostoRisorse.getLegno());
        assertEquals(0, cartaPersonaggio.CostoRisorse.getPietra());
        assertEquals(0, cartaPersonaggio.CostoRisorse.getServi());
        assertEquals(0, cartaPersonaggio.CostoRisorse.getMonete());
    }

    @Test
    public void validaPresaCarta_OK() throws Exception {
        Giocatore giocatore = new Giocatore();
        Torre torre = new Torre(TipoCarta.Personaggio);
        SpazioAzioneTorre spazioAzioneTorre = new SpazioAzioneTorre(1, new Risorsa(), torre);
        cartaPersonaggio.ValidaPresaCarta(giocatore, spazioAzioneTorre);
    }

    @Test (expected = DomainException.class)
    public void validaPresaCarta_PlanciaPiena() throws Exception {
        Giocatore giocatore = new Giocatore();
        Torre torre = new Torre(TipoCarta.Personaggio);
        SpazioAzioneTorre spazioAzioneTorre = new SpazioAzioneTorre(1, new Risorsa(), torre);

        for (int i = 0; i<6; i++)
            giocatore.CartePersonaggio.add(new CartaPersonaggio("nome", 1, new Risorsa(), new ArrayList<Effetto>(), new ArrayList<Effetto>()));

        cartaPersonaggio.ValidaPresaCarta(giocatore, spazioAzioneTorre);
    }

    @Test
    public void assegnaGiocatore() throws Exception {
        Giocatore giocatore = new Giocatore();
        cartaPersonaggio.AssegnaGiocatore(giocatore);

        assertEquals(1, giocatore.CartePersonaggio.size());
    }

    @Test
    public void getTipoCarta() throws Exception {
        assertEquals(TipoCarta.Personaggio, cartaPersonaggio.getTipoCarta());
    }
}