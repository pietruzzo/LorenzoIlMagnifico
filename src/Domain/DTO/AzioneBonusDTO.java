package Domain.DTO;

import Domain.Risorsa;

import java.io.Serializable;

/**
 * Created by Michele on 06/06/2017.
 */
public class AzioneBonusDTO implements Serializable {

    //region Proprietà
    private final short idGiocatore;
    private final int valoreAzione;
    private final int idSpazioAzione;
    private final Risorsa bonusRisorse;
    //endregion

    //region Getters
    public short getIdGiocatore() {
        return idGiocatore;
    }

    public int getValoreAzione() {
        return valoreAzione;
    }

    public int getIdSpazioAzione() {
        return idSpazioAzione;
    }

    public Risorsa getBonusRisorse() {
        return bonusRisorse;
    }
    //endregion

    /**
     * Costruttore
     */
    public AzioneBonusDTO(short idGiocatore, int valoreAzione, int idSpazioAzione, Risorsa bonusRisorse) {
        this.idGiocatore = idGiocatore;
        this.valoreAzione = valoreAzione;
        this.idSpazioAzione = idSpazioAzione;
        this.bonusRisorse = bonusRisorse;
    }
}
