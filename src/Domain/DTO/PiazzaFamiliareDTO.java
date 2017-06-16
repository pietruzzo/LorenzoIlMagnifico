package Domain.DTO;

import Domain.ColoreDado;

import java.io.Serializable;

/**
 * Created by Michele on 03/06/2017.
 */
public class PiazzaFamiliareDTO implements Serializable{

    //region Proprietà
    private final ColoreDado coloreDado;
    private final int idSpazioAzione;
    private final int servitoriAggiunti;
    //endregion

    //region Getters
    public ColoreDado getColoreDado() {
        return coloreDado;
    }

    public int getIdSpazioAzione() {
        return idSpazioAzione;
    }

    public int getServitoriAggiunti () {return servitoriAggiunti; }
    //endregion

    /**
     * Costruttore
     */
    public PiazzaFamiliareDTO(ColoreDado coloreDado, int idSpazioAzione, int servitoriAggiunti)
    {
        this.coloreDado = coloreDado;
        this.idSpazioAzione = idSpazioAzione;
        this.servitoriAggiunti = servitoriAggiunti;
    }


}
