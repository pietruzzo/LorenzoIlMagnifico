package Domain;

import static org.junit.Assert.*;

/**
 * Created by Michele on 04/06/2017.
 */
public class RisorsaTest {
    @org.junit.Test
    public void setRisorse() throws Exception {
        Risorsa risorsa = new Risorsa(1,1,1,1,0,0,0);
        risorsa.setRisorse(Risorsa.TipoRisorsa.LEGNO, 2);

        assertEquals(2, risorsa.getLegno());
    }

    @org.junit.Test
    public void add() throws Exception {
        Risorsa risorsa = new Risorsa(1,1,1,1,2,2,2);
        risorsa = Risorsa.add(risorsa, risorsa);

        assertEquals(2, risorsa.getLegno());
        assertEquals(2, risorsa.getPietra());
        assertEquals(2, risorsa.getServi());
        assertEquals(2, risorsa.getMonete());
        assertEquals(4, risorsa.getPuntiVittoria());
        assertEquals(4, risorsa.getPuntiMilitari());
        assertEquals(4, risorsa.getPuntiFede());
    }

    @org.junit.Test
    public void sub() throws Exception {
        Risorsa risorsa = new Risorsa(1,1,1,1,2,2,2);
        risorsa = Risorsa.sub(risorsa, risorsa);

        assertEquals(0, risorsa.getLegno());
        assertEquals(0, risorsa.getPietra());
        assertEquals(0, risorsa.getServi());
        assertEquals(0, risorsa.getMonete());
        assertEquals(0, risorsa.getPuntiVittoria());
        assertEquals(0, risorsa.getPuntiMilitari());
        assertEquals(0, risorsa.getPuntiFede());
    }

    @org.junit.Test
    public void isPositivo() throws Exception {
        Risorsa risorsa = new Risorsa(1,1,1,1,2,2,2);
        assertTrue(risorsa.isPositivo());

        risorsa.setRisorse(Risorsa.TipoRisorsa.MONETE, -1);
        assertFalse(risorsa.isPositivo());
    }

    @org.junit.Test
    public void multScalare() throws Exception {
        Risorsa risorsa = new Risorsa(1,1,1,1,2,1,-2);
        risorsa = risorsa.multScalare(5);

        assertEquals(5, risorsa.getLegno());
        assertEquals(5, risorsa.getPietra());
        assertEquals(5, risorsa.getServi());
        assertEquals(5, risorsa.getMonete());
        assertEquals(10, risorsa.getPuntiVittoria());
        assertEquals(5, risorsa.getPuntiMilitari());
        assertEquals(-10, risorsa.getPuntiFede());
    }

    @org.junit.Test
    public void testclone() throws Exception {
        Risorsa risorsa = new Risorsa(1,1,1,1,2,0,-2);
        Risorsa risorsaClone = risorsa.clone();

        assertEquals(1, risorsaClone.getLegno());
        assertEquals(1, risorsaClone.getPietra());
        assertEquals(1, risorsaClone.getServi());
        assertEquals(1, risorsaClone.getMonete());
        assertEquals(2, risorsaClone.getPuntiVittoria());
        assertEquals(0, risorsaClone.getPuntiMilitari());
        assertEquals(-2, risorsaClone.getPuntiFede());
    }

    @org.junit.Test
    public void setNegToZero() throws Exception {
        Risorsa risorsa = new Risorsa(1,1,1,-1,2,0,-2);
        risorsa = Risorsa.setNegToZero(risorsa);

        assertEquals(1, risorsa.getLegno());
        assertEquals(1, risorsa.getPietra());
        assertEquals(1, risorsa.getServi());
        assertEquals(0, risorsa.getMonete());
        assertEquals(2, risorsa.getPuntiVittoria());
        assertEquals(0, risorsa.getPuntiMilitari());
        assertEquals(0, risorsa.getPuntiFede());
    }

}