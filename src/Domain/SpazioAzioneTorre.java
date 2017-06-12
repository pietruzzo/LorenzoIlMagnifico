package Domain;

import Exceptions.DomainException;

import java.io.Serializable;

/**
 * Created by Portatile on 13/05/2017.
 */
public class SpazioAzioneTorre extends SpazioAzione  implements Serializable
{

    protected Familiare FamiliarePiazzato;
    protected Carta CartaAssociata;
    protected Torre Torre;

    /**
     * Costruttore
     */
    public SpazioAzioneTorre(int valore, Risorsa bonusRisorse, Torre torre) {
        super(valore, bonusRisorse);

        this.Tipo = TipoSpazioAzione.Torre;
        this.Torre = torre;
    }

    /**
     * Ritorna la carta associata allo spazioAzione
     */
    public Carta getCartaAssociata() {
        return CartaAssociata;
    }

    /**
     * Associa una carta allo spazio azione
     */
    public void AssociaCarta(Carta cartaDaAssociare)
    {
        this.CartaAssociata = cartaDaAssociare;
    }

    /**
     * Consente di piazzare un familiare nello spazioAzione, previa verifica
     */
    @Override
    public void PiazzaFamiliare(Familiare familiare, int servitoriAggiunti) throws DomainException {
        Boolean torreOccupata = this.Torre.TorreOccupata();

        this.ValidaPiazzamentoFamiliare(familiare, torreOccupata, servitoriAggiunti);
        super.PiazzaFamiliare(familiare, servitoriAggiunti);
        this.FamiliarePiazzato = familiare;
        this.FamiliarePiazzato.Giocatore.PagaRisorse(this.CartaAssociata.getCostoRisorse());
        if(torreOccupata)
            this.FamiliarePiazzato.Giocatore.PagaRisorse(new Risorsa(Risorsa.TipoRisorsa.MONETE, 3));

        this.CartaAssociata.AssegnaGiocatore(familiare.Giocatore);
        this.CartaAssociata = null;

    }


    /** Verifica se è possibile piazzare il familiare nello spazio azione */
    protected void ValidaPiazzamentoFamiliare(Familiare familiare, Boolean torreOccupata, int servitoriAggiunti) throws DomainException {
        //Effettua i controlli legati alla torre di appartenenza
        this.Torre.ValidaPiazzamentoFamiliare(familiare);

        if(this.FamiliarePiazzato != null)
            throw new DomainException("Questo spazio azione è già occupato da un altro familiare!");
        if(this.CartaAssociata == null)
            throw new DomainException("A questo spazio azione non è associata alcuna carta!");

        Risorsa costoEffetti = super.ValidaValoreAzione(familiare, servitoriAggiunti);

        //Calcola il malus dovuto dall'occupazione della torre
        //Se la torre è occupata il malus è di -3 monete
        Risorsa malusTorreOccupata = new Risorsa();
        if(torreOccupata)
            this.CalcolaMalusTorreOccupata(familiare.Giocatore);

        //Valuta se il giocatore rimarrebbe con tutte le risorse in positivo prendendo la carta
        //Considera il bonus dello spazio azione, il costo della carta, il malus della torre occupata e gli effetti delle carte (anche le carte scomunica)
        if(!Risorsa.sub(Risorsa.add(familiare.Giocatore.Risorse, this.BonusRisorse),
                        Risorsa.add(Risorsa.add(this.CartaAssociata.getCostoRisorse(), malusTorreOccupata), costoEffetti)).isPositivo())
            throw new DomainException("Non si dispone di risorse sufficienti per poter prendere la carta.");

        //Valuta se il giocatore ha abbastanza spazio nella plancia per prendere la carta
        this.CartaAssociata.ValidaPresaCarta(familiare.Giocatore, this);

        if(this.CartaAssociata instanceof CartaTerritorio)
            this.ValidaCartaTerritorio(familiare.Giocatore, costoEffetti.getPuntiMilitari());
    }

