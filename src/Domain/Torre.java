package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Portatile on 13/05/2017.
 */
public class Torre {

    //region Proprieta
    protected TipoCarta Tipo;
    protected List<SpazioAzioneTorre> SpaziAzione;
    //endregion

    /**
     * Costruttore Torre
     * @param tipo Indica il tipo della torre
     */
    public Torre(TipoCarta tipo)
    {
        this.Tipo = tipo;
        this.SpaziAzione = new ArrayList<>();
        int bonus = 0;

        for (int valore = 1; valore <= 7; valore+=2)
        {
            if(valore == 5)
                bonus = 1;
            if(valore == 7)
                bonus = 2;

            int bonusLegni = 0, bonusPietre = 0, bonusMonete = 0, bonusMilitare = 0;
            switch (tipo)
            {
                case Territorio:
                    bonusLegni = bonus;
                    break;
                case Personaggio:
                    bonusPietre = bonus;
                    break;
                case Edificio:
                    bonusMilitare = bonus;
                    break;
                case Impresa:
                    bonusMonete = bonus;
                    break;
            }

            SpaziAzione.add(new SpazioAzioneTorre(valore, bonusLegni, bonusPietre, bonusMonete, bonusMilitare ));
        }
    }

    /**
     * Consente di piazzare un familiare nella torre, sullo spazio azione indicato dal valore
     */
    public void PiazzaFamiliare(Familiare familiare, int valore) throws Exception {
        this.ValidaPiazzamentoFamiliare(familiare);

        SpazioAzioneTorre spazioAzione = this.GetSpazioAzioneByValore(valore);
        Boolean torreOccupata = this.TorreOccupata();
        spazioAzione.PiazzaFamiliare(familiare, torreOccupata);
    }

    /**
     * Ritorna lo spazioAzione dato il suo valore (univoco nella torre)
     */
    private SpazioAzioneTorre GetSpazioAzioneByValore(int valore) {
        return this.SpaziAzione.stream().filter(x -> x.Valore == valore).findFirst().orElse(null);
    }

    /**
     * Ritorna true se la torre risulta occupata da un qualsiasi altro familiare
     */
    private Boolean TorreOccupata() {
        return this.SpaziAzione.stream().anyMatch(x -> x.FamiliarePiazzato != null);
    }

    /**
     * Verifica se è possibile piazzare il familiare nella torre
     */
    private void ValidaPiazzamentoFamiliare(Familiare familiare) throws Exception {
        if(this.SpaziAzione.stream().anyMatch(x -> x.FamiliarePiazzato != null
                                                && x.FamiliarePiazzato.Giocatore == familiare.Giocatore
                                                && x.FamiliarePiazzato.Neutro == familiare.Neutro))
            throw new Exception("Non è possibile piazzare un altro familiare dello stesso colore nella torre!");
    }

}
