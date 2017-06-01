package Domain;

import Exceptions.DomainException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

            SpaziAzione.add(new SpazioAzioneTorre(valore, new Risorsa(bonusLegni, bonusPietre,0, bonusMonete,0, bonusMilitare,0), this));
        }
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
    protected Boolean TorreOccupata() {
        return this.SpaziAzione.stream().anyMatch(x -> x.FamiliarePiazzato != null);
    }

    /**
     * Verifica se è possibile piazzare il familiare nella torre
     */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare) throws DomainException {
        if(this.SpaziAzione.stream().anyMatch(x -> x.FamiliarePiazzato != null
                                                && x.FamiliarePiazzato.Giocatore == familiare.Giocatore
                                                && x.FamiliarePiazzato.Neutro == familiare.Neutro))
            throw new DomainException("Non è possibile piazzare un altro familiare dello stesso colore nella torre!");
    }

    /**
     * Associa le carte agli spazi azione
     */
    protected void PescaCarte(int periodo, List<Carta> carteDisponibili)
    {
        List<Carta> carteDaPescare = new ArrayList<>();
        int numeroSpaziAzione = this.SpaziAzione.size(); //sempre 4

        //region Scelta carte da pescare
        switch (this.Tipo)
        {
            case Territorio:
                carteDaPescare = carteDisponibili.stream().filter(c -> c.Periodo == periodo && c instanceof CartaTerritorio)
                        .limit(numeroSpaziAzione)
                        .collect(Collectors.toList());
                break;

            case Personaggio:
                carteDaPescare = carteDisponibili.stream().filter(c -> c.Periodo == periodo && c instanceof CartaPersonaggio)
                        .limit(numeroSpaziAzione)
                        .collect(Collectors.toList());
                break;

            case Impresa:
                carteDaPescare = carteDisponibili.stream().filter(c -> c.Periodo == periodo && c instanceof CartaImpresa)
                        .limit(numeroSpaziAzione)
                        .collect(Collectors.toList());
                break;

            case Edificio:
                carteDaPescare = carteDisponibili.stream().filter(c -> c.Periodo == periodo && c instanceof CartaEdificio)
                        .limit(numeroSpaziAzione)
                        .collect(Collectors.toList());
                break;

            default:
                carteDaPescare = new ArrayList<>();
                break;
        }
        //endregion

        //Associa una carta ad ogni spazio azione
        for (int i = 0; i < carteDaPescare.size(); i++)
        {
            this.SpaziAzione.get(i).AssociaCarta(carteDaPescare.get(i));
        }
    }
}
