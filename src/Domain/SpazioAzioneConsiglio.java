package Domain;

import Exceptions.DomainException;
import Exceptions.NetworkException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Portatile on 13/05/2017.
 */
public class SpazioAzioneConsiglio extends SpazioAzione  implements Serializable {

    protected List<Familiare> FamiliariPiazzati;

    /**
     * Costruttore
     */
    public SpazioAzioneConsiglio(int valore, Risorsa bonusRisorse)
    {
        super(valore, bonusRisorse);
        this.FamiliariPiazzati = new ArrayList<>();
        this.Tipo = TipoSpazioAzione.Consiglio;
    }

    /**
     * Consente di piazzare un familiare nello spazioAzione, previa verifica
     */
    @Override
    public void PiazzaFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException
    {
        if(familiare.Giocatore.Risorse.getServi() < servitoriAggiunti)
            throw new DomainException("Non si dispone di servitori a sufficienza!");

        super.ValidaPiazzamentoFamiliare(familiare, servitoriAggiunti);
        super.PiazzaFamiliare(familiare, servitoriAggiunti);
        this.FamiliariPiazzati.add(familiare);

        //Se è stato piazzato su uno spazio azione consiglio deve chiedere il privilegio del consiglio
        try {
            familiare.Giocatore.SceltaPrivilegioConsiglio(1);
        } catch (NetworkException e) {
            System.out.println("Giocatore non più connesso");
        }
    }

    /**
     * Toglie tutti i familiari dallo spazio azione
     */
    @Override
    protected void RimuoviFamiliari() {
        this.FamiliariPiazzati = new ArrayList<>();
    }


    /**
     * Non è possibile effettuare un'azione bonus nello spazio azione del consiglio
     */
    @Override
    protected void AzioneBonusEffettuata(Giocatore giocatore, int valoreAzione, Risorsa bonusRisorse, int servitoriAggiunti) throws DomainException {
        throw new DomainException("Non è possibile effettuare l'azione bonus nello spazio azione del Consiglio");
    }
}
