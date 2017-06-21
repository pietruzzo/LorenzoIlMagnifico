package Domain.DTO;

import Domain.Risorsa;

import java.io.Serializable;

/**
 * Created by Michele on 06/06/2017.
 */
public class AzioneBonusDTO implements Serializable {

    //region Proprietà
    private final int valoreAzione;
    private final int idSpazioAzione;
    private final Risorsa bonusRisorse;
    private final int servitoriAggiunti;
    //endregion

    //region Getters
    public int getValoreAzione() {
        return valoreAzione;
    }

    public int getIdSpazioAzione() {
        return idSpazioAzione;
    }

    public Risorsa getBonusRisorse() {
        return bonusRisorse;
    }

    public int getServitoriAggiunti() {
        return servitoriAggiunti;
    }
    //endregion

    /**
     * Costruttore
     */
    public AzioneBonusDTO(int valoreAzione, int idSpazioAzione, Risorsa bonusRisorse, int servitoriAggiunti) {
        this.valoreAzione = valoreAzione;
        this.idSpazioAzione = idSpazioAzione;
        this.bonusRisorse = bonusRisorse;
        this.servitoriAggiunti = servitoriAggiunti;
    }
}
