package Domain;

import Domain.Effetti.Effetto;
import Exceptions.DomainException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michele on 04/06/2017.
 */
public class CartaEdificioTest {
    CartaEdificio cartaEdificio;

    @Before
    public void setUp() throws Exception {
        cartaEdificio = new CartaEdificio("carta", 1, new Risorsa(), new ArrayList<Effetto>(), new ArrayList<Effetto>());

        assertEquals("carta", cartaEdificio.Nome);
        assertEquals(1, cartaEdificio.Periodo);
        assertEquals(0, cartaEdificio.CostoRisorse.getLegno());
        assertEquals(0, cartaEdificio.CostoRisorse.getPietra());
        assertEquals(0, cartaEdificio.CostoRisorse.getServi());
        assertEquals(0, cartaEdificio.CostoRisorse.getMonete());
    }

    @Test
    public void validaPresaCarta_OK() throws Exception {
        Giocatore giocatore = new Giocatore();
        Torre torre = new Torre(TipoCarta.Edificio);
        SpazioAzioneTorre spazioAzioneTorre = new SpazioAzioneTorre(1, new Risorsa(), torre);
        cartaEdificio.ValidaPresaCarta(giocatore, spazioAzioneTorre);
    }

    @Test (expected = DomainException.class)
    public void validaPresaCarta_PlanciaPiena() throws Exception {
        Giocatore giocatore = new Giocatore();
        Torre torre = new Torre(TipoCarta.Edificio);
        SpazioAzioneTorre spazioAzioneTorre = new SpazioAzioneTorre(1, new Risorsa(), torre);

        for (int i = 0; i<6; i++)
            giocatore.CarteEdificio.add(new CartaEdificio("nome", 1, new Risorsa(), new ArrayList<Effetto>(), new ArrayList<Effetto>()));

        cartaEdificio.ValidaPresaCarta(giocatore, spazioAzioneTorre);
    }

    @Test
    public void assegnaGiocatore() throws Exception {
        Giocatore giocatore = new Giocatore();
        cartaEdificio.AssegnaGiocatore(giocatore);

        assertEquals(1, giocatore.CarteEdificio.size());
    }

    @Test
    public void getTipoCarta() throws Exception {
        assertEquals(TipoCarta.Edificio, cartaEdificio.getTipoCarta());
    }

}