package Domain.DTO;

import Domain.ColoreDado;
import Domain.Risorsa;

import java.io.Serializable;

/**
 * Created by Michele on 03/06/2017.
 * Classe utilizzata per la comunicazione client server
 * Viene sfruttata per tutti gli aggiornamenti che riguardano il giocatore e le sue proprietà
 */
public class UpdateGiocatoreDTO implements Serializable {

    //region Proprietà
    private final int idGiocatore;
    private Risorsa risorse;
    private final ColoreDado coloreDado;
    private final Integer idSpazioAzione;
    //endregion

    //region Getters
    public int getIdGiocatore() {
        return idGiocatore;
    }

    public Risorsa getRisorse() {
        return risorse;
    }

    public ColoreDado getColoreDado() {
        return coloreDado;
    }

    public Integer getIdSpazioAzione() {
        return idSpazioAzione;
    }

    public void setRisorse(Risorsa risorse) {
        this.risorse = risorse;
    }
    //endregion

    /**
     * Costruttore
     */
    public UpdateGiocatoreDTO(int idGiocatore, Risorsa risorsa, ColoreDado coloreDado, Integer idSpazioAzione)
    {
        this.idGiocatore = idGiocatore;
        this.risorse = risorsa;
        this.coloreDado = coloreDado;
        this.idSpazioAzione = idSpazioAzione;
    }

}