    /**
     * Valuta se il giocatore ha abbastanza punti militari per poter piazzare la carta nella plancia
     */
    private void ValidaCartaTerritorio(Giocatore giocatore, int costoPuntiMilitariEffetti) throws DomainException {
        int minimoPuntiMilitari = 0;
        switch (giocatore.CarteTerritorio.size())
        {
            case 2:
                minimoPuntiMilitari = 1;
                break;

            case 3:
                minimoPuntiMilitari = 4;
                break;

            case 4:
                minimoPuntiMilitari = 10;
                break;

            case 5:
                minimoPuntiMilitari = 20;
                break;
        }

        if((giocatore.Risorse.getPuntiMilitari() + this.BonusRisorse.getPuntiMilitari() - costoPuntiMilitariEffetti) < minimoPuntiMilitari)
            throw new DomainException(String.format("Per poter prendere questa carta sono necessari almeno %d punti militari.", minimoPuntiMilitari));
    }

    /**
     * Toglie tutti i familiari dallo spazio azione
     */
    @Override
    protected void RimuoviFamiliari() {
        this.FamiliarePiazzato = null;
    }


    /**
     * Metodo per effettuare azioni bonus (ovvero senza piazzamento familiare) negli spazi azione Torre
     * Effettua un’azione del valore specificato per prendere una carta dalla torre senza piazzare un familiare
     * Paga 3 monete aggiuntive se la torre è occupata e prende l'eventuale bonus dello spazio azione
     * @param giocatore giocatore che effettua l'azione
     * @param valoreAzione valore dell'azione
     */
    @Override
    protected void AzioneBonusEffettuata(Giocatore giocatore, int valoreAzione, Risorsa bonusRisorse, int servitoriAggiunti) throws DomainException {
        Boolean torreOccupata = this.Torre.TorreOccupata();
        Risorsa costoEffetti = super.ValidaValoreAzioneBonus(giocatore, valoreAzione, servitoriAggiunti);

        //Calcola il malus dovuto dall'occupazione della torre
        //Se la torre è occupata il malus è di -3 monete
        Risorsa malusTorreOccupata = new Risorsa();
        if(torreOccupata)
            malusTorreOccupata = this.CalcolaMalusTorreOccupata(giocatore);

        //Se lo spazio azione ha ancora associato una carta
        if(this.CartaAssociata != null) {
            //Applica il bonus dovuto all'azione immediata
            Risorsa costoCarta = Risorsa.sub(this.CartaAssociata.getCostoRisorse(), bonusRisorse);

            //Valuta se il giocatore rimarrebbe con tutte le risorse in positivo prendendo la carta
            //Considera il bonus dello spazio azione, il costo della carta, il malus della torre occupata e gli effetti delle carte (anche le carte scomunica)
            if(!Risorsa.sub(Risorsa.add(giocatore.Risorse, this.BonusRisorse),
                    Risorsa.add(Risorsa.add(costoCarta, malusTorreOccupata), costoEffetti)).isPositivo())
                throw new DomainException("Non si dispone di risorse sufficienti per poter prendere la carta.");

            //Valuta se il giocatore ha abbastanza spazio nella plancia per prendere la carta
            this.CartaAssociata.ValidaPresaCarta(giocatore, this);

            if(this.CartaAssociata instanceof CartaTerritorio)
                this.ValidaCartaTerritorio(giocatore, costoEffetti.getPuntiMilitari());

            super.AzioneBonusEffettuata(giocatore, valoreAzione, bonusRisorse, servitoriAggiunti);

            giocatore.PagaRisorse(costoCarta);
            this.CartaAssociata.AssegnaGiocatore(giocatore);
            this.CartaAssociata = null;
        }
        else
            super.AzioneBonusEffettuata(giocatore, valoreAzione, bonusRisorse, servitoriAggiunti);

        if(torreOccupata)
            giocatore.PagaRisorse(malusTorreOccupata);
    }

    /**
     * Calcola il malus dovuto alla torre occupata
     * @param giocatore giocatore che deve effettuare l'azione
     * @return la risorsa corrispondente al malus
     * @throws DomainException se il giocatore non può pagare il malus
     */
    private Risorsa CalcolaMalusTorreOccupata(Giocatore giocatore) throws DomainException {
        Risorsa malusTorreOccupata = new Risorsa();
        if(giocatore.Risorse.getMonete() < 3)
            throw new DomainException("Siccome la torre è occupata, sono necessarie almeno 3 monete per prendere la carta.");
        else
            malusTorreOccupata.setRisorse(Risorsa.TipoRisorsa.MONETE, 3);

        return malusTorreOccupata;
    }
}
